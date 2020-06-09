package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuBindGroupLayoutEntry extends WgpuJavaStruct {

    public static final int SHADER_STAGE_NONE = 0;
    public static final int SHADER_STAGE_VERTEX = 1;
    public static final int SHADER_STAGE_FRAGMENT = 2;
    public static final int SHADER_STAGE_COMPUTE = 4;

    private final Struct.Unsigned32 binding = new Struct.Unsigned32();
    private final Struct.Unsigned32 visibility = new Struct.Unsigned32();
    private final Struct.Enum<WgpuBindingType> bindingType = new Struct.Enum<>(WgpuBindingType.class);
    private final Struct.Boolean multiSampled = new Struct.Boolean();
    private final Struct.Boolean hasDynamicOffset = new Struct.Boolean();
    private final Struct.Enum<WgpuTextureViewDimension> viewDimension
            = new Struct.Enum<>(WgpuTextureViewDimension.class);
    private final Struct.Enum<WgpuTextureComponentType> textureComponentType
            = new Struct.Enum<>(WgpuTextureComponentType.class);
    private final Struct.Enum<WgpuTextureFormat> storageTextureFormat = new Struct.Enum<>(WgpuTextureFormat.class);

    public WgpuBindGroupLayoutEntry(Runtime runtime) {
        super(runtime);
    }

    public WgpuBindGroupLayoutEntry() {
        useDirectMemory();
    }

    public void setPartial(int binding, int visibility, WgpuBindingType bindingType){
        this.binding.set(binding);
        this.visibility.set(visibility);
        this.bindingType.set(bindingType);
    }
}
