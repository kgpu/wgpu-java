package com.noahcharlton.wgpuj.jni;

public enum WgpuBlendFactor {

    Zero(0),
    One(1),
    SrcColor(2),
    OneMinusSrcColor(3),
    SrcAlpha(4),
    OneMinusSrcAlpha(5),
    DstColor(6),
    OneMinusDstColor(7),
    DstAlpha(8),
    OneMinusDstAlpha(9),
    SrcAlphaSaturated(10),
    BlendColor(11),
    OneMinusBlendColor(12);
    
    private final int value;

    WgpuBlendFactor(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
