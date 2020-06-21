package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.BufferSettings;
import com.noahcharlton.wgpuj.core.util.BufferUsage;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutEntry;
import com.noahcharlton.wgpuj.jni.WgpuCLimits;
import com.noahcharlton.wgpuj.jni.WgpuCommandEncoderDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuJNI;
import com.noahcharlton.wgpuj.jni.WgpuPipelineLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuRequestAdapterOptions;
import com.noahcharlton.wgpuj.jni.WgpuTextureDescriptor;

import java.util.concurrent.atomic.AtomicLong;

public class Device {

    private final WgpuJNI natives = WgpuJava.wgpuNative;
    private final long deviceId;
    private final long defaultQueue;

    public Device(long id) {
        deviceId = id;
        defaultQueue = natives.wgpu_device_get_default_queue(deviceId);
    }

    public void poll(boolean forceWait){
        natives.wgpu_device_poll(deviceId, forceWait);
    }

    public long createPipelineLayout(long... bindGroupLayouts){
        var desc = WgpuPipelineLayoutDescriptor.createDirect();
        var layoutsPtr = WgpuJava.createLongArrayPointer(bindGroupLayouts);

        desc.setBindGroupLayouts(layoutsPtr);
        desc.setBindGroupLayoutsLength(bindGroupLayouts.length);

        return natives.wgpu_device_create_pipeline_layout(deviceId, desc.getPointerTo());
    }

    public long createCommandEncoder(String name){
        var desc = WgpuCommandEncoderDescriptor.createDirect();
        desc.setLabel(name);

        return natives.wgpu_device_create_command_encoder(deviceId, desc.getPointerTo());
    }

    public long createTexture(WgpuTextureDescriptor descriptor){
        return natives.wgpu_device_create_texture(deviceId, descriptor.getPointerTo());
    }

    public long createBindGroupLayout(String label, WgpuBindGroupLayoutEntry... entries) {
        var desc = WgpuBindGroupLayoutDescriptor.createDirect();
        desc.setEntries(entries);
        desc.setEntriesLength(entries.length);

        return natives.wgpu_device_create_bind_group_layout(deviceId,
                desc.getPointerTo());
    }

    public long createBindGroup(String name, long layout, WgpuBindGroupEntry... entries) {
        var desc = WgpuBindGroupDescriptor.createDirect();
        desc.setLabel(name);
        desc.setLayout(layout);
        desc.setEntries(entries);
        desc.setEntriesLength(entries.length);

        return natives.wgpu_device_create_bind_group(deviceId, desc.getPointerTo());
    }

    public Buffer createVertexBuffer(String name, float[] data) {
        return createFloatBuffer(name, data, BufferUsage.VERTEX);
    }

    public Buffer createFloatBuffer(String name, float[] data, BufferUsage... usages) {
        var buffer = new BufferSettings()
                .setLabel(name)
                .setSize(data.length * Float.BYTES)
                .setMapped(true)
                .setUsages(usages)
                .createBuffer(this);
        var ptr = buffer.getMappedData();

        for(int i = 0; i < data.length; i++) {
            ptr.put(0, data, 0, data.length);
        }

        buffer.unmap();

        return buffer;
    }

    public Buffer createIntBuffer(String name, int[] data, BufferUsage... usages) {
        var buffer = new BufferSettings()
                .setLabel(name)
                .setSize(data.length * Integer.BYTES)
                .setMapped(true)
                .setUsages(usages)
                .createBuffer(this);
        var ptr = buffer.getMappedData();

        for(int i = 0; i < data.length; i++) {
            ptr.put(0, data, 0, data.length);
        }

        buffer.unmap();

        return buffer;
    }

    public Buffer createIndexBuffer(String name, short[] data) {
        return createShortBuffer(name, data, BufferUsage.INDEX);
    }

    public Buffer createShortBuffer(String name, short[] data, BufferUsage... usages) {
        var buffer = new BufferSettings()
                .setLabel(name)
                .setSize(data.length * Short.BYTES)
                .setMapped(true)
                .setUsages(usages)
                .createBuffer(this);
        var ptr = buffer.getMappedData();

        for(int i = 0; i < data.length; i++) {
            ptr.put(0, data, 0, data.length);
        }

        buffer.unmap();

        return buffer;
    }

    public static Device create(DeviceSettings settings, long surface) {
        var optionsDesc = WgpuRequestAdapterOptions.createDirect();
        optionsDesc.setCompatibleSurface(surface);
        optionsDesc.setPowerPreference(WgpuPowerPreference.DEFAULT);

        var limitsDesc = WgpuCLimits.createDirect();
        limitsDesc.setMaxBindGroups(16);

        var adapter = requestAdapter(optionsDesc, settings);
        long id = WgpuJava.wgpuNative.wgpu_adapter_request_device(adapter, 0, limitsDesc.getPointerTo(), null);

        return new Device(id);
    }

    private static long requestAdapter(WgpuRequestAdapterOptions options, DeviceSettings settings) {
        AtomicLong adapter = new AtomicLong(0);

        WgpuJava.wgpuNative.wgpu_request_adapter_async(
                options.getPointerTo(),
                settings.getBackend(),
                false,
                (received, userData) -> {
                    adapter.set(received);
                },
                WgpuJava.createNullPointer()
        );

        return adapter.get();
    }

    public long getDefaultQueue(){
        return defaultQueue;
    }

    public long getId() {
        return deviceId;
    }
}
