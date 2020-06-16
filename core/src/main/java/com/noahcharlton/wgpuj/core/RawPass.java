package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.WgpuJNI;
import com.noahcharlton.wgpuj.jni.WgpuRawPass;
import jnr.ffi.Pointer;

public abstract class RawPass {

    protected final WgpuJNI natives = WgpuJava.wgpuNative;
    protected WgpuRawPass pass;
    protected Pointer passPointer;

    protected RawPass(WgpuRawPass pass) {
        this.pass = pass;
        this.passPointer = pass.getPointerTo();
    }

    public abstract void endPass();
}
