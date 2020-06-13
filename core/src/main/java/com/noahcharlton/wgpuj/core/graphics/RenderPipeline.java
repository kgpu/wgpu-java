package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.jni.WgpuPipelineLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRenderPipelineDescriptor;

public class RenderPipeline {

    private final Color clearColor;
    private final long pipelineID;

    public RenderPipeline(RenderPipelineSettings settings, long device){
        var pipelineLayoutDescriptor = new WgpuPipelineLayoutDescriptor(settings.getBindGroupLayouts());
        long pipelineLayoutID = WgpuJava.wgpuNative.wgpu_device_create_pipeline_layout(device,
                pipelineLayoutDescriptor.getPointerTo());

        WgpuRenderPipelineDescriptor pipelineDesc = settings.build(device, pipelineLayoutID);
        pipelineID = WgpuJava.wgpuNative.wgpu_device_create_render_pipeline(device, pipelineDesc.getPointerTo());

        clearColor = settings.getClearColor();
    }

    public Color getClearColor() {
        return clearColor;
    }

    public long getPipelineID() {
        return pipelineID;
    }
}
