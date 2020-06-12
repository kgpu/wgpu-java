package com.noahcharlton.wgpuj.core.util;

import java.io.IOException;
import java.nio.charset.Charset;

public class ClasspathUtil {

    public static byte[] readBytes(String path){
        var inputStream = ClasspathUtil.class.getResourceAsStream(path);

        if(inputStream == null){
            throw new RuntimeException("Failed to find file: " + path);
        }

        try {
            return inputStream.readAllBytes();
        } catch(IOException e) {
            throw new RuntimeException("Failed to read file " + path, e);
        }
    }

    public static String readText(String path, Charset charset){
        byte[] bytes = readBytes(path);

        return new String(bytes, charset);
    }
}
