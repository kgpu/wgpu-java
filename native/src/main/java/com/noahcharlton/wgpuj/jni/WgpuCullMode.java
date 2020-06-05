package com.noahcharlton.wgpuj.jni;

public enum WgpuCullMode {

    NONE(0),
    FRONT(1),
    BACK(2);

    private final int value;

    WgpuCullMode(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
