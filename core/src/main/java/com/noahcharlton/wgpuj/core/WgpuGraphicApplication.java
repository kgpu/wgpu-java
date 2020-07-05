package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.core.graphics.GraphicApplicationSettings;
import com.noahcharlton.wgpuj.core.graphics.RenderPass;
import com.noahcharlton.wgpuj.core.graphics.Window;
import com.noahcharlton.wgpuj.core.graphics.GlfwHandler;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.core.util.ImageData;

import java.io.IOException;

public class WgpuGraphicApplication extends WgpuApplication implements AutoCloseable{

    private static final ImageData defaultWindowIcon = loadDefaultWindowIcon();

    private final Window window;

    private WgpuGraphicApplication(Window window) {
        super(window.getDevice());

        this.window = window;
    }

    public static WgpuGraphicApplication create(GraphicApplicationSettings settings){
        GlfwHandler.init();
        Window window = new Window(settings);
        window.setIcon(defaultWindowIcon);

        return new WgpuGraphicApplication(window);
    }

    public void initializeSwapChain(){
        window.initializeSwapChain();
    }

    public void update(){
        window.update();
    }

    public RenderPass renderStart(Color clearColor){
        return window.renderStart(clearColor);
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
