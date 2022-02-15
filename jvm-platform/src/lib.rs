#![allow(non_snake_case)]

extern crate jvm_rs;

use std::{
    ffi::{c_void, CString},
    os::raw::{c_int, c_char, c_uchar},
    ptr::null_mut,
    mem::size_of,
};
use jvm_rs::{
    jni::{
        JavaVM,
        jboolean,
        jclass,
        jint,
        JNI_VERSION_1_2,
        JNIEnv,
        jobjectArray,
        jstring,
        JNI_OK,
        jobject,
        jmethodID,
        jbyte,
        jlong,
        jsize
    },
    jvmti::{
        JVMTI_VERSION_1_0,
        jvmtiEnv,
        jvmtiCapabilities,
        jvmtiEvent_JVMTI_EVENT_CLASS_FILE_LOAD_HOOK,
        jvmtiEventCallbacks,
        jvmtiEventMode_JVMTI_ENABLE,
        jvmtiError_JVMTI_ERROR_NONE,
    },
};

static mut JVMTI: *mut jvmtiEnv = null_mut();

static mut SERVICE_CLASS: jclass = null_mut();
static mut TRANSFORM_METHOD: jmethodID = null_mut();

#[no_mangle]
pub unsafe extern "system" fn JNI_OnLoad(_vm: *mut JavaVM, _reserved: &mut c_void) -> c_int {
    let mut ptr: *mut c_void = null_mut();
    let result = (*(*_vm)).GetEnv.unwrap()(_vm, &mut ptr, JVMTI_VERSION_1_0 as jint);
    if result == JNI_OK as jint {
        JVMTI = ptr.cast::<jvmtiEnv>();

        let mut ver: jint = -1;
        let mut error = (*(*JVMTI)).GetVersionNumber.unwrap()(JVMTI, &mut ver);
        if error != jvmtiError_JVMTI_ERROR_NONE {
            println!("Something has gone horribly wrong. @ GetVersionNumber, ver/error: {}/{}", ver, error);
            return -1;
        }

        let mut capabilities = jvmtiCapabilities {
            _bitfield_1: Default::default()
        };
        error = (*(*JVMTI)).GetCapabilities.unwrap()(JVMTI, &mut capabilities);
        if error != jvmtiError_JVMTI_ERROR_NONE {
            println!("Something has gone horribly wrong. @ GetCapabilities, error: {}", error);
            return -1;
        }

        capabilities.set_can_get_bytecodes(1);
        capabilities.set_can_redefine_classes(1);
        capabilities.set_can_redefine_any_class(1);
        capabilities.set_can_retransform_classes(1);
        capabilities.set_can_retransform_any_class(1);
        capabilities.set_can_generate_all_class_hook_events(1);

        error = (*(*JVMTI)).AddCapabilities.unwrap()(JVMTI, &mut capabilities);
        if error != jvmtiError_JVMTI_ERROR_NONE {
            println!("Something has gone horribly wrong. @ AddCapabilities, error: {}", error);
            return -1;
        }

        let callbacks: jvmtiEventCallbacks = jvmtiEventCallbacks {
            VMInit: None,
            VMDeath: None,
            ThreadStart: None,
            ThreadEnd: None,
            ClassFileLoadHook: Option::Some(loadHook),
            ClassLoad: None,
            ClassPrepare: None,
            VMStart: None,
            Exception: None,
            ExceptionCatch: None,
            SingleStep: None,
            FramePop: None,
            Breakpoint: None,
            FieldAccess: None,
            FieldModification: None,
            MethodEntry: None,
            MethodExit: None,
            NativeMethodBind: None,
            CompiledMethodLoad: None,
            CompiledMethodUnload: None,
            DynamicCodeGenerated: None,
            DataDumpRequest: None,
            reserved72: None,
            MonitorWait: None,
            MonitorWaited: None,
            MonitorContendedEnter: None,
            MonitorContendedEntered: None,
            reserved77: None,
            reserved78: None,
            reserved79: None,
            ResourceExhausted: None,
            GarbageCollectionStart: None,
            GarbageCollectionFinish: None,
            ObjectFree: None,
            VMObjectAlloc: None,
            reserved85: None,
            SampledObjectAlloc: None,
        };

        error = (*(*JVMTI)).SetEventCallbacks.unwrap()(JVMTI, &callbacks, size_of::<jvmtiEventCallbacks>() as jint);
        if error != jvmtiError_JVMTI_ERROR_NONE {
            println!("Something has gone horribly wrong. @ SetEventCallbacks, error: {}", error);
            return -1;
        }

        error = (*(*JVMTI)).SetEventNotificationMode.unwrap()(JVMTI, jvmtiEventMode_JVMTI_ENABLE, jvmtiEvent_JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, null_mut());
        if error != jvmtiError_JVMTI_ERROR_NONE {
            println!("Something has gone horribly wrong. @ SetEventNotificationMode, error: {}", error);
            return -1;
        }
    }

    JNI_VERSION_1_2 as i32
}

