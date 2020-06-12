package com.noahcharlton.wgpuj.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class ClasspathUtilTests {

    @Test
    void readBytesTest() {
        var data = ClasspathUtil.readBytes("/first100bytes");

        byte[] bytes = new byte[100];

        for(byte i = 0; i < 100; i++){
            bytes[i] = i;
        }

        Assertions.assertArrayEquals(bytes, data);
    }

    @Test
    void readTextTest() {
        var data = ClasspathUtil.readText("/testTextFile", StandardCharsets.UTF_8);

        Assertions.assertEquals("Hello World! Foobar1234", data);
    }
}
