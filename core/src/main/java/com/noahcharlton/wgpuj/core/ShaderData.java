package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.jni.WgpuProgrammableStageDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuShaderModuleDescription;

import java.io.IOException;

/**
 * A container for a shader before it is loaded into
 * Wgpu.
 */
public class ShaderData {

    private final String entryPoint;
    private final byte[] data;

    public ShaderData(String entryPoint, byte[] data) {
        this.entryPoint = entryPoint;
        this.data = data;
    }

    public WgpuProgrammableStageDescriptor build(long device){
        long shaderModule = createModule(device);

        return new WgpuProgrammableStageDescriptor(shaderModule, entryPoint);
    }

    public long createModule(long device) {
        return new WgpuShaderModuleDescription(data).load(device);
    }

    public static ShaderData fromClasspathFile(String path, String entryPoint){
        var inputStream = WgpuShaderModuleDescription.class.getResourceAsStream(path);

        if(inputStream == null){
            throw new RuntimeException("Failed to find shader file: " + path);
        }

        try {
            byte[] bytes =  inputStream.readAllBytes();

            return new ShaderData(entryPoint, bytes);
        } catch(IOException e) {
            throw new RuntimeException("Failed to read shader file " + path, e);
        }
    }

    public byte[] getData() {
        return data;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
