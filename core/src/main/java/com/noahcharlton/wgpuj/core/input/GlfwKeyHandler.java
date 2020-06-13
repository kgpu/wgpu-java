package com.noahcharlton.wgpuj.core.input;

import com.noahcharlton.wgpuj.core.graphics.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

public class GlfwKeyHandler implements GLFWKeyCallbackI {

    private final Window window;

    public GlfwKeyHandler(Window window) {
        this.window = window;
    }

    @Override
    public void invoke(long windowHandle, int keyCode, int scancode, int action, int mods) {
        Key key = Key.toKey(keyCode);

        if(action == GLFW.GLFW_PRESS){
            window.getEventHandler().onKeyPressed(key);
        }else if(action == GLFW.GLFW_RELEASE){
            window.getEventHandler().onKeyReleased(key);
        }else if(action != GLFW.GLFW_REPEAT){
            throw new UnsupportedOperationException("Unknown key action: " + action);
        }
    }
}
