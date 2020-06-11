package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuTextureDataLayout extends WgpuJavaStruct {

    private final Struct.Unsigned64 offset = new Struct.Unsigned64();
    private final Struct.Unsigned32 bytesPerRow = new Struct.Unsigned32();
    private final Struct.Unsigned32 rowsPerImage = new Struct.Unsigned32();

    public void set(long offset, long bytesPerRow, long rowsPerImage){
        this.offset.set(offset);
        this.bytesPerRow.set(bytesPerRow);
        this.rowsPerImage.set(rowsPerImage);
    }
}
