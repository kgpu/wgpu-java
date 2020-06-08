package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuLoadOp;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuStoreOp;
import com.noahcharlton.wgpuj.jni.WgpuSwapChainStatus;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
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
        String actual = RustCString.fromPointer(output);

        if(face == WgpuFrontFace.CLOCKWISE) {
            Assertions.assertEquals("Cw", actual);
        } else {
            Assertions.assertEquals("Ccw", actual);
        }
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
