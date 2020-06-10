package com.noahcharlton.wgpuj.core.util;

public enum BufferUsage {
    MAP_READ(1),
    MAP_WRITE(2),
    COPY_SRC(4),
    COPY_DST(8),
    INDEX(16),
    VERTEX(32),
    UNIFORM(64),
    STORAGE(128),
    INDIRECT(256);

    private final int value;

    BufferUsage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
