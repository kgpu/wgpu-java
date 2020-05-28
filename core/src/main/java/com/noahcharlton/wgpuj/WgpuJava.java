package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.util.SharedLibraryLoader;

public class WgpuJava {

    public static native String hello(String input);

    static {
        var loader = new SharedLibraryLoader();

        loader.load("wgpu_jni");
    }

}
