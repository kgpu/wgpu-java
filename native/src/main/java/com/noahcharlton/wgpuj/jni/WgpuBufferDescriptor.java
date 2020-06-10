package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.RustCString;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuBufferDescriptor extends WgpuJavaStruct {

    public static final int MAP_READ = 1;
    public static final int MAP_WRITE = 2;
    public static final int COPY_SRC = 4;
    public static final int COPY_DST = 8;
    public static final int INDEX = 16;
    public static final int VERTEX = 32;
    public static final int UNIFORM = 64;
    public static final int STORAGE = 128;
    public static final int INDIRECT = 256;

    private final Struct.Pointer label = new Struct.Pointer();
    private final Unsigned64 size = new Unsigned64();
    private final Unsigned32 usage = new Unsigned32();
    private final Struct.Boolean mappedAtCreation = new Struct.Boolean();

    public WgpuBufferDescriptor(java.lang.String label, long size, int usage, boolean mappedAtCreation) {
        useDirectMemory();

        this.label.set(RustCString.toPointer(label));
        this.size.set(size);
        this.usage.set(usage);
        this.mappedAtCreation.set(mappedAtCreation);
    }

    public Unsigned32 getUsage() {
        return usage;
    }
}
