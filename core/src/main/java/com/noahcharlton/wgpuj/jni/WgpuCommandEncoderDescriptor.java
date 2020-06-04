package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.RustCString;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuCommandEncoderDescriptor extends WgpuJavaStruct {

    private final Struct.Pointer label = new Struct.Pointer();

    public WgpuCommandEncoderDescriptor(java.lang.String label) {
        useDirectMemory();

        this.label.set(RustCString.toPointer(label));
    }
}
