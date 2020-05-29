package com.noahcharlton.wgpuj.jni;

import jnr.ffi.Pointer;
import jnr.ffi.types.u_int32_t;

public interface WgpuJNI {

    @u_int32_t
    int wgpu_get_version();

    Pointer wgpu_create_surface_from_windows_hwnd(Pointer hInstance, Pointer hwnd);

    int wgpu_set_log_level(WgpuLogLevel level);

    void wgpu_set_log_callback(LogCallback callback);
}
