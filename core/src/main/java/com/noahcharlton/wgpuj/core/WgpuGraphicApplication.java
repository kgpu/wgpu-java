package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.core.graphics.GraphicApplicationSettings;
import com.noahcharlton.wgpuj.core.graphics.RenderPass;
import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.graphics.Window;
import com.noahcharlton.wgpuj.core.util.GlfwHandler;
import com.noahcharlton.wgpuj.core.util.ImageData;

import java.io.IOException;

public class WgpuGraphicApplication extends WgpuApplication implements AutoCloseable{

    private static final ImageData defaultWindowIcon = loadDefaultWindowIcon();

    private final Window window;

    private WgpuGraphicApplication(GraphicApplicationSettings settings, Window window) {
        super(Device.create(settings, window.getSurface()));

        this.window = window;
    }

    public static WgpuGraphicApplication create(GraphicApplicationSettings settings){
        GlfwHandler.init();
        Window window = new Window(settings);
        window.setIcon(defaultWindowIcon);

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

    private static ImageData loadDefaultWindowIcon() {
        try{
            return ImageData.fromFile("/default_icon.png");
        }catch (IOException e){
            throw new RuntimeException("Failed to load default application icon", e);
        }
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
