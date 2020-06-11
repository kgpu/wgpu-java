package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuOrigin3D extends WgpuJavaStruct {

    private final Struct.Unsigned32 x = new Struct.Unsigned32();
    private final Struct.Unsigned32 y = new Struct.Unsigned32();
    private final Struct.Unsigned32 z = new Struct.Unsigned32();

    public WgpuOrigin3D() {
    }

    public WgpuOrigin3D(int x, int y, int z) {
        useDirectMemory();

        set(x, y, z);
    }

    public void set(int x, int y, int z){
        this.x.set(x);
        this.y.set(y);
        this.z.set(z);
    }
}
