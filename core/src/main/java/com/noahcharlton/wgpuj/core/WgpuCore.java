package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.SharedLibraryLoader;

import java.io.File;

public class WgpuCore {

    public static void loadWgpuNative(){
        File file = new SharedLibraryLoader().load("wgpu_native");

        WgpuJava.init(file);
    }
}
