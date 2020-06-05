package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;

import java.io.IOException;
import java.nio.ByteBuffer;

public class WgpuShaderModuleDescription extends WgpuJavaStruct {

    private final WgpuU32Array code;

    private WgpuShaderModuleDescription(byte[] data) {
        useDirectMemory();

        var buffer = ByteBuffer.allocateDirect(data.length).put(data).position(0);
        var length = data.length / 4;

        code = inner(new WgpuU32Array(buffer, length));
    }

    public long load(long device) {
        return WgpuJava.wgpuNative.wgpu_device_create_shader_module(device, this.getPointerTo());
    }

    public static WgpuShaderModuleDescription fromFile(java.lang.String name){
        java.lang.String path = "/shaders/" + name + ".spv";
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
