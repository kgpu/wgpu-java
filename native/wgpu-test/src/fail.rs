use std::ffi::CString;
use std::os::raw::c_char;

macro_rules! assert_ffi {
    ($left:expr, $right:expr) => ({
        match (&$left, &$right) {
            (left_val, right_val) => {
                if !(*left_val == *right_val) {
                    $crate::fail::fail_test(format!(r#"assertion failed: `(left == right)`
  left: `{:?}`,
 right: `{:?}`"#, &*left_val, &*right_val))
                }
            }
        }
    });
    ($left:expr, $right:expr,) => ({
        $crate::assert_ffi!($left, $right)
    });
    ($left:expr, $right:expr, $($arg:tt)+) => ({
        match (&($left), &($right)) {
            (left_val, right_val) => {
                if !(*left_val == *right_val) {
                    $crate::fail::fail_test(format!(r#"assertion failed: `(left == right)`
  left: `{:?}`,
 right: `{:?}`: {}"#, &*left_val, &*right_val,
                           $crate::format_args!($($arg)+)))
                }
            }
        }
    });
}

macro_rules! fail_ffi {
    ($($arg:tt)*) => ($crate::fail::fail_test(format!($($arg)*)));
}

pub type TestFailCallback = unsafe extern "C" fn(msg: *const c_char);

static mut FAIL_CALLBACK: Option<TestFailCallback> = None;

pub fn fail_test(msg: String) {
    let msg = CString::new(msg).unwrap();

    unsafe {
        let callback = FAIL_CALLBACK.unwrap();

        callback(msg.as_ptr());
    }
}

#[no_mangle]
pub unsafe extern fn set_fail_callback(callback: TestFailCallback) {
    match &FAIL_CALLBACK {
        Some(_) => panic!("The fail callback can only be set once."),
        None => (),
    }

    FAIL_CALLBACK = Some(callback);
}
