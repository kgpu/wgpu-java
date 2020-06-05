package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.core.GlfwHandler;
import com.noahcharlton.wgpuj.core.SharedLibraryLoader;
import com.noahcharlton.wgpuj.core.Window;

public class Example {

    public static void main(String[] args){
        SharedLibraryLoader.loadWgpuNative();
        GlfwHandler.init();

        Window window = new Window();

        while(!window.isCloseRequested()){
            window.update();
        }

        window.dispose();
        GlfwHandler.terminate();
    }

}
