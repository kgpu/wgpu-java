package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuRenderPassColorDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 attachment;
    private final Struct.Unsigned64 resolveTarget;
    private final Struct.Enum<WgpuLoadOp> loadOp;
    private final Struct.Enum<WgpuStoreOp> storeOp;
    private final WgpuColor clearColor;

    public WgpuRenderPassColorDescriptor(Runtime runtime) {
        super(runtime);

        this.attachment = new Struct.Unsigned64();
        this.resolveTarget = new Struct.Unsigned64();
        this.loadOp = new Struct.Enum<>(WgpuLoadOp.class);
        this.storeOp = new Struct.Enum<>(WgpuStoreOp.class);
        this.clearColor = inner(WgpuColor.createHeap());
    }

    public WgpuRenderPassColorDescriptor(long attachment, WgpuLoadOp loadOp, WgpuStoreOp storeOp,
                                         double clearR, double clearG, double clearB, double clearA) {
        this(WgpuJava.getRuntime());

        useDirectMemory();
        this.attachment.set(attachment);
        this.loadOp.set(loadOp);
        this.storeOp.set(storeOp);
        this.clearColor.setR(clearR);
        this.clearColor.setG(clearG);
        this.clearColor.setB(clearB);
        this.clearColor.setA(clearA);
    }
}
