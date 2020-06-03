package com.noahcharlton.wgpuj.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainStatus;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.util.Dimension;

public class SwapChain {

    private final long chainID;

    public SwapChain(long chainID) {
        this.chainID = chainID;
    }

    public void update(){
        System.out.println("Swap Chain run: " + Long.toBinaryString(chainID));
        var output = WgpuJava.wgpuNative.wgpu_swap_chain_get_next_texture(chainID);
        var status = output.getStatus();

        if(status != WgpuSwapChainStatus.GOOD){
            throw new RuntimeException("Swap chain not good: " + status);
        }
    }

    public static SwapChain create(Dimension dimension, long device, long surface){
        var descriptor = new WgpuSwapChainDescriptor(
                WgpuSwapChainDescriptor.TEXTURE_OUTPUT_ATTACHMENT,
                WgpuTextureFormat.Bgra8Unorm,
                dimension,
                WgpuPresentMode.FIFO);

        var id = WgpuJava.wgpuNative.wgpu_device_create_swap_chain(device, surface, descriptor.getPointerTo());

        System.out.println("Swap Chain created: " + id);
        return new SwapChain(id);
    }
}
