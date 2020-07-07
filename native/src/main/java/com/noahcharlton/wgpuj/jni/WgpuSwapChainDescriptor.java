package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

/** NOTE: THIS FILE WAS PRE-GENERATED BY JNR_GEN! */
public class WgpuSwapChainDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned32 usage = new Struct.Unsigned32();
    private final Struct.Enum<WgpuTextureFormat> format = new Struct.Enum<>(WgpuTextureFormat.class);
    private final Struct.Unsigned32 width = new Struct.Unsigned32();
    private final Struct.Unsigned32 height = new Struct.Unsigned32();
    private final Struct.Enum<WgpuPresentMode> presentMode = new Struct.Enum<>(WgpuPresentMode.class);

    private WgpuSwapChainDescriptor(){}

    @Deprecated
    public WgpuSwapChainDescriptor(Runtime runtime){
        super(runtime);
    }

    /**
    * Creates this struct on the java heap.
    * In general, this should <b>not</b> be used because these structs
    * cannot be directly passed into native code. 
    */
    public static WgpuSwapChainDescriptor createHeap(){
        return new WgpuSwapChainDescriptor();
    }

    /**
    * Creates this struct in direct memory.
    * This is how most structs should be created (unless, they
    * are members of a nothing struct)
    * 
    * @see WgpuJavaStruct#useDirectMemory
    */
    public static WgpuSwapChainDescriptor createDirect(){
        var struct = new WgpuSwapChainDescriptor();
        struct.useDirectMemory();
        return struct;
    }


    public long getUsage(){
        return usage.get();
    }

    public void setUsage(long x){
        this.usage.set(x);
    }

    public WgpuTextureFormat getFormat(){
        return format.get();
    }

    public void setFormat(WgpuTextureFormat x){
        this.format.set(x);
    }

    public long getWidth(){
        return width.get();
    }

    public void setWidth(long x){
        this.width.set(x);
    }

    public long getHeight(){
        return height.get();
    }

    public void setHeight(long x){
        this.height.set(x);
    }

    public WgpuPresentMode getPresentMode(){
        return presentMode.get();
    }

    public void setPresentMode(WgpuPresentMode x){
        this.presentMode.set(x);
    }

}