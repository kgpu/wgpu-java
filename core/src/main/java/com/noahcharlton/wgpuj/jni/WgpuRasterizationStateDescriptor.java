package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuRasterizationStateDescriptor extends WgpuJavaStruct {

    private final Struct.Enum<WgpuFrontFace> frontFace = new Struct.Enum<>(WgpuFrontFace.class);
    private final Struct.Enum<WgpuCullMode> cullMode = new Struct.Enum<>(WgpuCullMode.class);
    private final Struct.Unsigned32 depthBias = new Struct.Unsigned32();
    private final Struct.Float depthBiasSlopeScale = new Struct.Float();
    private final Struct.Float depthBiasClamp = new Struct.Float();

    public WgpuRasterizationStateDescriptor(WgpuFrontFace frontFace, WgpuCullMode cullMode, int depthBias,
                                            float depthBiasSlopeScale, float depthBiasClamp) {
        useDirectMemory();

        this.frontFace.set(frontFace);
        this.cullMode.set(cullMode);
        this.depthBias.set(depthBias);
        this.depthBiasSlopeScale.set(depthBiasSlopeScale);
        this.depthBiasClamp.set(depthBiasClamp);
    }
}
