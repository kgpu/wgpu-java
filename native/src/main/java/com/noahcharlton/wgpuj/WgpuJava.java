package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.jni.LogCallback;
import com.noahcharlton.wgpuj.jni.WgpuJNI;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;

import java.io.File;
import java.nio.ByteBuffer;

public class WgpuJava {

    public static WgpuJNI wgpuNative;
    private static Runtime runtime;

    public static void init(File nativeFile){
        var loader = LibraryLoader.create(WgpuJNI.class);

        wgpuNative = loader.load(nativeFile.getAbsolutePath());
        runtime = Runtime.getRuntime(wgpuNative);

        setupLog();
    }

    private static void setupLog() {
        wgpuNative.wgpu_set_log_callback(LogCallback.createDefault());
        wgpuNative.wgpu_set_log_level(WgpuLogLevel.WARN);
    }

    private static String getWgpuNativeVersion(){
        int version = wgpuNative.wgpu_get_version();
        int major = (version >> 16) & 0xFF;
        int minor = (version >> 8) & 0xFF;
        int patch = version & 0xff;

        return String.format("%d.%d.%d", major, minor, patch);
    }

    public static Pointer createLongPointer(long value){
        return Pointer.wrap(runtime, value);
    }

    public static Pointer createDirectLongPointer(long value){
        var pointer = createDirectPointer(Long.BYTES);
        pointer.putLongLong(0, value);

        return pointer;
    }


    public static Pointer createNullPointer(){
        return Pointer.wrap(runtime, 0x00);
    }

    public static Pointer createByteBufferPointer(ByteBuffer buffer) {
        if(!buffer.isDirect()){
            throw new IllegalArgumentException("Buffer must be direct!");
        }

        if(buffer.position() > 0){
            throw new IllegalArgumentException("Buffer should have a position of zero!");
        }

        return Pointer.wrap(runtime, buffer);
    }

    public static Pointer createDirectPointer(int size) {
        return runtime.getMemoryManager().allocateDirect(size);
    }

    public static Runtime getRuntime() {
        return runtime;
    }
}