unsafe extern "C" fn loadHook(
    jvmti_env: *mut jvmtiEnv,
    jni_env: *mut JNIEnv,
    class_being_redefined: jclass,
    loader: jobject,
    name: *const c_char,
    protection_domain: jobject,
    class_data_len: jint,
    class_data: *const c_uchar,
    new_class_data_len: *mut jint,
    new_class_data: *mut *mut c_uchar,
) {
    if name != null_mut() {
        if TRANSFORM_METHOD != null_mut() {
            let jname = (*(*jni_env)).NewStringUTF.unwrap()(jni_env, name);

            let buffer_object = (*(*jni_env)).NewByteArray.unwrap()(jni_env, class_data_len);
            let buffer = class_data as *const jbyte;
            (*(*jni_env)).SetByteArrayRegion.unwrap()(jni_env, buffer_object, 0, class_data_len, buffer);

            let transformed_buffer = (*(*jni_env)).CallStaticObjectMethod.unwrap()(
                jni_env,
                SERVICE_CLASS,
                TRANSFORM_METHOD,

                class_being_redefined,
                loader,
                jname,
                protection_domain,
                buffer_object,
            ) as jobjectArray;

            if transformed_buffer != null_mut() {
                let transformed_buffer_size: jsize = (*(*jni_env)).GetArrayLength.unwrap()(jni_env, transformed_buffer);
                let mut result_buffer: *mut c_uchar = null_mut();

                (*(*jvmti_env)).Allocate.unwrap()(jvmti_env, transformed_buffer_size as jlong, &mut result_buffer);
                (*(*jni_env)).GetByteArrayRegion.unwrap()(jni_env, transformed_buffer, 0, transformed_buffer_size, result_buffer as *mut jbyte);

                *new_class_data_len = transformed_buffer_size;
                *new_class_data = result_buffer;
            }
        }
    }
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeTransformationService_getClass0(
    env: *mut JNIEnv,
    _class: jclass,
    class_name: jstring,
) -> jclass {
    let c_str = (*(*env)).GetStringUTFChars.unwrap()(env, class_name, &mut 0);

    (*(*env)).FindClass.unwrap()(env, c_str)
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeTransformationService_getLoadedClasses0(
    env: *mut JNIEnv,
    _class: jclass,
) -> jobjectArray {
    let mut class_count: jint = 0;
    let mut classes_ptr: *mut jclass = null_mut();
    (*(*JVMTI)).GetLoadedClasses.unwrap()(JVMTI, &mut class_count, &mut classes_ptr);

    let class_name = CString::new("java/lang/Class").unwrap();
    let class_array_class = (*(*env)).FindClass.unwrap()(env, class_name.as_ptr());

    create_object_array(env, class_count, classes_ptr, class_array_class)
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeTransformationService_isModifiable0(
    _env: *mut JNIEnv,
    _class: jclass,
    target_class: jclass,
) -> jboolean {
    let mut return_value: jboolean = 0;
    (*(*JVMTI)).IsModifiableClass.unwrap()(JVMTI, target_class, &mut return_value);
    return_value
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeTransformationService_requestRetransform0(
    _env: *mut JNIEnv,
    _class: jclass,
    target_class: jclass,
) {
    (*(*JVMTI)).RetransformClasses.unwrap()(JVMTI, 1, &target_class);
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeTransformationService_registerNatives0(
    env: *mut JNIEnv,
    class: jclass,
) {
    SERVICE_CLASS = class;

    let method_name = CString::new("transformAll").unwrap();
    let method_desc = CString::new("(Ljava/lang/Class;Ljava/lang/ClassLoader;Ljava/lang/String;Ljava/security/ProtectionDomain;[B)[B").unwrap();

    TRANSFORM_METHOD = (*(*env)).GetStaticMethodID.unwrap()(env, class, method_name.as_ptr(), method_desc.as_ptr());
}

unsafe fn create_object_array(
    env: *mut JNIEnv,
    class_count: jint,
    classes: *mut jobject,
    class_array: jclass
) -> jobjectArray {
    let arr = std::slice::from_raw_parts(classes, class_count as usize);

    let jarray = (*(*env)).NewObjectArray.unwrap()(env, class_count, class_array, null_mut());

    for class_index in 0..class_count {
        (*(*env)).SetObjectArrayElement.unwrap()(env, jarray, class_index as jint, arr[class_index as usize])
    }

    jarray
}