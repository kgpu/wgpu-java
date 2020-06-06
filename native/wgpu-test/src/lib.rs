use std::ffi::{CStr, CString};
use std::os::raw::c_char;

#[macro_use]
mod fail;
mod wgpu_enum;

#[no_mangle]
pub unsafe extern fn rust_fails_test(){
    fail_ffi!("test 1234");
}

#[no_mangle]
pub extern fn rust_returns_true() -> bool {
    true
}

#[no_mangle]
pub extern fn rust_returns_false() -> bool {
    false
}

#[no_mangle]
pub unsafe extern fn rust_returns_foobar_string() -> *const c_char{
    CString::new("foobar").unwrap().into_raw()
}

#[no_mangle]
pub unsafe extern fn java_gives_foobar_string(input: *mut c_char){
    let c_str = CString::from_raw(input);

    assert_ffi!("foobar".to_string(), c_str.into_string().unwrap());
}

#[no_mangle]
pub extern fn color_to_string(color: wgt::Color) -> *const c_char {
    CString::new(format!("{:?}", color)).unwrap().into_raw()
}

#[no_mangle]
pub extern fn bind_group_layout_descriptor_test(
    desc: &wgc::binding_model::BindGroupLayoutDescriptor) {

    let label = unsafe {CStr::from_ptr(desc.label).to_str().unwrap() };

    assert_ffi!(label, "foobar9876");
}

// Currently disabled - see WgpuTypeTests.java for more detail
//
//#[no_mangle]
//pub fn device_descriptor_test(device: &wgt::DeviceDescriptor) {
//    assert_ffi!(device.extensions.anisotropic_filtering, true);
//    assert_ffi!(device.limits.max_bind_groups, 54321);
//}
