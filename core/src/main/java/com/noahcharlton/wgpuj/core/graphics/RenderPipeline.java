package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.jni.WgpuRenderPipelineDescriptor;

public class RenderPipeline {

    private final Device device;
    private final Color clearColor;
    private final long pipelineID;

    public RenderPipeline(RenderPipelineSettings settings, Device device){
        this.device = device;
        this.clearColor = settings.getClearColor();

        long pipelineLayoutID = device.createPipelineLayout(settings.getBindGroupLayouts());

        WgpuRenderPipelineDescriptor pipelineDesc = settings.build(device, pipelineLayoutID);
        pipelineID = WgpuJava.wgpuNative.wgpu_device_create_render_pipeline(device.getId(),
                pipelineDesc.getPointerTo());
    }

    public Color getClearColor() {
        return clearColor;
    }

    public long getPipelineID() {
        return pipelineID;
    }
}
