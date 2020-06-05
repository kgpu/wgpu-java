package com.noahcharlton.wgpuj.jni;

public enum  WgpuLoadOp {

    CLEAR(0),
    LOAD(1);

    private final int value;

    WgpuLoadOp(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
