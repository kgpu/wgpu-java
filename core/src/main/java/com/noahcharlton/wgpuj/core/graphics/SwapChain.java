package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.jni.WgpuCommandEncoderDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuLoadOp;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuRawPass;
import com.noahcharlton.wgpuj.jni.WgpuRawPassDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRenderPassColorDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuStoreOp;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainOutput;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainStatus;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;

public class SwapChain {

    private final long chainID;

    private long device;
    private long encoderID;

    private WgpuRawPass rawPass;

    public SwapChain(long chainID) {
        this.chainID = chainID;
    }

    public void renderStart(RenderPipeline pipeline, long device){
        this.device = device;
        this.rawPass = beginRenderPass();

        WgpuJava.wgpuNative.wgpu_render_pass_set_pipeline(rawPass.getPointerTo(), pipeline.getPipelineID());
    }

    private WgpuRawPass beginRenderPass(){
        long textureID = getSwapChainTexture();
        encoderID = WgpuJava.wgpuNative.wgpu_device_create_command_encoder(device,
                new WgpuCommandEncoderDescriptor("command_encoder").getPointerTo());
        var clearColor = Color.GREEN;
        var rawPassDescriptor = new WgpuRawPassDescriptor(null, new WgpuRenderPassColorDescriptor(textureID,
                WgpuLoadOp.CLEAR, WgpuStoreOp.STORE, clearColor.r, clearColor.g, clearColor.b, clearColor.a));

        return WgpuJava.wgpuNative.wgpu_command_encoder_begin_render_pass(encoderID,
                rawPassDescriptor.getPointerTo());
    }

    public void setBindGroup(int set, long bindGroup){
        WgpuJava.wgpuNative.wgpu_render_pass_set_bind_group(rawPass.getPointerTo(), set, bindGroup,
                WgpuJava.createNullPointer(), 0);
    }

    public void setIndexBuffer(Buffer buffer){
        WgpuJava.wgpuNative.wgpu_render_pass_set_index_buffer(rawPass.getPointerTo(), buffer.getId(), 0,
                buffer.getSize());
    }

    public void setVertexBuffer(Buffer buffer, int slot){
        WgpuJava.wgpuNative.wgpu_render_pass_set_vertex_buffer(rawPass.getPointerTo(), slot, buffer.getId(),
                0, buffer.getSize());
    }

    public void drawIndexed(int indexCount, int instances, int indexOffset){
        WgpuJava.wgpuNative.wgpu_render_pass_draw_indexed(rawPass.getPointerTo(),
                indexCount, instances, indexOffset, 0, 0);
    }

    public void draw(int vertices, int instances){
        WgpuJava.wgpuNative.wgpu_render_pass_draw(rawPass.getPointerTo(), vertices, instances, 0, 0);
    }

    private long getSwapChainTexture() {
        var output = new WgpuSwapChainOutput();

        WgpuJava.wgpuNative.wgpu_swap_chain_get_next_texture_jnr_hack(chainID, output.getPointerTo());

        if(output.getStatus() != WgpuSwapChainStatus.GOOD){
            throw new RuntimeException("Wgpu Swap Chain has a bad status: " + output.getStatus());
        }else if(output.getTextureViewID() == 0){
            throw new RuntimeException();
        }

        return output.getTextureViewID();
    }

    public void renderEnd(){
        long queue = WgpuJava.wgpuNative.wgpu_device_get_default_queue(device);
        WgpuJava.wgpuNative.wgpu_render_pass_end_pass(rawPass.getPointerTo());

        long cmdBuffer = WgpuJava.wgpuNative.wgpu_command_encoder_finish(encoderID, WgpuJava.createNullPointer());
        WgpuJava.wgpuNative.wgpu_queue_submit(queue, WgpuJava.createDirectLongPointer(cmdBuffer), 1);
        WgpuJava.wgpuNative.wgpu_swap_chain_present(chainID);

        rawPass = null;
    }

    public static SwapChain create(Dimension dimension, long device, long surface){
        var descriptor = new WgpuSwapChainDescriptor(
                WgpuSwapChainDescriptor.TEXTURE_OUTPUT_ATTACHMENT,
                WgpuTextureFormat.BGRA8_UNORM,
                dimension.getWidth(),
                dimension.getHeight(),
                WgpuPresentMode.FIFO);

        var id = WgpuJava.wgpuNative.wgpu_device_create_swap_chain(device, surface, descriptor.getPointerTo());

        return new SwapChain(id);
    }
}
