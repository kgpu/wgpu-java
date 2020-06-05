package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuDeviceDescriptor extends WgpuJavaStruct {

    private final Struct.Boolean anisotropicFiltering = new Struct.Boolean();
    private final Struct.Unsigned32 maxBindGroups = new Struct.Unsigned32();

    public WgpuDeviceDescriptor(boolean anisotropicFiltering, int maxBindGroups) {
        useDirectMemory();

        this.anisotropicFiltering.set(anisotropicFiltering);
        this.maxBindGroups.set(maxBindGroups);
    }
}