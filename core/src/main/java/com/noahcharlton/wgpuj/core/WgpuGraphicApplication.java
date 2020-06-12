package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.graphics.SwapChain;
import com.noahcharlton.wgpuj.core.graphics.Window;
import com.noahcharlton.wgpuj.core.graphics.WindowSettings;
import com.noahcharlton.wgpuj.core.util.GlfwHandler;

public class WgpuGraphicApplication implements AutoCloseable{

    private volatile boolean applicationCreated;

    private final Window window;
    private long queue;

    public WgpuGraphicApplication(WindowSettings windowSettings) {
        if(applicationCreated) throw new UnsupportedOperationException("Wgpu-java only supports one application.");
        applicationCreated = true;

        GlfwHandler.init();

        window = new Window(windowSettings);
        queue = WgpuJava.wgpuNative.wgpu_device_get_default_queue(window.getDevice());
    }

    public void init(RenderPipelineSettings settings){
        window.initRenderPipeline(settings);
    }

    public SwapChain renderStart(){
        return window.renderStart();
    }

    @Override
    public void close() {
        window.dispose();
        GlfwHandler.terminate();
    }

    public Window getWindow() {
        return window;
    }

    public long getDevice(){
        return window.getDevice();
    }

    public long getQueue() {
        return queue;
    }
}
