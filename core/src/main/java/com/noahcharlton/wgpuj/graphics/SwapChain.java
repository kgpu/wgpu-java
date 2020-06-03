package com.noahcharlton.wgpuj.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainOutput;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainStatus;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.util.Dimension;

public class SwapChain {

    private final long chainID;

    public SwapChain(long chainID) {
        this.chainID = chainID;
    }

    public void update(){
        var output = new WgpuSwapChainOutput();

        WgpuJava.wgpuNative.wgpu_swap_chain_get_next_texture_jnr_hack(chainID, output.getPointerTo());

        if(output.getStatus() != WgpuSwapChainStatus.GOOD){
            throw new RuntimeException("Wgpu Swap Chain has a bad status: " + output.getStatus());
        }else if(output.getTextureViewID() == 0){
            throw new RuntimeException();
        }
    }

    public static SwapChain create(Dimension dimension, long device, long surface){
        var descriptor = new WgpuSwapChainDescriptor(
                WgpuSwapChainDescriptor.TEXTURE_OUTPUT_ATTACHMENT,
                WgpuTextureFormat.Bgra8Unorm,
                dimension,
                WgpuPresentMode.FIFO);

        var id = WgpuJava.wgpuNative.wgpu_device_create_swap_chain(device, surface, descriptor.getPointerTo());

        return new SwapChain(id);
    }
}
