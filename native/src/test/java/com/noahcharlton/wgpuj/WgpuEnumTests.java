package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.jni.WgpuBindingType;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuBufferMapAsyncStatus;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuInputStepMode;
import com.noahcharlton.wgpuj.jni.WgpuLoadOp;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuStoreOp;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainStatus;
import com.noahcharlton.wgpuj.jni.WgpuTextureAspect;
import com.noahcharlton.wgpuj.jni.WgpuTextureComponentType;
import com.noahcharlton.wgpuj.jni.WgpuTextureDimension;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.jni.WgpuTextureViewDimension;
import com.noahcharlton.wgpuj.jni.WgpuVertexFormat;
import com.noahcharlton.wgpuj.util.RustCString;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WgpuEnumTests extends WgpuNativeTest {

    @ParameterizedTest
    @EnumSource(WgpuPowerPreference.class)
    void wgpuPowerPreferenceEnumTest(WgpuPowerPreference preference) {
        Pointer output = wgpuTest.get_power_preference_name(preference);

        standardEnumTest(preference, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuPrimitiveTopology.class)
    void wgpuPrimitiveTopologyTest(WgpuPrimitiveTopology primitive) {
        Pointer output = wgpuTest.get_primitive_topology_name(primitive);

        standardEnumTest(primitive, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuPresentMode.class)
    void wgpuPresentModeTest(WgpuPresentMode mode) {
        Pointer output = wgpuTest.get_present_mode_name(mode);

        standardEnumTest(mode, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuLoadOp.class)
    void wgpuLoadOpTest(WgpuLoadOp op) {
        Pointer output = wgpuTest.get_load_op_name(op);

        standardEnumTest(op, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuStoreOp.class)
    void wgpuStoreOpTest(WgpuStoreOp op) {
        Pointer output = wgpuTest.get_store_op_name(op);

        standardEnumTest(op, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuBlendFactor.class)
    void wgpuBlendFactorTest(WgpuBlendFactor factor) {
        Pointer output = wgpuTest.get_blend_factor_name(factor);

        standardEnumTest(factor, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuBlendOperation.class)
    void wgpuBlendOperationTest(WgpuBlendOperation operation) {
        Pointer output = wgpuTest.get_blend_operation_name(operation);

        standardEnumTest(operation, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuCullMode.class)
    void wgpuCullModeTest(WgpuCullMode mode) {
        Pointer output = wgpuTest.get_cull_mode_name(mode);

        standardEnumTest(mode, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuIndexFormat.class)
    void wgpuIndexFormatTest(WgpuIndexFormat format) {
        Pointer output = wgpuTest.get_index_format_name(format);

        standardEnumTest(format, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuFrontFace.class)
    void wgpuFrontFaceTest(WgpuFrontFace face) {
        Pointer output = wgpuTest.get_front_face_name(face);

        standardEnumTest(face, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuLogLevel.class)
    void wgpuLogLevelTest(WgpuLogLevel level) {
        Pointer output = wgpuTest.get_log_level_name(level);

        standardEnumTest(level, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuSwapChainStatus.class)
    void wgpuSwapChainStatusTest(WgpuSwapChainStatus status) {
        Pointer output = wgpuTest.get_swap_chain_status_name(status);

        standardEnumTest(status, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuTextureFormat.class)
    void wgpuTextureFormatStatusTest(WgpuTextureFormat format) {
        Pointer output = wgpuTest.get_texture_format_name(format);

        standardEnumTest(format, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuBindingType.class)
    void wgpuBindingTypeTest(WgpuBindingType type) {
        Pointer output = wgpuTest.get_wgpu_binding_type_name(type);

        standardEnumTest(type, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuBufferMapAsyncStatus.class)
    void wgpuBufferMapAsyncStatusTest(WgpuBufferMapAsyncStatus status) {
        Pointer output = wgpuTest.get_wgpu_buffer_map_async_status_name(status);

        standardEnumTest(status, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuTextureViewDimension.class)
    void wgpuTextureViewDimensionNameTest(WgpuTextureViewDimension dimension) {
        Pointer output = wgpuTest.get_wgpu_texture_view_dimension_name(dimension);

        standardEnumTest(dimension, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuTextureComponentType.class)
    void wgpuTextureComponentTypeTest(WgpuTextureComponentType type) {
        Pointer output = wgpuTest.get_wgpu_texture_component_type_name(type);

        standardEnumTest(type, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuInputStepMode.class)
    void wgpuInputStepModeTest(WgpuInputStepMode mode) {
        Pointer output = wgpuTest.get_wgpu_input_step_mode_name(mode);

        standardEnumTest(mode, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuVertexFormat.class)
    void wgpuVertexFormatTest(WgpuVertexFormat format) {
        Pointer output = wgpuTest.get_wgpu_vertex_format_name(format);

        standardEnumTest(format, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuTextureDimension.class)
    void wgpuTextureDimensionTest(WgpuTextureDimension dimension) {
        Pointer output = wgpuTest.get_wgpu_texture_dimension_name(dimension);

        standardEnumTest(dimension, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuTextureAspect.class)
    void wgpuTextureAspectTest(WgpuTextureAspect aspect) {
        Pointer output = wgpuTest.get_wgpu_texture_aspect_name(aspect);

        standardEnumTest(aspect, output);
    }

    private <E extends Enum> void standardEnumTest(E e, Pointer output) {
        String actual = RustCString.fromPointer(output);
        String expected = toRustEnumName(e);

        Assertions.assertEquals(expected, actual);
    }

    private <E extends Enum> String toRustEnumName(E enumType) {
        var nameParts = enumType.name().split("_");

        return Arrays.stream(nameParts).map(input ->
                input.substring(0, 1) + input.substring(1).toLowerCase()).collect(Collectors.joining());
    }
}
