package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.RustCString;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuProgrammableStageDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 module = new Struct.Unsigned64();
    private final Struct.Pointer entryPoint = new Struct.Pointer();

    public WgpuProgrammableStageDescriptor() {

    }

    public WgpuProgrammableStageDescriptor(long module, java.lang.String entryPoint) {
        useDirectMemory();

        set(module, entryPoint);
    }

    public void set(long module, java.lang.String entryPoint) {
        this.module.set(module);
        this.entryPoint.set(RustCString.toPointer(entryPoint));
    }
}
