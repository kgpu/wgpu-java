package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindingResourceData;
import com.noahcharlton.wgpuj.jni.WgpuBindingResourceTag;
import com.noahcharlton.wgpuj.jni.WgpuColor;
import com.noahcharlton.wgpuj.jni.WgpuExtent3d;
import com.noahcharlton.wgpuj.jni.WgpuOrigin3d;
import com.noahcharlton.wgpuj.util.RustCString;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class WgpuTypeTests extends WgpuNativeTest {

    @Test
    void wgpuColorTest() {
        WgpuColor color = WgpuColor.createDirect();
        color.setR(1.0);
        color.setG(.75);
        color.setB(.5);
        color.setA(.25);

        Pointer output = wgpuTest.color_to_string(color.getPointerTo());

        String actual = RustCString.fromPointer(output);
        String expected = "Color { r: 1.0, g: 0.75, b: 0.5, a: 0.25 }";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void bindGroupLayoutDescriptorNameTest() {
        var descriptor = WgpuBindGroupLayoutDescriptor.createDirect();
        descriptor.setLabel("foobar9876");

        wgpuTest.bind_group_layout_descriptor_test(descriptor.getPointerTo());
    }

    @Test
    void bindGroupEntryBindingTest() {
        var entry = new WgpuBindGroupEntry();
        entry.getBinding().set(654321);

        wgpuTest.bind_group_entry_test_binding(entry.getPointerTo());
    }

    @Test
    void bindGroupEntrySamplerResourceTest() {
        var entry = new WgpuBindGroupEntry();
        entry.setSampler(0, 4343);

        Pointer actual = wgpuTest.bind_group_entry_resource_to_string(entry.getPointerTo());

        String expected = "Sampler((4343, 0, Empty))";
        Assertions.assertEquals(expected, RustCString.fromPointer(actual));
    }

    @Test
    void bindGroupEntryBufferResourceTest() {
        var entry = new WgpuBindGroupEntry();
        entry.setBuffer(0, 123, 456);

        Pointer actual = wgpuTest.bind_group_entry_resource_to_string(entry.getPointerTo());

        String expected = "Buffer(BufferBinding { buffer: (123, 0, Empty), offset: 0, size: BufferSize(456) })";
        Assertions.assertEquals(expected, RustCString.fromPointer(actual));
    }

    @Test
    void bindGroupEntryTextureResourceTest() {
        var entry = new WgpuBindGroupEntry();
        entry.setTextureView(0, 9874);

        Pointer actual = wgpuTest.bind_group_entry_resource_to_string(entry.getPointerTo());

        String expected = "TextureView((9874, 0, Empty))";
        Assertions.assertEquals(expected, RustCString.fromPointer(actual));
    }

    @Test
    void origin3dTest() {
        var origin = WgpuOrigin3d.createDirect();
        origin.setX(123);
        origin.setY(456);
        origin.setZ(789);

        wgpuTest.wgpu_origin_3d_test(origin.getPointerTo());
    }

    @Test
    void extent3dTest() {
        var extent = WgpuExtent3d.createDirect();
        extent.setWidth(147);
        extent.setHeight(258);
        extent.setDepth(369);

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
                Arguments.of((Consumer<WgpuBindingResourceData>) data -> {
                            data.getBinding().setSize(26);
                            data.getBinding().setOffset(45);
                            data.getBinding().setBuffer(31);
                        },
                        WgpuBindingResourceTag.BUFFER,
                        "Buffer(BufferBinding { buffer: (31, 0, Empty), offset: 45, size: BufferSize(26) })")
        );
    }

}
