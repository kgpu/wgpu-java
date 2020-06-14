package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuVertexStateDescriptor extends WgpuJavaStruct {

    private final Struct.Enum<WgpuIndexFormat> indexFormat = new Struct.Enum<>(WgpuIndexFormat.class);
    private final Struct.StructRef<WgpuVertexBufferLayoutDescriptor> vertexBuffers
            = new Struct.StructRef<>(WgpuVertexBufferLayoutDescriptor.class);
    private final Struct.Unsigned64 vertexBufferLength = new Struct.Unsigned64();

    public WgpuVertexStateDescriptor() {

    }

    public void set(WgpuIndexFormat indexFormat, WgpuVertexBufferLayoutDescriptor... vertexBuffers){
        this.indexFormat.set(indexFormat);
        this.vertexBuffers.set(vertexBuffers);
        this.vertexBufferLength.set(vertexBuffers.length);
    }
}
