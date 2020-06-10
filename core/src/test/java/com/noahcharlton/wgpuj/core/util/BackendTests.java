package com.noahcharlton.wgpuj.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BackendTests {

    @Test
    public void backendBitTest(){
        int output = Backend.of(Backend.VULKAN, Backend.METAL, Backend.DX12);

        Assertions.assertEquals(2 | 4 | 8, output);
    }
}
