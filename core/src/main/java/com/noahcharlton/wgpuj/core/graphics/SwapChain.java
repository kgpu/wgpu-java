package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.CommandEncoder;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.jni.*;

public class SwapChain {

    private final long chainID;

    private final WgpuJNI natives = WgpuJava.wgpuNative;
    private final Device device;

    private RenderPass rawPass;
    private CommandEncoder encoder;

    public SwapChain(long chainID, Device device) {
        this.chainID = chainID;
        this.device = device;
    }

    public void renderStart(Color clearColor){
        encoder = device.createCommandEncoder("Swap Chain Command Encoder");
        rawPass = RenderPass.create(encoder, getSwapChainTexture(), clearColor);
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

        long cmdBuffer = encoder.finish(WgpuCommandBufferDescriptor.createDirect());
        device.getDefaultQueue().submit(cmdBuffer);
        natives.wgpu_swap_chain_present(chainID);

        rawPass = null;
        encoder = null;
    }

    public static SwapChain create(Dimension dimension, Device device, long surface){
        var descriptor = WgpuSwapChainDescriptor.createDirect();
        descriptor.setUsage(Wgpu.TextureUsage.OUTPUT_ATTACHMENT);
        descriptor.setFormat(WgpuTextureFormat.BGRA8_UNORM);
        descriptor.setWidth(dimension.getWidth());
        descriptor.setHeight(dimension.getHeight());
        descriptor.setPresentMode(WgpuPresentMode.FIFO);

        return device.createSwapChain(surface, descriptor);
    }

    public RenderPass getRenderPass() {
        return rawPass;
    }
}
