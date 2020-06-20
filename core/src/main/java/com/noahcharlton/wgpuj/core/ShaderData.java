package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.ClasspathUtil;
import com.noahcharlton.wgpuj.core.util.ShaderCompiler;
import com.noahcharlton.wgpuj.jni.WgpuProgrammableStageDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuShaderModuleDescriptor;

import java.nio.charset.StandardCharsets;

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

    public WgpuProgrammableStageDescriptor build(Device device){
        long shaderModule = createModule(device);

        return new WgpuProgrammableStageDescriptor(shaderModule, entryPoint);
    }

    public long createModule(Device device) {
        var shaderModule = WgpuShaderModuleDescriptor.createDirect();
        var dataPtr = WgpuJava.createByteArrayPointer(data);

        shaderModule.getCode().setBytes(dataPtr);
        shaderModule.getCode().setLength(data.length / 4);

        return WgpuJava.wgpuNative.wgpu_device_create_shader_module(device.getId(), shaderModule.getPointerTo());
    }

    public static ShaderData fromCompiledClasspathFile(String path, String entryPoint){
        return new ShaderData(entryPoint, ClasspathUtil.readBytes(path));
    }

    public static ShaderData fromRawClasspathFile(String path, String entryPoint){
        int type;

        if(path.endsWith(".vert")){
            type = ShaderCompiler.VERTEX;
        }else if(path.endsWith(".frag")){
            type = ShaderCompiler.FRAGMENT;
        }else if(path.endsWith(".comp")){
            type = ShaderCompiler.COMPUTE;
        }else{
            throw new IllegalArgumentException("Path must have extension .frag, .vert, or .comp. You may also use " +
                    "ShaderData.fromRawClasspathFile(String path, String entryPoint, int type)");
        }

        return fromRawClasspathFile(path, entryPoint, type);
    }

    public static ShaderData fromRawClasspathFile(String path, String entryPoint, int type){
        String text = ClasspathUtil.readText(path, StandardCharsets.UTF_8);

        return new ShaderData(entryPoint, ShaderCompiler.compile(text, type));
    }

    public byte[] getData() {
        return data;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
