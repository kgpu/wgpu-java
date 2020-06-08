package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.fail.RustFailCallback;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuLoadOp;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuStoreOp;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainStatus;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import jnr.ffi.Pointer;

public interface WgpuTest {

    boolean rust_returns_true();

    boolean rust_returns_false();

    Pointer rust_returns_foobar_string();

    void java_gives_foobar_string(Pointer value);

    void rust_fails_test();

    void set_fail_callback(RustFailCallback callback);

    Pointer color_to_string(Pointer color);

    void bind_group_layout_descriptor_test(Pointer bindGroupLayoutDescriptor);

    Pointer get_power_preference_name(WgpuPowerPreference preference);

    Pointer get_primitive_topology_name(WgpuPrimitiveTopology topology);

    Pointer get_present_mode_name(WgpuPresentMode mode);

    Pointer get_load_op_name(WgpuLoadOp op);

    Pointer get_store_op_name(WgpuStoreOp op);

    Pointer get_blend_factor_name(WgpuBlendFactor factor);

    Pointer get_blend_operation_name(WgpuBlendOperation operation);

    Pointer get_cull_mode_name(WgpuCullMode mode);

    Pointer get_index_format_name(WgpuIndexFormat format);

    Pointer get_front_face_name(WgpuFrontFace face);

    Pointer get_log_level_name(WgpuLogLevel level);

    Pointer get_swap_chain_status_name(WgpuSwapChainStatus status);

    Pointer get_texture_format_name(WgpuTextureFormat name);
}
