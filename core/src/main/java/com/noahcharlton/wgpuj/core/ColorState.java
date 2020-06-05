package com.noahcharlton.wgpuj.core;

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
        return new WgpuColorStateDescriptor(
                format,
                alpha.getSrc(),
                alpha.getDest(),
                alpha.getOperation(),
                color.getSrc(),
                color.getDest(),
                color.getOperation(),
                writeMask);
    }
}
