package com.noahcharlton.wgpuj.jni;

public enum WgpuPowerPreference {

    DEFAULT(0),
    LOW(1),
    PERFORMANCE(2);

    private final int value;

    WgpuPowerPreference(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
