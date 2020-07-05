package com.noahcharlton.wgpuj.core.compute;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.jni.WgpuComputePipelineDescriptor;

public class ComputePipeline {

    public static long create(Device device, long layout, ShaderData shader){
        var descriptor = WgpuComputePipelineDescriptor.createDirect();
        descriptor.setLayout(layout);
        descriptor.getComputeStage().setEntryPoint(shader.getEntryPoint());
        descriptor.getComputeStage().setModule(shader.createModule(device));

        return WgpuJava.wgpuNative.wgpu_device_create_compute_pipeline(device.getId(), descriptor.getPointerTo());
    }
}
