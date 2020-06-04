package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuRenderPassDepthStencilDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 attachment = new Struct.Unsigned64();
    private final Struct.Enum<WgpuLoadOp> depthLoadOp = new Struct.Enum<>(WgpuLoadOp.class);
    private final Struct.Enum<WgpuStoreOp> depthStoreOp = new Struct.Enum<>(WgpuStoreOp.class);
    private final Struct.Float clearDepth = new Struct.Float();
    private final Struct.Enum<WgpuLoadOp> stencilLoadOp = new Struct.Enum<>(WgpuLoadOp.class);
    private final Struct.Enum<WgpuStoreOp> stencilStoreOp = new Struct.Enum<>(WgpuStoreOp.class);
    private final Struct.Unsigned32 clear_stencil = new Struct.Unsigned32();

    public WgpuRenderPassDepthStencilDescriptor() {
        useDirectMemory();

        throw new UnsupportedOperationException("unimplemented");
    }
}
