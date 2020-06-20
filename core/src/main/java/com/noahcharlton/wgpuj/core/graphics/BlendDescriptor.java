package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.jni.WgpuBlendDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;

public class BlendDescriptor {

    private final WgpuBlendFactor src;
    private final WgpuBlendFactor dest;
    private final WgpuBlendOperation operation;

    public BlendDescriptor(WgpuBlendFactor src, WgpuBlendFactor dest, WgpuBlendOperation operation) {
        this.src = src;
        this.dest = dest;
        this.operation = operation;
    }

    public void applyTo(WgpuBlendDescriptor descriptor) {
        descriptor.setSrcFactor(src);
        descriptor.setDstFactor(dest);
        descriptor.setOperation(operation);
    }

    public WgpuBlendFactor getDest() {
        return dest;
    }

    public WgpuBlendFactor getSrc() {
        return src;
    }

    public WgpuBlendOperation getOperation() {
        return operation;
    }
}
