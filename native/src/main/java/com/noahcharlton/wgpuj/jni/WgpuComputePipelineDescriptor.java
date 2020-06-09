package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuComputePipelineDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 layout = new Struct.Unsigned64();
    private final WgpuProgrammableStageDescriptor stage = inner(new WgpuProgrammableStageDescriptor());

    public WgpuComputePipelineDescriptor(long layout, long shaderModule, java.lang.String entryPoint) {
        useDirectMemory();

        this.layout.set(layout);
        this.stage.set(shaderModule, entryPoint);
    }
}
