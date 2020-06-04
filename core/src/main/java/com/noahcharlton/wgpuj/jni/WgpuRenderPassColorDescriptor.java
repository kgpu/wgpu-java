package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.Color;
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
        this.clearColor = inner(new WgpuColor());
    }

    public WgpuRenderPassColorDescriptor(long attachment, WgpuLoadOp loadOp, WgpuStoreOp storeOp, Color clearColor) {
        this(WgpuJava.getRuntime());

        useDirectMemory();
        this.attachment.set(attachment);
        this.loadOp.set(loadOp.getIntValue());
        this.storeOp.set(storeOp.getIntValue());
        this.clearColor.set(clearColor);
    }
}
