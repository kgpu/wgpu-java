package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.core.WgpuApplication;

public class Example {

    public static void main(String[] args){
        try(WgpuApplication application = new WgpuApplication()){
            while(!application.getWindow().isCloseRequested()){
                application.update();
            }
        }
    }

}
