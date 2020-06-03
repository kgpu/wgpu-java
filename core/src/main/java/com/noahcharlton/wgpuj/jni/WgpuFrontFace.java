package com.noahcharlton.wgpuj.jni;

public enum WgpuFrontFace {

    COUNTER_CLOCKWISE(0),
    CLOCKWISE(1);

    private final int value;

    WgpuFrontFace(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
