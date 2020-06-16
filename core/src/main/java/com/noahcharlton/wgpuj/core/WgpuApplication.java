package com.noahcharlton.wgpuj.core;

public class WgpuApplication {

    protected final Device device;
    protected final long queue;

    public WgpuApplication(Device device) {
        this.device = device;
        this.queue = device.getDefaultQueue();
    }

    public long getQueue() {
        return queue;
    }

    public Device getDevice() {
        return device;
    }
}
