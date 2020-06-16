package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.core.util.Backend;

public class DeviceSettings {

    private int backend = Backend.of(Backend.DX12, Backend.VULKAN, Backend.METAL);

    public DeviceSettings setBackend(Backend... backends) {
        this.backend = Backend.of(backends);

        return this;
    }

    public int getBackend() {
        return backend;
    }
}
