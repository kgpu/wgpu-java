package com.noahcharlton.wgpuj.jni;

public enum WgpuSwapChainStatus {

    GOOD(0),
    SUBOPTIMAL(1),
    TIMEOUT(2),
    OUTDATED(3),
    LOST(4),
    OUT_OF_MEMORY(5);

    private final int value;

    WgpuSwapChainStatus(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
