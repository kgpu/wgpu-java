package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.core.util.Backend;

public class DeviceSettings {

    private int backend = Backend.of(Backend.DX12, Backend.VULKAN, Backend.METAL);
    private String tracePath;
    private int extensions;

    public DeviceSettings setBackend(Backend... backends) {
        this.backend = Backend.of(backends);

        return this;
    }

    public int getBackend() {
        return backend;
    }

    public void setExtensions(int extensions) {
        this.extensions = extensions;
    }

    public int getExtensions() {
        return extensions;
    }

    public void setTracePath(String tracePath) {
        this.tracePath = tracePath;
    }

    public String getTracePath() {
        return tracePath;
    }
}
