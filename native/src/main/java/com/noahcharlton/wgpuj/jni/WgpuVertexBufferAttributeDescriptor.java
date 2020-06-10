package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuVertexBufferAttributeDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 offset = new Struct.Unsigned64();
    private final Struct.Enum<WgpuVertexFormat> format = new Struct.Enum<>(WgpuVertexFormat.class);
    private final Struct.Unsigned32 shaderLocation = new Struct.Unsigned32();

    public WgpuVertexBufferAttributeDescriptor(long offset, WgpuVertexFormat format, int location) {
        useDirectMemory();

        this.offset.set(offset);
        this.format.set(format);
        this.shaderLocation.set(location);
    }

    public WgpuVertexBufferAttributeDescriptor(Runtime runtime) {
        super(runtime);
    }
}
