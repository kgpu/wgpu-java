package com.noahcharlton.wgpuj.core.util;

import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.jni.WgpuProgrammableStageDescriptor;

import java.nio.charset.StandardCharsets;

/**
 * A wrapper around Shader Module
 */
public class ShaderModule {

    private final String entryPoint;
    private final long module;

    public ShaderModule(long module, String entryPoint) {
        this.entryPoint = entryPoint;
        this.module = module;
    }

    public WgpuProgrammableStageDescriptor toDescriptor(){
        var desc = WgpuProgrammableStageDescriptor.createDirect();

        desc.setModule(module);
        desc.setEntryPoint(entryPoint);

        return desc;
    }

    public long getModule() {
        return module;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
