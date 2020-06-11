package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.RustCString;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuTextureViewDescriptor extends WgpuJavaStruct {

    private final Struct.Pointer label = new Struct.Pointer();
    private final Struct.Enum<WgpuTextureFormat> format = new Struct.Enum<>(WgpuTextureFormat.class);
    private final Struct.Enum<WgpuTextureViewDimension> dimension = new Struct.Enum<>(WgpuTextureViewDimension.class);
    private final Struct.Enum<WgpuTextureAspect> aspect = new Struct.Enum<>(WgpuTextureAspect.class);
    private final Struct.Unsigned32 baseMipLevel = new Struct.Unsigned32();
    private final Struct.Unsigned32 levelCount = new Struct.Unsigned32();
    private final Struct.Unsigned32 baseArrayLayer = new Struct.Unsigned32();
    private final Struct.Unsigned32 arrayLayerCount = new Struct.Unsigned32();

    public WgpuTextureViewDescriptor() {
        useDirectMemory();
    }

    public WgpuTextureViewDescriptor set(java.lang.String label, WgpuTextureFormat format,
                                         WgpuTextureViewDimension dimension, WgpuTextureAspect aspect, int baseMipLevel,
                                         int levelCount, int baseArrayLayer, int arrayLayerCount){

        this.label.set(RustCString.toPointer(label));
        this.format.set(format);
        this.dimension.set(dimension);
        this.aspect.set(aspect);
        this.baseMipLevel.set(baseMipLevel);
        this.levelCount.set(levelCount);
        this.baseArrayLayer.set(baseArrayLayer);
        this.arrayLayerCount.set(arrayLayerCount);

        return this;
    }
}
