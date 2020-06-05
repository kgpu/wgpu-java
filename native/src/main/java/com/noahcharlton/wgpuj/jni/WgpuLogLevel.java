package com.noahcharlton.wgpuj.jni;

public enum WgpuLogLevel {

    OFF(0),
    ERROR(1),
    WARN(2),
    INFO(3),
    DEBUG(4),
    TRACE(5);

    private final int value;

    WgpuLogLevel(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }

    public long getLongValue() {
        return value;
    }
}
