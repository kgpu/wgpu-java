package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.jni.Wgpu;
import com.noahcharlton.wgpuj.jni.WgpuJNI;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainOutput;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainStatus;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;

public class SwapChain {

    private final long chainID;

    private final WgpuJNI natives = WgpuJava.wgpuNative;
    private final Device device;

    private RenderPass rawPass;
    private long encoder;

    public SwapChain(long chainID, Device device) {
        this.chainID = chainID;
        this.device = device;
    }

    public void renderStart(RenderPipeline pipeline){
        rawPass = beginRenderPass(pipeline.getClearColor());

        rawPass.setPipeline(pipeline);
    }

    private RenderPass beginRenderPass(Color clearColor) {
        encoder = device.createCommandEncoder("command encoder");

        return RenderPass.create(encoder, getSwapChainTexture(), clearColor);
    }

    private long getSwapChainTexture() {
        var output = WgpuSwapChainOutput.createDirect();

        natives.wgpu_swap_chain_get_next_texture_jnr_hack(chainID, output.getPointerTo());

        if(output.getStatus() != WgpuSwapChainStatus.GOOD){
            throw new RuntimeException("Wgpu Swap Chain has a bad status: " + output.getStatus());
        }else if(output.getViewId() == 0){
            throw new RuntimeException();
        }

        return output.getViewId();
    }

    public void renderEnd(){
        rawPass.endPass();

        long cmdBuffer = natives.wgpu_command_encoder_finish(encoder, WgpuJava.createNullPointer());
        natives.wgpu_queue_submit(device.getDefaultQueue(), WgpuJava.createDirectLongPointer(cmdBuffer), 1);
        natives.wgpu_swap_chain_present(chainID);

        rawPass = null;
        encoder = 0;
    }

    public static SwapChain create(Dimension dimension, Device device, long surface){
        var descriptor = WgpuSwapChainDescriptor.createDirect();
        descriptor.setUsage(Wgpu.TextureUsage.OUTPUT_ATTACHMENT);
        descriptor.setFormat(WgpuTextureFormat.BGRA8_UNORM);
        descriptor.setWidth(dimension.getWidth());
        descriptor.setHeight(dimension.getHeight());
        descriptor.setPresentMode(WgpuPresentMode.FIFO);

        var id = WgpuJava.wgpuNative.wgpu_device_create_swap_chain(device.getId(), surface,
                descriptor.getPointerTo());

        return new SwapChain(id, device);
    }

    public RenderPass getRenderPass() {
        return rawPass;
    }
}
