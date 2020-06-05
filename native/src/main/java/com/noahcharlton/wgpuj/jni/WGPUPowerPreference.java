package com.noahcharlton.wgpuj.jni;

public enum WGPUPowerPreference{

    DEFAULT(0),
    LOW(1),
    PERFORMANCE(2);

    private final int value;

    WGPUPowerPreference(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
