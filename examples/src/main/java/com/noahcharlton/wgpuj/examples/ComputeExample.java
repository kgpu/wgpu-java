package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.DeviceSettings;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.compute.ComputePass;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.BufferSettings;
import com.noahcharlton.wgpuj.core.util.BufferUsage;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindingType;
import com.noahcharlton.wgpuj.jni.WgpuComputePipelineDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import jnr.ffi.Pointer;

import java.util.Arrays;

public class ComputeExample {

    private static final int[] numbers = new int[]{2, 7, 19, 20};

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();
        WgpuJava.setWgpuLogLevel(WgpuLogLevel.WARN);

        System.out.println("Calculating Collatz for: " + Arrays.toString(numbers));
        int bufferSize = numbers.length * Integer.BYTES;

        Device device = Device.create(new DeviceSettings(), 0);

        Buffer stagingBuffer = new BufferSettings()
                .setLabel("Staging Buffer")
                .setSize(bufferSize)
                .setUsages(BufferUsage.MAP_READ, BufferUsage.COPY_DST)
                .setMapped(false)
                .createBuffer(device);

        Buffer storageBuffer = device.createIntBuffer("storage buffer", numbers, BufferUsage.STORAGE,
                BufferUsage.COPY_DST, BufferUsage.COPY_SRC, BufferUsage.UNIFORM);

        long bindGroupLayout = device.createBindGroupLayout("bind group layout",
                new WgpuBindGroupLayoutEntry().setPartial(0, WgpuBindGroupLayoutEntry.SHADER_STAGE_COMPUTE,
                        WgpuBindingType.STORAGE_BUFFER));
        long bindGroup = device.createBindGroup("bind group", bindGroupLayout,
                new WgpuBindGroupEntry().setBuffer(0, storageBuffer.getId(), bufferSize));

        long pipelineLayout = device.createPipelineLayout(bindGroupLayout);
        long shader = ShaderData.fromRawClasspathFile("/collatz.comp", "main").createModule(device);

        Pointer pipelineDesc = new WgpuComputePipelineDescriptor(pipelineLayout, shader, "main").getPointerTo();
        long pipelineId = WgpuJava.wgpuNative.wgpu_device_create_compute_pipeline(device.getId(), pipelineDesc);

        long encoder = device.createCommandEncoder("command encoder");

        ComputePass pass = ComputePass.create(encoder);

        pass.setPipeline(pipelineId);
        pass.setBindGroup(0, bindGroup, WgpuJava.createNullPointer(), 0);
        pass.dispatch(numbers.length, 1, 1);
        pass.endPass();

        long queue = device.getDefaultQueue();
        long commandBuffer = WgpuJava.wgpuNative.wgpu_command_encoder_finish(encoder, WgpuJava.createNullPointer());

        storageBuffer.copyTo(stagingBuffer, encoder);

        WgpuJava.wgpuNative.wgpu_queue_submit(queue, WgpuJava.createDirectLongPointer(commandBuffer), 1);

        stagingBuffer.readAsync();

        device.poll(true);
        Pointer data = stagingBuffer.getMappedData();

        System.out.println("Times:");

        for(int i = 0; i < numbers.length; i++){
            System.out.println(numbers[i] + ": " + data.getInt(i * Integer.BYTES) + " steps");
        }

        stagingBuffer.unmap();
    }
}
