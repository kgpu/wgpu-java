package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.graphics.Window;

public class Example {

    public static void main(String[] args){
        WgpuJava.init();

        Window window = new Window();

        while(!window.isCloseRequested()){
            window.update();
        }

        WgpuJava.destroy();
    }

}
