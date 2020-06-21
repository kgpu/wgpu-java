package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuRasterizationStateDescriptor;

public class RasterizationState {

    public static WgpuRasterizationStateDescriptor of(
            WgpuFrontFace face,
            WgpuCullMode cullMode,
            int depthBias,
            float depthBiasSlopeScale,
            float depthBiasClamp){
        var desc = WgpuRasterizationStateDescriptor.createDirect();

        desc.setFrontFace(face);
        desc.setCullMode(cullMode);
        desc.setDepthBias(depthBias);
        desc.setDepthBiasSlopeScale(depthBiasSlopeScale);
        desc.setDepthBiasClamp(depthBiasClamp);

        return desc;
    }
}
