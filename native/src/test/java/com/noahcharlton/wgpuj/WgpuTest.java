package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.fail.RustFailCallback;
import com.noahcharlton.wgpuj.jni.WgpuColor;
import com.noahcharlton.wgpuj.jni.WgpuLoadOp;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuStoreOp;
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

    Pointer get_power_preference_name(WgpuPowerPreference preference);

    Pointer get_primitive_topology_name(WgpuPrimitiveTopology topology);

    Pointer get_present_mode_name(WgpuPresentMode mode);

    Pointer get_load_op_name(WgpuLoadOp op);

    Pointer get_store_op_name(WgpuStoreOp op);
}
