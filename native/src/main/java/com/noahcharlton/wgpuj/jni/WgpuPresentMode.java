package com.noahcharlton.wgpuj.jni;

public enum  WgpuPresentMode {

    IMMEDIATE(0),
    MAILBOX(1),
    FIFO(2);

    private final int value;

    WgpuPresentMode(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
