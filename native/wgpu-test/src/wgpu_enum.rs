/// Used to generate functions to test enums between
/// wgpu-java/native and the corresponding rust wgpu type.
///
/// ```
/// enum_name_test!(method_name, type_path)
/// ```
/// For instance the line `enum_name_test!(get_power_preference_name, wgt::PowerPreference)`
/// produces the following function:
/// ```
///#[no_mangle]
///pub extern fn get_power_preference_name(x : wgt::PowerPreference) -> *const std::os::raw::c_char{
///    std::ffi::CString::new(format!("{:?}", x)).unwrap().into_raw()
///}
/// ```
macro_rules! enum_name_test {
    ($func_name:ident, $enum_type:ty) => {
#[no_mangle]
pub extern fn $func_name(x : $enum_type) -> *const std::os::raw::c_char{
    std::ffi::CString::new(format!("{:?}", x)).unwrap().into_raw()
}
    };
}

enum_name_test!(get_power_preference_name, wgt::PowerPreference);
enum_name_test!(get_primitive_topology_name, wgt::PrimitiveTopology);
enum_name_test!(get_present_mode_name, wgt::PresentMode);
enum_name_test!(get_store_op_name, wgt::StoreOp);
enum_name_test!(get_load_op_name, wgt::LoadOp);