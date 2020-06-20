package com.noahcharlton.wgpuj.core.util;

import com.noahcharlton.wgpuj.jni.Wgpu;

public enum BufferUsage {
    MAP_READ(Wgpu.BufferUsage.MAP_READ),
    MAP_WRITE(Wgpu.BufferUsage.MAP_WRITE),
    COPY_SRC(Wgpu.BufferUsage.COPY_SRC),
    COPY_DST(Wgpu.BufferUsage.COPY_DST),
    INDEX(Wgpu.BufferUsage.INDEX),
    VERTEX(Wgpu.BufferUsage.VERTEX),
    UNIFORM(Wgpu.BufferUsage.UNIFORM),
    STORAGE(Wgpu.BufferUsage.STORAGE),
    INDIRECT(Wgpu.BufferUsage.INDIRECT);

    private final int value;

    BufferUsage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
