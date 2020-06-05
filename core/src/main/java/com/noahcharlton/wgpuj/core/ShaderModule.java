package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.jni.WgpuProgrammableStageDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuShaderModuleDescription;

import java.io.IOException;

public class ShaderModule {

    private final long id;
    private final String entryPoint;

    public ShaderModule(byte[] data, String entryPoint, long device) {
        this.id = new WgpuShaderModuleDescription(data).load(device);
        this.entryPoint = entryPoint;
    }

    public WgpuProgrammableStageDescriptor createStageDescriptor(){
        return new WgpuProgrammableStageDescriptor(id, entryPoint);
    }

    public static ShaderModule fromClasspathFile(String path, String entryPoint, long device){
        var inputStream = WgpuShaderModuleDescription.class.getResourceAsStream(path);

        if(inputStream == null){
            throw new RuntimeException("Failed to find shader file: " + path);
        }

        try {
            byte[] bytes =  inputStream.readAllBytes();

            return new ShaderModule(bytes, entryPoint, device);
        } catch(IOException e) {
            throw new RuntimeException("Failed to read shader file " + path, e);
        }
    }

    public long getId() {
        return id;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
