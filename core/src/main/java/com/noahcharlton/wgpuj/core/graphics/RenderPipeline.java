package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuPipelineLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRenderPipelineDescriptor;

public class RenderPipeline {

    private final long bindGroupID;
    private final long pipelineID;

    public RenderPipeline(RenderPipelineSettings settings, long device){
        var layoutDescriptor = new WgpuBindGroupLayoutDescriptor("bind group layout");
        long bindGroupLayout = WgpuJava.wgpuNative.wgpu_device_create_bind_group_layout(device,
                layoutDescriptor.getPointerTo());
        var groupDescriptor = new WgpuBindGroupDescriptor("bind group", bindGroupLayout);
        bindGroupID = WgpuJava.wgpuNative.wgpu_device_create_bind_group(device, groupDescriptor.getPointerTo());

        var pipelineLayoutDescriptor = new WgpuPipelineLayoutDescriptor(bindGroupLayout);
        long pipelineLayoutID = WgpuJava.wgpuNative.wgpu_device_create_pipeline_layout(device,
                pipelineLayoutDescriptor.getPointerTo());

        WgpuRenderPipelineDescriptor pipelineDesc = settings.build(device, pipelineLayoutID);
        pipelineID = WgpuJava.wgpuNative.wgpu_device_create_render_pipeline(device, pipelineDesc.getPointerTo());
    }

    public long getBindGroupID() {
        return bindGroupID;
    }

    public long getPipelineID() {
        return pipelineID;
    }
}
