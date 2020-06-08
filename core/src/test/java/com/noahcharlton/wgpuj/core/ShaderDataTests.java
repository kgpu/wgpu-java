package com.noahcharlton.wgpuj.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShaderDataTests {

    private static ShaderData data = ShaderData.fromClasspathFile("/first100bytes", "entryPoint");

    @Test
    void shaderDataBytesTest() {
        byte[] bytes = new byte[100];

        for(byte i = 0; i < 100; i++){
            bytes[i] = i;
        }

        Assertions.assertArrayEquals(bytes, data.getData());
    }

    @Test
    void dataGetEntryPointTest() {
        Assertions.assertEquals("entryPoint", data.getEntryPoint());
    }
}
