package com.noahcharlton.wgpuj.core.util;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.WgpuBufferDescriptor;

public class BufferSettings {

    private String label = "";
    private long size = 0;
    private BufferUsage[] usages = new BufferUsage[0];
    private boolean mapped = false;

    public Buffer createBuffer(long device){
        var descriptor = toDescriptor().getPointerTo();
        var id = WgpuJava.wgpuNative.wgpu_device_create_buffer(device, descriptor);

        return new Buffer(id, size);
    }

    public WgpuBufferDescriptor toDescriptor(){
        return new WgpuBufferDescriptor(
                label,
                size,
                calculateUsage(),
                mapped
        );
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

    public BufferSettings setLabel(String label) {
        this.label = label;
        return this;
    }

    public long getSize() {
        return size;
    }

    public BufferSettings setSize(long size) {
        this.size = size;
        return this;
    }

    public BufferUsage[] getUsages() {
        return usages;
    }

    public BufferSettings setUsages(BufferUsage... usages) {
        this.usages = usages;

        return this;
    }

    public boolean isMapped() {
        return mapped;
    }

    public BufferSettings setMapped(boolean mapped) {
        this.mapped = mapped;
        return this;
    }
}
