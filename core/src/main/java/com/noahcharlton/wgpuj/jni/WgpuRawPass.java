package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuRawPass extends WgpuJavaStruct {

    private final Struct.Pointer data = new Struct.Pointer();
    private final Struct.Pointer base = new Struct.Pointer();
    private final Struct.UnsignedLong capacity = new Struct.UnsignedLong();
    private final Struct.Unsigned64 parent = new Struct.Unsigned64();

    public WgpuRawPass(Runtime runtime) {
        super(runtime);
    }
}
