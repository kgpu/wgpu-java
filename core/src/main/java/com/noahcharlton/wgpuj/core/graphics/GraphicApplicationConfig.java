package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.core.DeviceConfig;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.system.MemoryUtil.NULL;

public class GraphicApplicationConfig extends DeviceConfig {

    private int width;
    private int height;
    private String title;

    public GraphicApplicationConfig(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public GraphicApplicationConfig() {
        this("", 0, 0);
    }

    long createWindow(){
        return GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
    }

    public GraphicApplicationConfig setWidth(int width) {
        this.width = width;

        return this;
    }

    public GraphicApplicationConfig setHeight(int height) {
        this.height = height;

        return this;
    }

    public GraphicApplicationConfig setTitle(String title) {
        this.title = title;

        return this;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTitle() {
        return title;
    }


}
