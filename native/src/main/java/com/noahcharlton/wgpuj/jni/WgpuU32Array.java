package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

import java.nio.ByteBuffer;

public class WgpuU32Array extends WgpuJavaStruct {

    private final Struct.Pointer pointer = new Struct.Pointer();
    private final Struct.UnsignedLong length = new Struct.UnsignedLong();

    protected WgpuU32Array(ByteBuffer buffer, long bufferLength) {
        useDirectMemory();

        pointer.set(WgpuJava.createByteBufferPointer(buffer));
        length.set(bufferLength);
    }
}
