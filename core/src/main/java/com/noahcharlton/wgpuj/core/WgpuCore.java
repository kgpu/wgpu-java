package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.SharedLibraryLoader;

import java.io.File;

public class WgpuCore {

    private static final File file = new SharedLibraryLoader().load("wgpu_native");

    public static void loadWgpuNative(){
        WgpuJava.init(file);
    }

    static File getLibraryFile() {
        return file;
    }
}
