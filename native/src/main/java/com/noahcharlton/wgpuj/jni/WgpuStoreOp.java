package com.noahcharlton.wgpuj.jni;

public enum WgpuStoreOp {

    CLEAR(0),
    STORE(1);

    private final int value;

    WgpuStoreOp(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
