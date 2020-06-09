package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.RustCString;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuBindGroupDescriptor extends WgpuJavaStruct {

    private final Struct.Pointer label = new Struct.Pointer();
    private final Struct.Unsigned64 layout = new Struct.Unsigned64();
    private final Struct.StructRef<WgpuBindGroupEntry> entries = new StructRef<>(WgpuBindGroupEntry.class);
    private final Struct.UnsignedLong entriesLength = new Struct.UnsignedLong();

    public WgpuBindGroupDescriptor(java.lang.String label, long layout, WgpuBindGroupEntry... entries) {
        useDirectMemory();

        this.label.set(RustCString.toPointer(label));
        this.layout.set(layout);
        this.entries.set(entries);
        this.entriesLength.set(entries.length);
    }
}
