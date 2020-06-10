package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.graphics.SwapChain;
import com.noahcharlton.wgpuj.core.graphics.Window;
import com.noahcharlton.wgpuj.core.graphics.WindowSettings;
import com.noahcharlton.wgpuj.core.util.GlfwHandler;

public class WgpuGraphicApplication implements AutoCloseable{

    private volatile boolean applicationCreated;

    private final Window window;

    public WgpuGraphicApplication(RenderPipelineSettings renderSettings, WindowSettings windowSettings) {
        if(applicationCreated) throw new UnsupportedOperationException("Wgpu-java only supports one application.");
        applicationCreated = true;

        GlfwHandler.init();

        window = new Window(renderSettings, windowSettings);
    }

    public SwapChain renderStart(){
        return window.renderStart();
    }

    public void renderEnd(){
        window.renderEnd();
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
}
