package com.noahcharlton.wgpuj.core.graphics;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowSettings {

    private final int width;
    private final int height;
    private final String title;

    public WindowSettings(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    long createWindow(){
        return GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
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
