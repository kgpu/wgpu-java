package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;

import java.io.IOException;
import java.nio.ByteBuffer;

public class WgpuShaderModuleDescription {

    private final ByteBuffer byteBuffer;
    private final int length;

    private WgpuShaderModuleDescription(byte[] data) {
        byteBuffer = ByteBuffer.allocateDirect(data.length).put(data).position(0);
        this.length = data.length / 4;
    }

    public long load(long device){
        var array = WgpuStructs.createWgpuU32Array(byteBuffer, length);

        return WgpuJava.wgpuNative.wgpu_device_create_shader_module(device, array);
    }

    public static WgpuShaderModuleDescription fromFile(String name){
        String path = "/shaders/" + name + ".spv";
        var inputStream = WgpuShaderModuleDescription.class.getResourceAsStream(path);

        if(inputStream == null){
            throw new RuntimeException("Failed to find shader file: " + name);
        }

        try {
            byte[] bytes =  inputStream.readAllBytes();

            return new WgpuShaderModuleDescription(bytes);
        } catch(IOException e) {
            throw new RuntimeException("Failed to read shader file " + path, e);
        }
    }
}
