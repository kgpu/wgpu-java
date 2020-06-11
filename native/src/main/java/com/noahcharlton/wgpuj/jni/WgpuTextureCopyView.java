package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuTextureCopyView extends WgpuJavaStruct {

    private final Struct.Unsigned64 texture = new Struct.Unsigned64();
    private final Struct.Unsigned32 mipLevel = new Struct.Unsigned32();
    private final WgpuOrigin3D origin = inner(new WgpuOrigin3D());

    public WgpuTextureCopyView(long texture, long mipLevel, int x, int y, int z) {
        useDirectMemory();

        this.texture.set(texture);
        this.mipLevel.set(mipLevel);
        this.origin.set(x, y, z);
    }
}
