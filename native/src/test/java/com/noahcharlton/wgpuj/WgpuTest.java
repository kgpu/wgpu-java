package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.fail.RustFailCallback;
import com.noahcharlton.wgpuj.jni.WgpuColor;
import jnr.ffi.Pointer;

public interface WgpuTest {

    boolean rust_returns_true();

    boolean rust_returns_false();

    Pointer rust_returns_foobar_string();

    void java_gives_foobar_string(Pointer value);

    void rust_fails_test();

    void set_fail_callback(RustFailCallback callback);

    Pointer color_to_string(WgpuColor color);

    void device_descriptor_test(Pointer deviceDescriptor);

    void bind_group_layout_descriptor_test(Pointer bindGroupLayoutDescriptor);
}
