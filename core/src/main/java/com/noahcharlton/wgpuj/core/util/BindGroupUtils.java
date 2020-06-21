package com.noahcharlton.wgpuj.core.util;

import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindingType;
import com.noahcharlton.wgpuj.jni.WgpuTextureComponentType;
import com.noahcharlton.wgpuj.jni.WgpuTextureViewDimension;

public class BindGroupUtils {

    public static WgpuBindGroupLayoutEntry partialLayout(int binding, int visibility, WgpuBindingType type){
        var desc = WgpuBindGroupLayoutEntry.createDirect();
        desc.setBinding(binding);
        desc.setVisibility(visibility);
        desc.setTy(type);

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
