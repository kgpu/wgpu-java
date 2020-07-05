package com.noahcharlton.wgpuj.core.util;

import java.nio.charset.StandardCharsets;

public class ShaderConfig {

    private final byte[] data;
    private final String entryPoint;

    public ShaderConfig( String entryPoint, byte[] data) {
        this.data = data;
        this.entryPoint = entryPoint;
    }

    public static ShaderConfig fromCompiledClasspathFile(String path, String entryPoint){
        return new ShaderConfig(entryPoint, ClasspathUtil.readBytes(path));
    }

    public static ShaderConfig fromRawClasspathFile(String path, String entryPoint){
        int type;

        if(path.endsWith(".vert")){
            type = ShaderCompiler.VERTEX;
        }else if(path.endsWith(".frag")){
            type = ShaderCompiler.FRAGMENT;
        }else if(path.endsWith(".comp")){
            type = ShaderCompiler.COMPUTE;
        }else{
            throw new IllegalArgumentException("Path must have extension .frag, .vert, or .comp. You may also use " +
                    "ShaderConfig.fromRawClasspathFile(String path, String entryPoint, int type)");
        }

        return fromRawClasspathFile(path, entryPoint, type);
    }

    public static ShaderConfig fromRawClasspathFile(String path, String entryPoint, int type){
        String text = ClasspathUtil.readText(path, StandardCharsets.UTF_8);

        return new ShaderConfig(entryPoint, ShaderCompiler.compile(path, text, type));
    }

    public byte[] getData() {
        return data;
    }

    public String getEntryPoint() {
        return entryPoint;
    }
}
