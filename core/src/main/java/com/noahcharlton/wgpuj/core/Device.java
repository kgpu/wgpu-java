package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.graphics.SwapChain;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.BufferSettings;
import com.noahcharlton.wgpuj.core.util.BufferUsage;
import com.noahcharlton.wgpuj.jni.*;
import com.noahcharlton.wgpuj.util.RustCString;
import jnr.ffi.Pointer;

import java.util.concurrent.atomic.AtomicLong;

public class Device {

    private final WgpuJNI natives = WgpuJava.wgpuNative;
    private final Queue defaultQueue;
    private final long deviceId;

    public Device(long id) {
        deviceId = id;
        defaultQueue = new Queue(natives.wgpu_device_get_default_queue(deviceId));
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

    public CommandEncoder createCommandEncoder(String name){
        var desc = WgpuCommandEncoderDescriptor.createDirect();
        desc.setLabel(name);

        var encoderId = natives.wgpu_device_create_command_encoder(deviceId, desc.getPointerTo());

        return new CommandEncoder(encoderId);
    }

    public long createTexture(WgpuTextureDescriptor descriptor){
        return natives.wgpu_device_create_texture(deviceId, descriptor.getPointerTo());
    }

    public long createBindGroupLayout(String label, WgpuBindGroupLayoutEntry... entries) {
        var desc = WgpuBindGroupLayoutDescriptor.createDirect();
        desc.setLabel(label);
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
        ptr.put(0, data, 0, data.length);
        buffer.unmap();

        return buffer;
    }

    public SwapChain createSwapChain(long surface, WgpuSwapChainDescriptor desc){
        var swapChainID = WgpuJava.wgpuNative.wgpu_device_create_swap_chain(deviceId, surface,
                desc.getPointerTo());

        return new SwapChain(swapChainID, this);
    }

    public Buffer createIntBuffer(String name, int[] data, BufferUsage... usages) {
        var buffer = new BufferSettings()
                .setLabel(name)
                .setSize(data.length * Integer.BYTES)
                .setMapped(true)
                .setUsages(usages)
                .createBuffer(this);
        var ptr = buffer.getMappedData();
        ptr.put(0, data, 0, data.length);
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
        ptr.put(0, data, 0, data.length);

        buffer.unmap();

        return buffer;
    }

    public static Device create(DeviceSettings settings, long surface) {
        var optionsDesc = WgpuRequestAdapterOptions.createDirect();
        optionsDesc.setCompatibleSurface(surface);
        optionsDesc.setPowerPreference(WgpuPowerPreference.DEFAULT);

        var tracePath = RustCString.toPointer(settings.getTracePath());
        var limitsDesc = WgpuCLimits.createDirect();
        limitsDesc.setMaxBindGroups(16);

        var adapter = requestAdapter(optionsDesc, settings);
        long id = WgpuJava.wgpuNative.wgpu_adapter_request_device(adapter, settings.getExtensions(),
                limitsDesc.getPointerTo(), tracePath);

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

    public Queue getDefaultQueue() {
        return defaultQueue;
    }

    public long getId() {
        return deviceId;
    }
}
