package com.noahcharlton.wgpuj.jni;

public enum WgpuIndexFormat {
    Uint16(0),
    Uint32(1);

    private final int value;

    WgpuIndexFormat(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
