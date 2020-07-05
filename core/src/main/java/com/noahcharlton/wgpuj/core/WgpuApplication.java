package com.noahcharlton.wgpuj.core;

public class WgpuApplication {

    protected final Device device;
    protected final Queue defaultQueue;

    public WgpuApplication(Device device) {
        this.device = device;
        this.defaultQueue = device.getDefaultQueue();
    }

    public Queue getDefaultQueue() {
        return defaultQueue;
    }

    public Device getDevice() {
        return device;
    }
}
