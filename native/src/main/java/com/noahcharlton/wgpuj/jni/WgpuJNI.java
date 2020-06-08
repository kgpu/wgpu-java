package com.noahcharlton.wgpuj.jni;

import jnr.ffi.Pointer;
import jnr.ffi.types.intptr_t;
import jnr.ffi.types.u_int32_t;
import jnr.ffi.types.u_int64_t;

public interface WgpuJNI {

    @u_int32_t
    int wgpu_get_version();

    @u_int64_t
    long wgpu_create_surface_from_windows_hwnd(Pointer hInstance, Pointer hwnd);

    int wgpu_set_log_level(WgpuLogLevel level);

    void wgpu_set_log_callback(LogCallback callback);

    void wgpu_request_adapter_async(Pointer options,
                                    @u_int32_t int backendMask,
                                    boolean allowUnsafe,
                                    RequestAdapterCallback callback,
                                    Pointer userdata);

    @u_int64_t
    long wgpu_adapter_request_device(@u_int64_t long buffer,
                                     @u_int64_t long extensions,
                                     Pointer limits,
                                     String tracePath);

    @u_int64_t
    long wgpu_device_create_shader_module(@u_int64_t long device, Pointer shaderModuleDescriptor);

    @u_int64_t
    long wgpu_device_create_bind_group_layout(@u_int64_t long device, Pointer bindGroupLayoutDescriptor);

    @u_int64_t
    long wgpu_device_create_bind_group(@u_int64_t long device, Pointer wgpuBindGroupDescriptor);

    @u_int64_t
    long wgpu_device_create_pipeline_layout(@u_int64_t long device, Pointer wgpuPipelineLayoutDescriptor);


    @u_int64_t
    long wgpu_device_create_render_pipeline(@u_int64_t long device, Pointer wgpuRenderPipelineDescriptor);

    @u_int64_t
    long wgpu_device_create_swap_chain(@u_int64_t long device, @u_int64_t long surfaceID,
                                       Pointer wgpuWSwapChainDescriptor);

    void wgpu_swap_chain_get_next_texture_jnr_hack(@u_int64_t long swapChain, Pointer output);

    @u_int64_t
    long wgpu_device_create_command_encoder(@u_int64_t long device_id, Pointer commandEncoderDescription);

    WgpuRawPass wgpu_command_encoder_begin_render_pass(@u_int64_t long encoder, Pointer renderPassDescriptor);

    void wgpu_render_pass_set_pipeline(Pointer rawPass, @u_int64_t long pipelineID);

    /**
     * @param offsets Currently Not implemented
     */
    void wgpu_render_pass_set_bind_group(Pointer rawPass, @u_int32_t int index, @u_int64_t long bindGroup,
                                         Pointer offsets, @intptr_t long offsetLength);

    void wgpu_render_pass_draw(Pointer rawPass, @u_int32_t int vertexCount, @u_int32_t int instanceCount,
                               @u_int32_t int firstVertex, @u_int32_t int firstInstance);

    @u_int64_t
    long wgpu_device_get_default_queue(@u_int64_t long device);

    @u_int64_t
    long wgpu_render_pass_end_pass(Pointer rawPass);

    @u_int64_t
    long wgpu_command_encoder_finish(@u_int64_t long encoderID, Pointer commandBufferDescriptor);

    void wgpu_queue_submit(@u_int64_t long queueID, Pointer commandBuffers, @u_int32_t int buffersLength);

    void wgpu_swap_chain_present(@u_int64_t long swapChain);
}
