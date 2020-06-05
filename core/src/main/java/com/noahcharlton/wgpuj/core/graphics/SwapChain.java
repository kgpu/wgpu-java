package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
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

    private long encoderID;

    public SwapChain(long chainID) {
        this.chainID = chainID;
    }

    public WgpuRawPass beginRenderPass(long device){
        long textureID = getSwapChainTexture();
        encoderID = WgpuJava.wgpuNative.wgpu_device_create_command_encoder(device,
                new WgpuCommandEncoderDescriptor("command_encoder").getPointerTo());
        var clearColor = Color.GREEN;
        var rawPassDescriptor = new WgpuRawPassDescriptor(null, new WgpuRenderPassColorDescriptor(textureID,
                WgpuLoadOp.CLEAR, WgpuStoreOp.STORE, clearColor.r, clearColor.g, clearColor.b, clearColor.a));

        return WgpuJava.wgpuNative.wgpu_command_encoder_begin_render_pass(encoderID,
                rawPassDescriptor.getPointerTo());
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

    public void endRenderPass(WgpuRawPass rawPass, long device){
        long queue = WgpuJava.wgpuNative.wgpu_device_get_default_queue(device);
        WgpuJava.wgpuNative.wgpu_render_pass_end_pass(rawPass.getPointerTo());

        long cmdBuffer = WgpuJava.wgpuNative.wgpu_command_encoder_finish(encoderID, WgpuJava.createNullPointer());
        WgpuJava.wgpuNative.wgpu_queue_submit(queue, WgpuJava.createDirectLongPointer(cmdBuffer), 1);
        WgpuJava.wgpuNative.wgpu_swap_chain_present(chainID);
    }

    public static SwapChain create(Dimension dimension, long device, long surface){
        var descriptor = new WgpuSwapChainDescriptor(
                WgpuSwapChainDescriptor.TEXTURE_OUTPUT_ATTACHMENT,
                WgpuTextureFormat.Bgra8Unorm,
                dimension.getWidth(),
                dimension.getHeight(),
                WgpuPresentMode.FIFO);

        var id = WgpuJava.wgpuNative.wgpu_device_create_swap_chain(device, surface, descriptor.getPointerTo());

        return new SwapChain(id);
    }
}
