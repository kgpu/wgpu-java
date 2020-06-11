package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.fail.RustFailCallback;
import com.noahcharlton.wgpuj.jni.WgpuBindingType;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuBufferMapAsyncStatus;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuInputStepMode;
import com.noahcharlton.wgpuj.jni.WgpuLoadOp;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuStoreOp;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainStatus;
import com.noahcharlton.wgpuj.jni.WgpuTextureAspect;
import com.noahcharlton.wgpuj.jni.WgpuTextureComponentType;
import com.noahcharlton.wgpuj.jni.WgpuTextureDimension;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.jni.WgpuTextureViewDimension;
import com.noahcharlton.wgpuj.jni.WgpuVertexFormat;
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

    void bind_group_entry_test_binding(Pointer bindGroupEntry);

    void wgpu_origin_3d_test(Pointer origin3d);

    void wgpu_extent_3d_test(Pointer extent3d);

    Pointer bind_group_entry_resource_to_string(Pointer bindGroupEntry);

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

    Pointer get_wgpu_binding_type_name(WgpuBindingType type);

    Pointer get_wgpu_buffer_map_async_status_name(WgpuBufferMapAsyncStatus status);

    Pointer get_wgpu_texture_view_dimension_name(WgpuTextureViewDimension dimension);

    Pointer get_wgpu_texture_component_type_name(WgpuTextureComponentType type);

    Pointer get_wgpu_input_step_mode_name(WgpuInputStepMode mode);

    Pointer get_wgpu_vertex_format_name(WgpuVertexFormat format);

    Pointer get_wgpu_texture_dimension_name(WgpuTextureDimension dimension);

    Pointer get_wgpu_texture_aspect_name(WgpuTextureAspect aspect);
}
