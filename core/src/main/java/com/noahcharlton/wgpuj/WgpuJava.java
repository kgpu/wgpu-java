package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.jni.LogCallback;
import com.noahcharlton.wgpuj.jni.WgpuJNI;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import com.noahcharlton.wgpuj.util.GlfwHandler;
import com.noahcharlton.wgpuj.util.SharedLibraryLoader;
import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;

import java.nio.ByteBuffer;

public class WgpuJava {

    public static WgpuJNI wgpuNative;
    private static Runtime runtime;

    public static void init(){
        var dll = new SharedLibraryLoader().load("wgpu_native");
        var loader = LibraryLoader.create(WgpuJNI.class);

        wgpuNative = loader.load(dll.getAbsolutePath());
        runtime = Runtime.getRuntime(wgpuNative);

        printVersionString();
        setupLog();
        GlfwHandler.init();
    }

    private static void setupLog() {
        //Trace level
        wgpuNative.wgpu_set_log_callback(LogCallback.createDefault());
        wgpuNative.wgpu_set_log_level(WgpuLogLevel.WARN);
    }

    private static void printVersionString(){
        int version = wgpuNative.wgpu_get_version();
        int major = (version >> 16) & 0xFF;
        int minor = (version >> 8) & 0xFF;
        int patch = version & 0xff;

        System.out.printf("wpgu-native: %d.%d.%d\n", major, minor, patch);
    }

    public static void destroy(){
        GlfwHandler.terminate();
    }

    public static Pointer createLongPointer(long value){
        return Pointer.wrap(runtime, value);
    }

    public static Pointer createNullPointer(){
        return Pointer.wrap(WgpuJava.getRuntime(), ByteBuffer.allocate(0));
    }

    public static Runtime getRuntime() {
        return runtime;
    }
}
