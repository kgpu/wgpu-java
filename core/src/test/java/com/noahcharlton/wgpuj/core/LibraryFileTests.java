package com.noahcharlton.wgpuj.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LibraryFileTests {

    @Test
    void fileExistsTest() {
        Assertions.assertTrue(WgpuCore.getLibraryFile().exists());
    }
}
