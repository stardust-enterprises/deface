extern crate jni;

use std::ffi::{c_void, CStr};
use std::os::raw::c_int;
use std::ptr::null_mut;
use jni::objects::{JClass, JObject, JString};
use jni::{JNIEnv};
use jni::sys::{JavaVM, JNI_VERSION_1_4};

static mut JVM: *mut JavaVM = null_mut();

#[no_mangle]
pub unsafe extern "system" fn JNI_OnLoad(_vm: *mut JavaVM, _reserved: &mut c_void) -> c_int {
    JVM = _vm;
    let v = (*JVM).GetEnv.unwrap().call()
    if (*JVM).GetEnv.() {

    }
    /**
    if (m_jvm->GetEnv((void**)&m_jvmti, JVMTI_VERSION_1_0) == JNI_OK){
        jvmtiCapabilities capa;
        m_jvmti->GetCapabilities(&capa);
        //capa.can_get_bytecodes = 1; // idk if its necessary
        capa.can_redefine_classes = 1;
        capa.can_redefine_any_class = 1;
        capa.can_retransform_classes = 1;
        capa.can_retransform_any_class = 1;
        capa.can_generate_all_class_hook_events = 1;
        m_jvmti->AddCapabilities(&capa);
        jvmtiEventCallbacks callbacks = {nullptr};
        callbacks.ClassFileLoadHook = ClassFileLoadHook;
        m_jvmti->SetEventCallbacks(&callbacks, sizeof(callbacks));
        m_jvmti->SetEventNotificationMode(JVMTI_ENABLE, JVMTI_EVENT_CLASS_FILE_LOAD_HOOK, (jthread) nullptr);
    }
    */

    JNI_VERSION_1_4 as i32
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeTransformationService_getClass0<'a>(
    env: *mut JNIEnv<'a>,
    _this: JClass,
    class_name: JString,
) -> JClass<'a> {
    let v = (*env).get_string_utf_chars(class_name).unwrap();
    let d = CStr::from_ptr(v).to_str().unwrap();
    (*env).find_class(&d).unwrap()
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeTransformationService_getLoadedClasses0<'a>(
    env: *mut JNIEnv<'a>,
    _this: JClass,
) -> JObject<'a> {

}
