package com.noahcharlton.wgpuj.core.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class GlfwHandler {

    public static void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()){
            throw new RuntimeException("Unable to initialize glfw!");
        }

        //Do not use opengl
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);
    }

    public static void centerWindow(long handle, Dimension currentDimension) {
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        // Center the window
        GLFW.glfwSetWindowPos(
                handle,
                (vidmode.width() - currentDimension.getWidth()) / 2,
                (vidmode.height() - currentDimension.getHeight()) / 2
        );
    }

    public static Dimension getWindowDimension(long handle){
        try (MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(handle, width, height);

            return new Dimension(width.get(), height.get());
        }
    }

    public static void terminate(){
        GLFW.glfwTerminate();

        var callback = GLFW.glfwSetErrorCallback(null);

        if(callback != null)
            callback.free();
    }

    public static long getOsWindowHandle(long handle){
        if(Platform.isWindows){
            return GLFWNativeWin32.glfwGetWin32Window(handle);
        }else{
            throw new UnsupportedOperationException("Platform not supported. See " +
                    "https://github.com/DevOrc/wgpu-java/issues/4");
        }
    }

}
