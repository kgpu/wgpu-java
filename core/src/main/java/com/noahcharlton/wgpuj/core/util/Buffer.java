package com.noahcharlton.wgpuj.core.util;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.BufferMapCallback;
import com.noahcharlton.wgpuj.jni.WgpuBufferCopyView;
import com.noahcharlton.wgpuj.jni.WgpuExtent3d;
import com.noahcharlton.wgpuj.jni.WgpuInputStepMode;
import com.noahcharlton.wgpuj.jni.WgpuTextureCopyView;
import com.noahcharlton.wgpuj.jni.WgpuVertexAttributeDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexFormat;
import jnr.ffi.Pointer;

public class Buffer {

    private final long id;
    private final long size;

    protected Buffer(long id, long size) {
        this.id = id;
        this.size = size;
    }

    public static WgpuVertexBufferLayoutDescriptor createLayout(long stride, WgpuInputStepMode mode,
                                                                WgpuVertexAttributeDescriptor... attributes){
        var desc = WgpuVertexBufferLayoutDescriptor.createDirect();
        desc.setArrayStride(stride);
        desc.setStepMode(mode);
        desc.setAttributes(attributes);
        desc.setAttributesLength(attributes.length);

        return desc;
    }

    public static WgpuVertexAttributeDescriptor vertexAttribute(long offset, WgpuVertexFormat format,
                                                                int shaderLocation){
        var desc = WgpuVertexAttributeDescriptor.createDirect();
        desc.setOffset(offset);
        desc.setFormat(format);
        desc.setShaderLocation(shaderLocation);

        return desc;
    }

    public void readAsync(){
        readAsync((status, userdata) -> {}, WgpuJava.createNullPointer());
    }

    public void readAsync(BufferMapCallback callback, Pointer userData){
        WgpuJava.wgpuNative.wgpu_buffer_map_read_async(id, 0, size, callback, userData);
    }

    public Pointer getMappedData(){
        return WgpuJava.wgpuNative.wgpu_buffer_get_mapped_range(id, 0, size);
    }

    public void unmap(){
        WgpuJava.wgpuNative.wgpu_buffer_unmap(id);
    }

    public long getId() {
        return id;
    }

    public long getSize() {
        return size;
    }
}
