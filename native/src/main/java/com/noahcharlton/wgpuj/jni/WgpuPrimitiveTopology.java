package com.noahcharlton.wgpuj.jni;

public enum WgpuPrimitiveTopology {

    PointList(0),
    LineList(1),
    LineString(2),
    TriangleList(3),
    TriangleStrip(4);

    private final int value;

    WgpuPrimitiveTopology(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }

}
