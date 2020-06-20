package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;

public class ColorState {

    private final WgpuTextureFormat format;
    private final BlendDescriptor alpha;
    private final BlendDescriptor color;
    private final int writeMask;

    public ColorState(WgpuTextureFormat format, BlendDescriptor alpha, BlendDescriptor color, int writeMask) {
        this.format = format;
        this.alpha = alpha;
        this.color = color;
        this.writeMask = writeMask;
    }

    public WgpuColorStateDescriptor build(){
        var desc = WgpuColorStateDescriptor.createDirect();
        desc.setFormat(format);
        desc.setWriteMask(writeMask);
        alpha.applyTo(desc.getAlphaBlend());
        color.applyTo(desc.getColorBlend());

        return desc;
    }
}
