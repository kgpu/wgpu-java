package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.compute.ComputePass;
import com.noahcharlton.wgpuj.core.graphics.RenderPass;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.ImageData;
import com.noahcharlton.wgpuj.jni.*;
import jnr.ffi.Pointer;

public class CommandEncoder {

    private final long id;

    public CommandEncoder(long id) {
        this.id = id;
    }

    public RenderPass beginRenderPass(WgpuRenderPassDescriptor desc){
        var rawPass = WgpuJava.wgpuNative.wgpu_command_encoder_begin_render_pass(id, desc.getPointerTo());

        return new RenderPass(rawPass);
    }

    public ComputePass beginComputePass(WgpuComputePassDescriptor desc){
        var computePassId = WgpuJava.wgpuNative.wgpu_command_encoder_begin_compute_pass(id,
                desc.getPointerTo());

        return new ComputePass(computePassId);
    }

    public long finish(WgpuCommandBufferDescriptor desc){
        return WgpuJava.wgpuNative.wgpu_command_encoder_finish(id, desc.getPointerTo());
    }

    public void copyBufferToBuffer(Buffer source, Buffer destination){
        if(source.getSize() > destination.getSize())
            throw new UnsupportedOperationException("Source cannot be bigger than destination!");

        WgpuJava.wgpuNative.wgpu_command_encoder_copy_buffer_to_buffer(id,
                source.getId(), 0, destination.getId(), 0, Pointer.wrap(WgpuJava.getRuntime(), source.getSize()));
    }

    public void copyBufferToTexture(Buffer buffer, long textureId, ImageData data){
        var bufferCopy = WgpuBufferCopyView.createDirect();
        bufferCopy.setBuffer(buffer.getId());
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

        WgpuJava.wgpuNative.wgpu_command_encoder_copy_buffer_to_texture(id, bufferCopy.getPointerTo(),
                textureCopy.getPointerTo(), extent.getPointerTo());
    }

}
