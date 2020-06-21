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

    public void copyToTexture(long encoder, long textureId, ImageData data){
        var bufferCopy = WgpuBufferCopyView.createDirect();
        bufferCopy.setBuffer(id);
        bufferCopy.getLayout().setOffset(0);
        bufferCopy.getLayout().setBytesPerRow(Integer.BYTES * data.getWidth());
        bufferCopy.getLayout().setRowsPerImage(data.getHeight());

        var textureCopy = WgpuTextureCopyView.createDirect();
        textureCopy.setTexture(textureId);
        textureCopy.setMipLevel(0);
        textureCopy.getOrigin().setX(0);
        textureCopy.getOrigin().setY(0);
        textureCopy.getOrigin().setZ(0);

        var extent = WgpuExtent3d.createDirect();
        extent.setWidth(data.getWidth());
        extent.setHeight(data.getHeight());
        extent.setDepth(1);

        WgpuJava.wgpuNative.wgpu_command_encoder_copy_buffer_to_texture(encoder, bufferCopy.getPointerTo(),
                textureCopy.getPointerTo(), extent.getPointerTo());
    }

    public void readAsync(){
        readAsync((status, userdata) -> {}, WgpuJava.createNullPointer());
    }

    public void readAsync(BufferMapCallback callback, Pointer userData){
        WgpuJava.wgpuNative.wgpu_buffer_map_read_async(id, 0, size, callback, userData);
    }

    public void copyTo(Buffer destination, long commandEncoder){
        WgpuJava.wgpuNative.wgpu_command_encoder_copy_buffer_to_buffer(commandEncoder,
                id, 0, destination.id, 0, size);
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
