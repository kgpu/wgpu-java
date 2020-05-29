package com.noahcharlton.wgpuj.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWNativeWin32;

public class GlfwHandler {

    public static void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()){
            throw new RuntimeException("Unable to initialize glfw!");
        }

        //Do not use opengl
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);
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
            throw new UnsupportedOperationException("Platform not supported");
        }
    }

}
