#![allow(non_snake_case)]

extern crate jvm_rs;

use std::{
    ffi::{
        c_void,
        CString
    },
    os::raw::{
        c_int,
        c_char,
        c_uchar
    },
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
        jsize,
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
            println!("[libdeface] Something has gone horribly wrong. @ GetVersionNumber, ver/error: {}/{}", ver, error);
            return -1;
        }

        let mut capabilities = jvmtiCapabilities {
            _bitfield_1: Default::default()
        };
        error = (*(*JVMTI)).GetCapabilities.unwrap()(JVMTI, &mut capabilities);
        if error != jvmtiError_JVMTI_ERROR_NONE {
            println!("[libdeface] Something has gone horribly wrong. @ GetCapabilities, error: {}", error);
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
            println!("[libdeface] Something has gone horribly wrong. @ AddCapabilities, error: {}", error);
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
            println!("[libdeface] Something has gone horribly wrong. @ SetEventCallbacks, error: {}", error);
            return -1;
        }

        error = (*(*JVMTI)).SetEventNotificationMode.unwrap()(JVMTI, jvmtiEventMode_JVMTI_ENABLE, jvmtiEvent_JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, null_mut());
        if error != jvmtiError_JVMTI_ERROR_NONE {
            println!("[libdeface] Something has gone horribly wrong. @ SetEventNotificationMode, error: {}", error);
            return -1;
        }
    }

    JNI_VERSION_1_2 as i32
}
