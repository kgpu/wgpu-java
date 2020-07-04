package com.noahcharlton.wgpuj.core.compute;

import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.jni.WgpuComputePipelineDescriptor;

public class ComputePipeline {

    public static WgpuComputePipelineDescriptor of(Device device, long layout, ShaderData shader){
        var descriptor = WgpuComputePipelineDescriptor.createDirect();
        descriptor.setLayout(layout);
        descriptor.getComputeStage().setEntryPoint(shader.getEntryPoint());
        descriptor.getComputeStage().setModule(shader.createModule(device));

        return descriptor;
    }
}
