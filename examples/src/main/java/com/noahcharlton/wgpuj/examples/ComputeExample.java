package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.*;
import com.noahcharlton.wgpuj.core.compute.ComputePass;
import com.noahcharlton.wgpuj.core.compute.ComputePipeline;
import com.noahcharlton.wgpuj.core.util.*;
import com.noahcharlton.wgpuj.jni.*;
import jnr.ffi.Pointer;

import java.util.Arrays;

public class ComputeExample {

    private static final int[] numbers = new int[]{2, 7, 19, 20};

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();
        WgpuJava.setWgpuLogLevel(WgpuLogLevel.WARN);

        System.out.println("Calculating Collatz for: " + Arrays.toString(numbers));
        int bufferSize = numbers.length * Integer.BYTES;

        Device device = Device.create(new DeviceConfig(), 0);

        Buffer stagingBuffer = new BufferConfig()
                .setLabel("Staging Buffer")
                .setSize(bufferSize)
                .setUsages(BufferUsage.MAP_READ, BufferUsage.COPY_DST)
                .setMapped(false)
                .createBuffer(device);

        Buffer storageBuffer = device.createIntBuffer("storage buffer", numbers, BufferUsage.STORAGE,
                BufferUsage.COPY_DST, BufferUsage.COPY_SRC, BufferUsage.UNIFORM);

        long bindGroupLayout = device.createBindGroupLayout("bind group layout",
                BindGroupUtils.partialLayout(0, Wgpu.ShaderStage.COMPUTE, WgpuBindingType.STORAGE_BUFFER));
        long bindGroup = device.createBindGroup("bind group", bindGroupLayout,
                BindGroupUtils.bufferEntry(0, storageBuffer));

        long pipelineLayout = device.createPipelineLayout(bindGroupLayout);
        ShaderConfig shader = ShaderConfig.fromRawClasspathFile("/collatz.comp", "main");
        long pipelineId = ComputePipeline.create(device, pipelineLayout, shader);

        CommandEncoder encoder = device.createCommandEncoder("command encoder");
        ComputePass pass = encoder.beginComputePass(WgpuComputePassDescriptor.createDirect());

        pass.setPipeline(pipelineId);
        pass.setBindGroup(0, bindGroup, WgpuJava.createNullPointer(), 0);
        pass.dispatch(numbers.length, 1, 1);
        pass.endPass();

        Queue queue = device.getDefaultQueue();
        encoder.copyBufferToBuffer(storageBuffer, stagingBuffer);
        long commandBuffer = encoder.finish(WgpuCommandBufferDescriptor.createDirect());
        queue.submit(commandBuffer);

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
