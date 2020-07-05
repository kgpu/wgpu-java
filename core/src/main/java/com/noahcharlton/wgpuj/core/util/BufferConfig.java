package com.noahcharlton.wgpuj.core.util;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.jni.WgpuBufferDescriptor;

public class BufferConfig {

    private String label = "";
    private long size = 0;
    private BufferUsage[] usages = new BufferUsage[0];
    private boolean mapped = false;

    public Buffer createBuffer(Device device){
        var descriptor = toDescriptor().getPointerTo();
        var id = WgpuJava.wgpuNative.wgpu_device_create_buffer(device.getId(), descriptor);

        return new Buffer(id, size);
    }

    public WgpuBufferDescriptor toDescriptor(){
        var desc = WgpuBufferDescriptor.createDirect();
        desc.setLabel(label);
        desc.setMappedAtCreation(mapped);
        desc.setSize(size);
        desc.setUsage(calculateUsage());

        return desc;
    }

    int calculateUsage() {
        int usagesInt = 0;

        for(BufferUsage usage : usages){
            usagesInt = usagesInt | usage.getValue();
        }
        return usagesInt;
    }

    public String getLabel() {
        return label;
    }

    public BufferConfig setLabel(String label) {
        this.label = label;
        return this;
    }

    public long getSize() {
        return size;
    }

    public BufferConfig setSize(long size) {
        this.size = size;
        return this;
    }

    public BufferUsage[] getUsages() {
        return usages;
    }

    public BufferConfig setUsages(BufferUsage... usages) {
        this.usages = usages;

        return this;
    }

    public boolean isMapped() {
        return mapped;
    }

    public BufferConfig setMapped(boolean mapped) {
        this.mapped = mapped;
        return this;
    }
}
