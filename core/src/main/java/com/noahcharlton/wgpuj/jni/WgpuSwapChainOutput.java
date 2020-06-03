package com.noahcharlton.wgpuj.jni;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuSwapChainOutput extends Struct {

    private final Struct.Enum<WgpuSwapChainStatus> status = new Struct.Enum<>(WgpuSwapChainStatus.class);
    private final Struct.Unsigned64 textureViewID = new Struct.Unsigned64();

    public WgpuSwapChainOutput(Runtime runtime) {
        super(runtime);
    }

    public WgpuSwapChainStatus getStatus() {
        return status.get();
    }
}
