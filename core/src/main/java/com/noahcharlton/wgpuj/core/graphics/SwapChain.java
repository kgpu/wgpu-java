package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.jni.WgpuJNI;
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

    private final WgpuJNI natives = WgpuJava.wgpuNative;
    private final Device device;
    private long queue;
    private long encoderID;

    private WgpuRawPass rawPass;

    public SwapChain(long chainID, Device device) {
        this.chainID = chainID;
        this.device = device;
    }

    public void renderStart(RenderPipeline pipeline){
        this.queue = device.getDefaultQueue();
        this.rawPass = beginRenderPass(pipeline.getClearColor());

        natives.wgpu_render_pass_set_pipeline(rawPass.getPointerTo(), pipeline.getPipelineID());
    }

    private WgpuRawPass beginRenderPass(Color clearColor) {
        long textureID = getSwapChainTexture();
        encoderID = device.createCommandEncoder("command_encoder");

        var rawPassDescriptor = new WgpuRawPassDescriptor(null, new WgpuRenderPassColorDescriptor(textureID,
                WgpuLoadOp.CLEAR, WgpuStoreOp.STORE, clearColor.r, clearColor.g, clearColor.b, clearColor.a));

        return natives.wgpu_command_encoder_begin_render_pass(encoderID,
                rawPassDescriptor.getPointerTo());
    }

    public void setBindGroup(int set, long bindGroup){
        natives.wgpu_render_pass_set_bind_group(rawPass.getPointerTo(), set, bindGroup,
                WgpuJava.createNullPointer(), 0);
    }

    public void setIndexBuffer(Buffer buffer){
        natives.wgpu_render_pass_set_index_buffer(rawPass.getPointerTo(), buffer.getId(), 0,
                buffer.getSize());
    }

    public void setVertexBuffer(Buffer buffer, int slot){
        natives.wgpu_render_pass_set_vertex_buffer(rawPass.getPointerTo(), slot, buffer.getId(),
                0, buffer.getSize());
    }

    public void drawIndexed(int indexCount, int instances, int indexOffset){
        natives.wgpu_render_pass_draw_indexed(rawPass.getPointerTo(),
                indexCount, instances, indexOffset, 0, 0);
    }

    public void draw(int vertices, int instances){
        natives.wgpu_render_pass_draw(rawPass.getPointerTo(), vertices, instances, 0, 0);
    }

    private long getSwapChainTexture() {
        var output = new WgpuSwapChainOutput();

        natives.wgpu_swap_chain_get_next_texture_jnr_hack(chainID, output.getPointerTo());

        if(output.getStatus() != WgpuSwapChainStatus.GOOD){
            throw new RuntimeException("Wgpu Swap Chain has a bad status: " + output.getStatus());
        }else if(output.getTextureViewID() == 0){
            throw new RuntimeException();
        }

        return output.getTextureViewID();
    }

    public void renderEnd(){
        natives.wgpu_render_pass_end_pass(rawPass.getPointerTo());

        long cmdBuffer = natives.wgpu_command_encoder_finish(encoderID, WgpuJava.createNullPointer());
        natives.wgpu_queue_submit(queue, WgpuJava.createDirectLongPointer(cmdBuffer), 1);
        natives.wgpu_swap_chain_present(chainID);

        rawPass = null;
    }

    public static SwapChain create(Dimension dimension, Device device, long surface){
        var descriptor = new WgpuSwapChainDescriptor(
                WgpuSwapChainDescriptor.TEXTURE_OUTPUT_ATTACHMENT,
                WgpuTextureFormat.BGRA8_UNORM,
                dimension.getWidth(),
                dimension.getHeight(),
                WgpuPresentMode.FIFO);

        var id = WgpuJava.wgpuNative.wgpu_device_create_swap_chain(device.getId(), surface,
                descriptor.getPointerTo());

        return new SwapChain(id, device);
    }
}
