package com.noahcharlton.wgpuj.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BufferSettingsTests {

    @Test
    void usagesBasicTest() {
        BufferSettings settings = new BufferSettings()
                .setUsages(BufferUsage.INDEX, BufferUsage.UNIFORM, BufferUsage.STORAGE);

        int actual = settings.calculateUsage();
        int expected = BufferUsage.INDEX.getValue() | BufferUsage.UNIFORM.getValue() | BufferUsage.STORAGE.getValue();

        Assertions.assertEquals(expected, actual);
    }
}
