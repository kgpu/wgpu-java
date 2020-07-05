package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.jni.WgpuRenderPipelineDescriptor;

public class RenderPipeline {

    private final long pipelineID;

    public RenderPipeline(long pipelineID) {
        this.pipelineID = pipelineID;
    }

    public long getPipelineID() {
        return pipelineID;
    }
}
