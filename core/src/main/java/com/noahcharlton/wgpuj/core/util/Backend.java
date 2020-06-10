package com.noahcharlton.wgpuj.core.util;

public enum Backend {

    EMPTY(0),
    VULKAN(1 << 1),
    METAL(1 << 2),
    DX12(1 << 3),
    DX11(1 << 4),
    GL(1 << 5);

    private final int value;

    Backend(int value) {
        this.value = value;
    }

    public static int of(Backend... backends){
        int output = 0;

        for(Backend backend : backends){
            output = output | backend.value;
        }

        return output;
    }
}
