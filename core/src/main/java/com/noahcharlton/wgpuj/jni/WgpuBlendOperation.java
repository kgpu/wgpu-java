package com.noahcharlton.wgpuj.jni;

public enum WgpuBlendOperation {

    ADD(0),
    SUBTRACT(0),
    REVERSE_SUBTRACT(0),
    MIN(3),
    MAX(4);

    private final int value;

    WgpuBlendOperation(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
