package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuDeviceDescriptor extends WgpuJavaStruct {

    private final WgpuExtensions extensions;
    private final WgpuLimits limits;

    public WgpuDeviceDescriptor(boolean anisotropicFiltering, int maxBindGroups) {
        useDirectMemory();

        this.extensions = inner(new WgpuExtensions(anisotropicFiltering));
        this.limits = inner(new WgpuLimits(maxBindGroups));
    }

    static class WgpuExtensions extends WgpuJavaStruct{
        private Struct.Boolean anisotropicFiltering = new Struct.Boolean();

        protected WgpuExtensions(boolean anisotropicFiltering) {
            useDirectMemory();

            this.anisotropicFiltering.set(anisotropicFiltering);
        }
    }

    static class WgpuLimits extends WgpuJavaStruct {
        private Struct.Unsigned32 maxBindGroups = new Struct.Unsigned32();

        protected WgpuLimits(int maxBindGroups) {
            useDirectMemory();

            this.maxBindGroups.set(maxBindGroups);
        }
    }
}
