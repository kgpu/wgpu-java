package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuVertexStateDescriptor extends WgpuJavaStruct {

    private final Struct.Enum<WgpuIndexFormat> indexFormat = new Struct.Enum<>(WgpuIndexFormat.class);
    private final Struct.Pointer vertexBuffers = new Struct.Pointer();
    private final Struct.Unsigned64 vertexBufferLength = new Struct.Unsigned64();

    public WgpuVertexStateDescriptor() {

    }

    public void set(WgpuIndexFormat indexFormat, Pointer... vertexBuffers){
        if(vertexBuffers.length > 0){
            throw new UnsupportedOperationException(); //TODO: Actually make this
        }

        this.indexFormat.set(indexFormat.getIntValue());
        this.vertexBuffers.set(WgpuJava.createNullPointer());
        this.vertexBufferLength.set(0);
    }
}
