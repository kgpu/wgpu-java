package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.graphics.Window;
import com.noahcharlton.wgpuj.core.util.GlfwHandler;
import com.noahcharlton.wgpuj.core.util.SharedLibraryLoader;

import java.io.File;

public class WgpuApplication implements AutoCloseable{

    private volatile boolean applicationCreated;

    private final Window window;

    public WgpuApplication() {
        if(applicationCreated) throw new UnsupportedOperationException("Wgpu-java only supports one application.");
        applicationCreated = true;

        GlfwHandler.init();
        loadWgpuNative();

        window = new Window();
    }

    private static void loadWgpuNative(){
        File file = new SharedLibraryLoader().load("wgpu_native");

        WgpuJava.init(file);
    }

    public void update(){
        window.update();
    }

    @Override
    public void close() {
        GlfwHandler.terminate();
    }

    public Window getWindow() {
        return window;
    }
}
