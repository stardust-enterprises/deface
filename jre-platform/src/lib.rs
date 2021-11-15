extern crate jni;

use std::ffi::c_void;
use std::os::raw::c_int;
use jni::objects::{JClass, JObject, JString};
use jni::{JNIEnv};
use jni::sys::{JavaVM, JNI_VERSION_1_6};

#[no_mangle]
pub unsafe extern "system" fn JNI_OnLoad(_vm: *mut JavaVM, _reserved: &mut c_void) -> c_int {
    JNI_VERSION_1_6 as i32
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_patcher_Patcher_getClass0<'a>(
    env: *mut JNIEnv,
    _this: JObject,
    class_name: JString
) -> JClass<'a> {
    let v = (*env).get_string_utf_chars(class_name).unwrap();
    (*env).find_class(&v).unwrap()
}