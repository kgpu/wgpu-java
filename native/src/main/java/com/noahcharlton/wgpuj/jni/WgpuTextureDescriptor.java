package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.RustCString;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuTextureDescriptor extends WgpuJavaStruct {

    public static final int COPY_SRC = 1;
    public static final int COPY_DST = 2;
    public static final int SAMPLED = 4;
    public static final int STORAGE = 8;
    public static final int OUTPUT_ATTACHMENT = 16;

    private final Struct.Pointer label = new Struct.Pointer();
    private final WgpuExtent3d size = inner(new WgpuExtent3d());
    private final Struct.Unsigned32 mipLevelCount = new Struct.Unsigned32();
    private final Struct.Unsigned32 sampleCount = new Struct.Unsigned32();
    private final Struct.Enum<WgpuTextureDimension> dimension = new Struct.Enum<>(WgpuTextureDimension.class);
    private final Struct.Enum<WgpuTextureFormat> format = new Struct.Enum<>(WgpuTextureFormat.class);
    private final Struct.Unsigned32 usage = new Struct.Unsigned32();

    public WgpuTextureDescriptor() {
        useDirectMemory();
    }

    public WgpuTextureDescriptor set(java.lang.String label, int width, int height, int depth, int mipLevelCount, int sampleCount,
                    WgpuTextureDimension dimension, WgpuTextureFormat format, int usage){

        this.label.set(RustCString.toPointer(label));
        this.size.set(width, height, depth);
        this.mipLevelCount.set(mipLevelCount);
        this.sampleCount.set(sampleCount);
        this.dimension.set(dimension);
        this.format.set(format);
        this.usage.set(usage);

        return this;
    }
}
