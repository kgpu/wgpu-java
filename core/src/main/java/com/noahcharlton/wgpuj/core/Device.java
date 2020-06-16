package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.BufferSettings;
import com.noahcharlton.wgpuj.core.util.BufferUsage;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutEntry;
import com.noahcharlton.wgpuj.jni.WgpuCommandEncoderDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuJNI;
import com.noahcharlton.wgpuj.jni.WgpuLimits;
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
        return natives.wgpu_device_create_pipeline_layout(deviceId,
                new WgpuPipelineLayoutDescriptor(bindGroupLayouts).getPointerTo());
    }

    public long createCommandEncoder(String name){
        return natives.wgpu_device_create_command_encoder(deviceId,
                new WgpuCommandEncoderDescriptor(name).getPointerTo());
    }

    public long createTexture(WgpuTextureDescriptor descriptor){
        return natives.wgpu_device_create_texture(deviceId, descriptor.getPointerTo());
    }

    public long createBindGroupLayout(String label, WgpuBindGroupLayoutEntry... entries) {
        return natives.wgpu_device_create_bind_group_layout(deviceId,
                new WgpuBindGroupLayoutDescriptor(label, entries).getPointerTo());
    }

    public long createBindGroup(String name, long layout, WgpuBindGroupEntry... entries) {
        return natives.wgpu_device_create_bind_group(deviceId,
                new WgpuBindGroupDescriptor(name, layout, entries).getPointerTo());
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
        var options = new WgpuRequestAdapterOptions(WgpuPowerPreference.DEFAULT, surface);
        var adapter = requestAdapter(options, settings);
        var limits = new WgpuLimits(1).getPointerTo();
        long id = WgpuJava.wgpuNative.wgpu_adapter_request_device(adapter, 0, limits, null);

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
