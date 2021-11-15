extern crate jni;

use jni::objects::{JClass, JObject, JString};
use jni::JNIEnv;

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_patcher_Patcher_getClass0(
    env: *mut JNIEnv,
    _this: JObject,
    class_name: JString
) -> JClass {
    return env.find_class(class_name).unwrap();
}