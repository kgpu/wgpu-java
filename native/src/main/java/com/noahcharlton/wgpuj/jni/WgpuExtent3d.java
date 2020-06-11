package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuExtent3d extends WgpuJavaStruct {

    private final Struct.Unsigned32 width = new Struct.Unsigned32();
    private final Struct.Unsigned32 height = new Struct.Unsigned32();
    private final Struct.Unsigned32 depth = new Struct.Unsigned32();

    public WgpuExtent3d() {

    }

    public WgpuExtent3d(int width, int height, int depth) {
        useDirectMemory();

        set(width, height, depth);
    }

    public void set(int width, int height, int depth){
        this.width.set(width);
        this.height.set(height);
        this.depth.set(depth);
    }

}
