package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindingResourceData;
import com.noahcharlton.wgpuj.jni.WgpuBindingResourceTag;
import com.noahcharlton.wgpuj.jni.WgpuColor;
import com.noahcharlton.wgpuj.jni.WgpuExtent3d;
import com.noahcharlton.wgpuj.jni.WgpuOrigin3D;
import com.noahcharlton.wgpuj.util.RustCString;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class WgpuTypeTests extends WgpuNativeTest{

    @Test
    void wgpuColorTest() {
        WgpuColor color = new WgpuColor();
        color.useDirectMemory();
        color.set(1.0, .75, .5, .25);
        Pointer output = wgpuTest.color_to_string(color.getPointerTo());

        String actual = RustCString.fromPointer(output);
        String expected = "Color { r: 1.0, g: 0.75, b: 0.5, a: 0.25 }";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void bindGroupLayoutDescriptorNameTest(){
        var descriptor = new WgpuBindGroupLayoutDescriptor("foobar9876");

        wgpuTest.bind_group_layout_descriptor_test(descriptor.getPointerTo());
    }

    @Test
    void bindGroupEntryBindingTest() {
        var entry = new WgpuBindGroupEntry();
        entry.setBinding(654321);

        wgpuTest.bind_group_entry_test_binding(entry.getPointerTo());
    }

    @Test
    void bindGroupEntryResourceTest() {
        var entry = new WgpuBindGroupEntry();
        entry.setBinding(654321);

        wgpuTest.bind_group_entry_test_binding(entry.getPointerTo());
    }

    @Test
    void origin3dTest() {
        var origin = new WgpuOrigin3D(123, 456, 789);

        wgpuTest.wgpu_origin_3d_test(origin.getPointerTo());
    }

    @Test
    void extent3dTest() {
        var extent = new WgpuExtent3d(147, 258, 369);

        wgpuTest.wgpu_extent_3d_test(extent.getPointerTo());
    }

    @ParameterizedTest
    @MethodSource("getBindGroupEntryStringInputs")
    void bindGroupEntryDataStringTest(Consumer<WgpuBindingResourceData> data, WgpuBindingResourceTag tag,
                                      String expected) {
        var entry = new WgpuBindGroupEntry();
        entry.getResource().setTag(tag);
        data.accept(entry.getResource().getData());

        Pointer output = wgpuTest.bind_group_entry_resource_to_string(entry.getPointerTo());
        String actual = RustCString.fromPointer(output);

        Assertions.assertEquals(expected, actual);
    }

    private static Stream<Arguments> getBindGroupEntryStringInputs() {
        return Stream.of(
                Arguments.of((Consumer<WgpuBindingResourceData>) data -> data.setSamplerId(745),
                        WgpuBindingResourceTag.SAMPLER, "Sampler((745, 0, Empty))"),
                Arguments.of((Consumer<WgpuBindingResourceData>) data -> data.setTextureViewId(66),
                        WgpuBindingResourceTag.TEXTURE_VIEW, "TextureView((66, 0, Empty))"),
                Arguments.of((Consumer<WgpuBindingResourceData>) data -> data.getBinding().set(31, 45, 26),
                        WgpuBindingResourceTag.BUFFER,
                        "Buffer(BufferBinding { buffer: (31, 0, Empty), offset: 45, size: BufferSize(26) })")
        );
    }

}
