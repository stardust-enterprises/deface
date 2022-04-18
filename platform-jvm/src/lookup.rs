use std::{
    ffi::CString,
    ptr::null_mut,
};
use jvm_rs::{
    jni::{jboolean, jclass, jint, JNIEnv, jobject, jobjectArray},
    jvmti::jvmtiError_JVMTI_ERROR_NONE,
};

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeLookupService_getClassLoaderClasses0(
    jni: *mut JNIEnv,
    _class: jclass,
    class_loader: jobject,
) -> jobjectArray {
    let jvmti = crate::agent::JVMTI;

    let mut class_count: jint = 0;
    let mut classes_ptr: *mut jclass = null_mut();
    let error = (*(*jvmti)).GetClassLoaderClasses.unwrap()(jvmti, class_loader, &mut class_count, &mut classes_ptr);
    if error != jvmtiError_JVMTI_ERROR_NONE {
        println!("[libdeface] Something has gone horribly wrong. @ GetClassLoaderClasses, error: {}", error);
        return null_mut();
    }

    let class_name = CString::new("java/lang/Class").unwrap();
    let class_array_class = (*(*jni)).FindClass.unwrap()(jni, class_name.as_ptr());

    create_object_array(jni, class_count, classes_ptr, class_array_class)
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeLookupService_getLoadedClasses0(
    jni: *mut JNIEnv,
    _class: jclass,
) -> jobjectArray {
    let jvmti = crate::agent::JVMTI;

    let mut class_count: jint = 0;
    let mut classes_ptr: *mut jclass = null_mut();
    let error = (*(*jvmti)).GetLoadedClasses.unwrap()(jvmti, &mut class_count, &mut classes_ptr);
    if error != jvmtiError_JVMTI_ERROR_NONE {
        println!("[libdeface] Something has gone horribly wrong. @ GetLoadedClasses, error: {}", error);
        return null_mut();
    }

    let class_name = CString::new("java/lang/Class").unwrap();
    let class_array_class = (*(*jni)).FindClass.unwrap()(jni, class_name.as_ptr());

    create_object_array(jni, class_count, classes_ptr, class_array_class)
}

#[no_mangle]
pub unsafe extern "system" fn Java_fr_stardustenterprises_deface_engine_NativeLookupService_isModifiable0(
    _jni: *mut JNIEnv,
    _class: jclass,
    target_class: jclass,
) -> jboolean {
    let jvmti = crate::agent::JVMTI;

    let mut return_value: jboolean = 0;
    let error = (*(*jvmti)).IsModifiableClass.unwrap()(jvmti, target_class, &mut return_value);
    if error != jvmtiError_JVMTI_ERROR_NONE {
        println!("[libdeface] Something has gone horribly wrong. @ IsModifiableClass, error: {}", error);
        return 0;
    }
    return_value
}

unsafe fn create_object_array(
    jni: *mut JNIEnv,
    class_count: jint,
    classes: *mut jobject,
    class_array: jclass,
) -> jobjectArray {
    let arr = std::slice::from_raw_parts(classes, class_count as usize);
    let jarray = (*(*jni)).NewObjectArray.unwrap()(jni, class_count, class_array, null_mut());

    for class_index in 0..class_count {
        (*(*jni)).SetObjectArrayElement.unwrap()(jni, jarray, class_index as jint, arr[class_index as usize])
    }

    jarray
}
