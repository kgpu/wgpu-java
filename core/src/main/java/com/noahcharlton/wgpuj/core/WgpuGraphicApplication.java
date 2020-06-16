package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.core.graphics.GraphicApplicationSettings;
import com.noahcharlton.wgpuj.core.graphics.RenderPass;
import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.graphics.Window;
import com.noahcharlton.wgpuj.core.util.GlfwHandler;

public class WgpuGraphicApplication extends WgpuApplication implements AutoCloseable{

    private final Window window;

    private WgpuGraphicApplication(GraphicApplicationSettings settings, Window window) {
        super(Device.create(settings, window.getSurface()));

        this.window = window;
    }

    public static WgpuGraphicApplication create(GraphicApplicationSettings settings){
        GlfwHandler.init();
        Window window = new Window(settings);

        return new WgpuGraphicApplication(settings, window);
    }

    public void init(RenderPipelineSettings settings){
        window.initRenderPipeline(settings, device);
    }

    public RenderPass renderStart(){
        return window.renderStart(device);
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
}
