package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.RustCString;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuBindGroupLayoutDescriptor extends WgpuJavaStruct {

    private final Struct.Pointer label = new Struct.Pointer();
    private final Struct.StructRef<WgpuBindGroupLayoutEntry> entries = new StructRef<>(WgpuBindGroupLayoutEntry.class);
    private final Struct.UnsignedLong length = new Struct.UnsignedLong();

    public WgpuBindGroupLayoutDescriptor(java.lang.String label, WgpuBindGroupLayoutEntry... entries) {
        useDirectMemory();

        this.label.set(RustCString.toPointer(label));
        this.entries.set(entries);
        this.length.set(entries.length);
    }

}
