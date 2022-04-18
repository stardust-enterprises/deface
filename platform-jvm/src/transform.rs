use std::{
    ffi::CString,
    os::raw::{c_char, c_uchar},
    ptr::{null_mut}
};
use jvm_rs::{
    jni::{jbyte, jclass, jint, jlong, JNIEnv, jobject, jobjectArray, jmethodID, jsize},
    jvmti::{jvmtiEnv, jvmtiError_JVMTI_ERROR_NONE}
};

static mut SERVICE_CLASS: jclass = null_mut();
static mut TRANSFORM_METHOD: jmethodID = null_mut();

#[no_mangle]
pub unsafe extern "C" fn load_hook(
    jvmti: *mut jvmtiEnv,
    jni: *mut JNIEnv,
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
            let jname = (*(*jni)).NewStringUTF.unwrap()(jni, name);

            let buffer_object = (*(*jni)).NewByteArray.unwrap()(jni, class_data_len);
            let buffer = class_data as *const jbyte;
            (*(*jni)).SetByteArrayRegion.unwrap()(jni, buffer_object, 0, class_data_len, buffer);

            let transformed_buffer = (*(*jni)).CallStaticObjectMethod.unwrap()(
                jni,
                SERVICE_CLASS,
                TRANSFORM_METHOD,
                class_being_redefined,
                loader,
                jname,
                protection_domain,
                buffer_object,
            ) as jobjectArray;

            if transformed_buffer != null_mut() {
                let transformed_buffer_size: jsize = (*(*jni)).GetArrayLength.unwrap()(jni, transformed_buffer);
                let mut result_buffer: *mut c_uchar = null_mut();

                let error = (*(*jvmti)).Allocate.unwrap()(jvmti, transformed_buffer_size as jlong, &mut result_buffer);
                if error != jvmtiError_JVMTI_ERROR_NONE {
                    println!("[libdeface] Something has gone horribly wrong. @ Allocate, error: {}", error);
                    return;
                }

                (*(*jni)).GetByteArrayRegion.unwrap()(jni, transformed_buffer, 0, transformed_buffer_size, result_buffer as *mut jbyte);

                *new_class_data_len = transformed_buffer_size;
                *new_class_data = result_buffer;
            }
        }
    }
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeTransformationService_retransformClass0(
    _env: *mut JNIEnv,
    _class: jclass,
    target_class: jclass,
) {
    let jvmti = crate::agent::JVMTI;

    let error = (*(*jvmti)).RetransformClasses.unwrap()(jvmti, 1, &target_class);
    if error != jvmtiError_JVMTI_ERROR_NONE {
        println!("[libdeface] Something has gone horribly wrong. @ RetransformClasses, error: {}", error);
    }
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
