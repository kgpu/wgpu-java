package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuRequestAdapterOptions extends WgpuJavaStruct {

    private final Struct.Enum<WGPUPowerPreference> power_preference = new Struct.Enum<>(WGPUPowerPreference.class);
    private final UnsignedLong compatible_surface = new UnsignedLong();

    public WgpuRequestAdapterOptions(WGPUPowerPreference preference, long surface) {
        useDirectMemory();

        power_preference.set(preference);
        compatible_surface.set(surface);
    }

}
