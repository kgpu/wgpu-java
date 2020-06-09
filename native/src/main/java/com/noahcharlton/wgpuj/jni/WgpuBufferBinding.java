package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;

public class WgpuBufferBinding extends WgpuJavaStruct {

    private final Unsigned64 id = new Unsigned64();
    private final Unsigned64 offset = new Unsigned64();
    private final Unsigned64 size = new Unsigned64();

    public void set(long id, long offset, long size){
        this.id.set(id);
        this.offset.set(offset);
        this.size.set(size);
    }
}
