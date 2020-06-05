package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuRawPassDescriptor extends WgpuJavaStruct {

    private final Struct.StructRef<WgpuRenderPassColorDescriptor> colorAttachments =
            new Struct.StructRef<>(WgpuRenderPassColorDescriptor.class);
    private final Struct.UnsignedLong colorAttachmentsLength = new Struct.UnsignedLong();
    private final Struct.Pointer depthStencilAttachment = new Struct.Pointer();

    public WgpuRawPassDescriptor(WgpuRenderPassDepthStencilDescriptor depthStencilDescriptor,
                                 WgpuRenderPassColorDescriptor... colorAttachments) {
        useDirectMemory();

        this.colorAttachments.set(colorAttachments);
        this.colorAttachmentsLength.set(colorAttachments.length);
        this.depthStencilAttachment.set(WgpuJava.createNullPointer());

        if(depthStencilDescriptor != null)
            throw new UnsupportedOperationException("Depth stencil not implemented!");
    }
}
