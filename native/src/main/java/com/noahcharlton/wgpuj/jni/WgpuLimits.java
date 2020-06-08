package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuLimits extends WgpuJavaStruct {

    private final Struct.Unsigned32 maxBindGroups = new Struct.Unsigned32();

    public WgpuLimits(int maxBindGroups) {
        useDirectMemory();

        this.maxBindGroups.set(maxBindGroups);
    }
}
