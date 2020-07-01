package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.RawPass;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.jni.WgpuLoadOp;
import com.noahcharlton.wgpuj.jni.WgpuRawPass;
import com.noahcharlton.wgpuj.jni.WgpuRenderPassDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRenderPassColorDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuStoreOp;

public class RenderPass extends RawPass {

    private RenderPass(WgpuRawPass pass) {
        super(pass);
    }

    public void setPipeline(RenderPipeline pipeline){
        natives.wgpu_render_pass_set_pipeline(passPointer, pipeline.getPipelineID());
    }

    public void setBindGroup(int set, long bindGroup){
        natives.wgpu_render_pass_set_bind_group(passPointer, set, bindGroup, WgpuJava.createNullPointer(), 0);
    }

    public void setIndexBuffer(Buffer buffer){
        natives.wgpu_render_pass_set_index_buffer(passPointer, buffer.getId(), 0, buffer.getSize());
    }

    public void setVertexBuffer(Buffer buffer, int slot){
        natives.wgpu_render_pass_set_vertex_buffer(passPointer, slot, buffer.getId(), 0, buffer.getSize());
    }

    public void drawIndexed(int indexCount, int instances, int indexOffset){
        natives.wgpu_render_pass_draw_indexed(passPointer, indexCount, instances, indexOffset, 0, 0);
    }

    public void draw(int vertices, int instances){
        natives.wgpu_render_pass_draw(passPointer, vertices, instances, 0, 0);
    }

    public void endPass(){
        natives.wgpu_render_pass_end_pass(passPointer);

        pass = null;
        passPointer = null;
    }

    static RenderPass create(long commandEncoder, long swapchainTexture, Color clearColor){
        var colorDescriptor = WgpuRenderPassColorDescriptor.createDirect();
        colorDescriptor.setAttachment(swapchainTexture);
        colorDescriptor.setLoadOp(WgpuLoadOp.CLEAR);
        colorDescriptor.setStoreOp(WgpuStoreOp.STORE);
        colorDescriptor.getClearColor().setR(clearColor.r);
        colorDescriptor.getClearColor().setR(clearColor.g);
        colorDescriptor.getClearColor().setR(clearColor.b);
        colorDescriptor.getClearColor().setR(clearColor.a);

        var rawPassDescriptor = new WgpuRenderPassDescriptor(null, colorDescriptor);

        var pass = WgpuJava.wgpuNative.wgpu_command_encoder_begin_render_pass(commandEncoder,
                rawPassDescriptor.getPointerTo());

        return new RenderPass(pass);
    }
}
