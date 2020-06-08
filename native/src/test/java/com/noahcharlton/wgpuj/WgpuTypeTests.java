package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuColor;
import com.noahcharlton.wgpuj.util.RustCString;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WgpuTypeTests extends WgpuNativeTest{

    @Test
    void wgpuColorTest() {
        WgpuColor color = new WgpuColor();
        color.set(1.0, .75, .5, .25);
        Pointer output = wgpuTest.color_to_string(color);

        String actual = RustCString.fromPointer(output);
        String expected = "Color { r: 1.0, g: 0.75, b: 0.5, a: 0.25 }";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void bindGroupLayoutDescriptorNameTest(){
        var descriptor = new WgpuBindGroupLayoutDescriptor("foobar9876");

        wgpuTest.bind_group_layout_descriptor_test(descriptor.getPointerTo());
    }
}
