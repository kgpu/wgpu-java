package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.util.GlfwHandler;
import com.noahcharlton.wgpuj.util.SharedLibraryLoader;
import jnr.ffi.LibraryLoader;

public class WgpuJava {

    public static void init(){
        var dll = new SharedLibraryLoader().load("wgpu_native");
        var lib = LibraryLoader.create(WgpuJNI.class).load(dll.getAbsolutePath());

        printVersionString(lib);
        GlfwHandler.init();
    }

    public static void printVersionString(WgpuJNI library){
        int version = library.wgpu_get_version();
        int major = (version >> 16) & 0xFF;
        int minor = (version >> 8) & 0xFF;
        int patch = version & 0xff;

        System.out.printf("wpgu-native: %d.%d.%d\n", major, minor, patch);
    }

    public static void destroy(){
        GlfwHandler.terminate();
    }

}
