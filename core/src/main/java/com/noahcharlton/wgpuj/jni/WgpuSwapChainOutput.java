package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuSwapChainOutput extends WgpuJavaStruct {

    private final Struct.Enum<WgpuSwapChainStatus> status = new Struct.Enum<>(WgpuSwapChainStatus.class);
    private final Struct.Unsigned64 textureViewID = new Struct.Unsigned64();

    public WgpuSwapChainOutput() {
        useDirectMemory();
    }

    public WgpuSwapChainStatus getStatus() {
        return status.get();
    }

    public long getTextureViewID() {
        return textureViewID.get();
    }
}
