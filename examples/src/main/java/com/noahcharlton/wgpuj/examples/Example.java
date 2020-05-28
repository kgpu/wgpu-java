package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.graphics.Window;

public class Example {

    public static void main(String[] args){
        WgpuJava.init();

        System.out.println("Hello: " + WgpuJava.hello("josh"));
        Window window = new Window();

        while(!window.isCloseRequested()){
            window.update();
        }

        WgpuJava.destroy();
    }

}
