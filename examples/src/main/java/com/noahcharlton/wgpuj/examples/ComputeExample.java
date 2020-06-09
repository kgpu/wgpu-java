package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindingResourceTag;
import com.noahcharlton.wgpuj.jni.WgpuBindingType;
import com.noahcharlton.wgpuj.jni.WgpuBufferDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuCommandEncoderDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuComputePipelineDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuLimits;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import com.noahcharlton.wgpuj.jni.WgpuPipelineLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRawPass;
import jnr.ffi.Pointer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class ComputeExample {

    private static final int[] numbers = new int[]{2, 7, 19, 20};

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();
        WgpuJava.setWgpuLogLevel(WgpuLogLevel.WARN);

        System.out.println("Calculating Collatz for: " + Arrays.toString(numbers));
        int bufferSize = numbers.length * 4;

        long adapter = getAdapterAsync();
        long device = WgpuJava.wgpuNative.wgpu_adapter_request_device(adapter, 0,
                new WgpuLimits(1).getPointerTo(), "");

        Pointer stagingBufferDescriptor = new WgpuBufferDescriptor("", bufferSize,
                WgpuBufferDescriptor.MAP_READ | WgpuBufferDescriptor.COPY_DST, false).getPointerTo();
        Pointer storageBufferDescriptor = new WgpuBufferDescriptor("", bufferSize,
                WgpuBufferDescriptor.STORAGE | WgpuBufferDescriptor.COPY_DST | WgpuBufferDescriptor.COPY_SRC | WgpuBufferDescriptor.UNIFORM, true)
                .getPointerTo();

        long stagingBuffer = WgpuJava.wgpuNative.wgpu_device_create_buffer(device, stagingBufferDescriptor);
        long storageBuffer = WgpuJava.wgpuNative.wgpu_device_create_buffer(device, storageBufferDescriptor);
        Pointer storageData = WgpuJava.wgpuNative.wgpu_buffer_get_mapped_range(storageBuffer, 0, bufferSize);

        for(int i = 0; i < numbers.length; i++){
            storageData.putInt(i * Integer.BYTES, numbers[i]);
        }

        WgpuJava.wgpuNative.wgpu_buffer_unmap(storageBuffer);

        long bindGroupLayout = createBindGroupLayout(device);
        long bindGroup = createBindGroup(device, bindGroupLayout, storageBuffer, bufferSize);

        Pointer pipelineLayoutDesc = new WgpuPipelineLayoutDescriptor(bindGroupLayout).getPointerTo();
        long pipelineLayout = WgpuJava.wgpuNative.wgpu_device_create_pipeline_layout(device, pipelineLayoutDesc);
        long shader = ShaderData.fromClasspathFile("/collatz.comp.spv", "main").createModule(device);

        Pointer pipelineDesc = new WgpuComputePipelineDescriptor(pipelineLayout, shader, "main").getPointerTo();
        long pipelineId = WgpuJava.wgpuNative.wgpu_device_create_compute_pipeline(device, pipelineDesc);

        long encoder = WgpuJava.wgpuNative.wgpu_device_create_command_encoder(device,
                new WgpuCommandEncoderDescriptor("command encoder").getPointerTo());

        WgpuRawPass rawPass = WgpuJava.wgpuNative.wgpu_command_encoder_begin_compute_pass(encoder,
                WgpuJava.createNullPointer());

        WgpuJava.wgpuNative.wgpu_compute_pass_set_pipeline(rawPass.getPointerTo(), pipelineId);
        WgpuJava.wgpuNative.wgpu_compute_pass_set_bind_group(rawPass.getPointerTo(), 0, bindGroup,
                WgpuJava.createNullPointer(), 0);
        WgpuJava.wgpuNative.wgpu_compute_pass_dispatch(rawPass.getPointerTo(), numbers.length, 1, 1);
        WgpuJava.wgpuNative.wgpu_compute_pass_end_pass(rawPass.getPointerTo());

        long queue = WgpuJava.wgpuNative.wgpu_device_get_default_queue(device);
        long commandBuffer = WgpuJava.wgpuNative.wgpu_command_encoder_finish(encoder, WgpuJava.createNullPointer());

        WgpuJava.wgpuNative.wgpu_command_encoder_copy_buffer_to_buffer(encoder, storageBuffer, 0, stagingBuffer,
                0, bufferSize);

        WgpuJava.wgpuNative.wgpu_queue_submit(queue, WgpuJava.createDirectLongPointer(commandBuffer), 1);

        WgpuJava.wgpuNative.wgpu_buffer_map_read_async(stagingBuffer, 0, bufferSize, (status, userdata) -> {},
                WgpuJava.createNullPointer());
        WgpuJava.wgpuNative.wgpu_device_poll(device, true);
        Pointer data = WgpuJava.wgpuNative.wgpu_buffer_get_mapped_range(stagingBuffer, 0, bufferSize);

        System.out.println("Times:");

        for(int i = 0; i < numbers.length; i++){
            System.out.println(numbers[i] + ": " + data.getInt(i * Integer.BYTES) + " steps");
        }

        WgpuJava.wgpuNative.wgpu_buffer_unmap(stagingBuffer);
    }

    private static long createBindGroup(long device, long layout, long storageBuffer, long bufferSize) {
        var entry = new WgpuBindGroupEntry();
        entry.setBinding(0);
        entry.getResource().setTag(WgpuBindingResourceTag.BUFFER);
        entry.getResource().getData().getBinding().set(storageBuffer, 0, bufferSize);

        var desc  = new WgpuBindGroupDescriptor("bind group", layout, entry);

        return WgpuJava.wgpuNative.wgpu_device_create_bind_group(device, desc.getPointerTo());
    }

    private static long createBindGroupLayout(long device){
        var entry = new WgpuBindGroupLayoutEntry();
        entry.setPartial(0, WgpuBindGroupLayoutEntry.SHADER_STAGE_COMPUTE, WgpuBindingType.STORAGE_BUFFER);

        var descriptor = new WgpuBindGroupLayoutDescriptor("bind group layout", entry);

        return WgpuJava.wgpuNative.wgpu_device_create_bind_group_layout(device, descriptor.getPointerTo());
    }

    private static long getAdapterAsync() {
        AtomicLong adapter = new AtomicLong();

        WgpuJava.wgpuNative.wgpu_request_adapter_async(
                WgpuJava.createNullPointer(),
                2 | 4 | 8,
                false,
                (received, userData) -> {
                    adapter.set(received);
                },
                WgpuJava.createNullPointer()
        );

        return adapter.get();
    }
}
