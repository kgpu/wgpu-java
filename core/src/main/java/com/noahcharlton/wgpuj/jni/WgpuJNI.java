package com.noahcharlton.wgpuj.jni;

import jnr.ffi.Pointer;
import jnr.ffi.types.u_int32_t;
import jnr.ffi.types.u_int64_t;

public interface WgpuJNI {

    @u_int32_t
    int wgpu_get_version();

    long wgpu_create_surface_from_windows_hwnd(Pointer hInstance, Pointer hwnd);

    int wgpu_set_log_level(WgpuLogLevel level);

    void wgpu_set_log_callback(LogCallback callback);

    void wgpu_request_adapter_async(Pointer options,
                                    @u_int32_t int backendMask,
                                    RequestAdapterCallback callback,
                                    Pointer userdata);

    @u_int64_t
    long wgpu_adapter_request_device(@u_int64_t long buffer, Pointer description, String tracePath);

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
}
