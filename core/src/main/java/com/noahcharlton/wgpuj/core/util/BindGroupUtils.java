package com.noahcharlton.wgpuj.core.util;

import com.noahcharlton.wgpuj.jni.*;

public class BindGroupUtils {

    public static WgpuBindGroupLayoutEntry partialLayout(int binding, int visibility, WgpuBindingType type){
        var desc = WgpuBindGroupLayoutEntry.createDirect();
        desc.setBinding(binding);
        desc.setVisibility(visibility);
        desc.setTy(type);

        return desc;
    }

    public static WgpuBindGroupLayoutEntry samplerLayout(int binding, int visibility, WgpuBindingType type,
                                                         boolean comparison){
        var desc = WgpuBindGroupLayoutEntry.createDirect();
        desc.setBinding(binding);
        desc.setVisibility(visibility);
        desc.setTy(type);
        desc.setMultisampled(comparison);

        return desc;
    }

    public static WgpuBindGroupLayoutEntry textureLayout(int binding, int visibility, WgpuBindingType type,
                                                         boolean multiSampled, WgpuTextureViewDimension dimension,
                                                         WgpuTextureComponentType textureComponentType){
        var desc = WgpuBindGroupLayoutEntry.createDirect();
        desc.setBinding(binding);
        desc.setVisibility(visibility);
        desc.setTy(type);
        desc.setMultisampled(multiSampled);
        desc.setViewDimension(dimension);
        desc.setTextureComponentType(textureComponentType);

        return desc;
    }

}
