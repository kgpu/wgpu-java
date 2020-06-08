package com.noahcharlton.wgpuj.jni;

public enum WgpuBlendFactor {

    ZERO(0),
    ONE(1),
    SRC_COLOR(2),
    ONE_MINUS_SRC_COLOR(3),
    SRC_ALPHA(4),
    ONE_MINUS_SRC_ALPHA(5),
    DST_COLOR(6),
    ONE_MINUS_DST_COLOR(7),
    DST_ALPHA(8),
    ONE_MINUS_DST_ALPHA(9),
    SRC_ALPHA_SATURATED(10),
    BLEND_COLOR(11),
    ONE_MINUS_BLEND_COLOR(12);
    
    private final int value;

    WgpuBlendFactor(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
