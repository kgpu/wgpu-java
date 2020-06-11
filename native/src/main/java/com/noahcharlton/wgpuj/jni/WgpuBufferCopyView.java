package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuBufferCopyView extends WgpuJavaStruct {

    private final Struct.Unsigned64 buffer = new Struct.Unsigned64();
    private final WgpuTextureDataLayout layout = inner(new WgpuTextureDataLayout());

    public WgpuBufferCopyView(long buffer, long offset, long bytesPerRow, long rowsPerImage) {
        useDirectMemory();

        this.buffer.set(buffer);
        this.layout.set(offset, bytesPerRow, rowsPerImage);
    }
}
