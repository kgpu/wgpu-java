package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.core.DeviceSettings;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.system.MemoryUtil.NULL;

public class GraphicApplicationSettings extends DeviceSettings {

    private int width;
    private int height;
    private String title;

    public GraphicApplicationSettings(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    long createWindow(){
        return GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
    }

    public GraphicApplicationSettings setWidth(int width) {
        this.width = width;

        return this;
    }

    public GraphicApplicationSettings setHeight(int height) {
        this.height = height;

        return this;
    }

    public GraphicApplicationSettings setTitle(String title) {
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
