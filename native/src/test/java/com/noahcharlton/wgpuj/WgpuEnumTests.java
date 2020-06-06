package com.noahcharlton.wgpuj;

import com.noahcharlton.wgpuj.jni.WgpuLoadOp;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuPresentMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuStoreOp;
import com.noahcharlton.wgpuj.util.RustCString;
import jnr.ffi.Pointer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WgpuEnumTests extends WgpuNativeTest{

    @ParameterizedTest
    @EnumSource(WgpuPowerPreference.class)
    void wgpuPowerPreferenceEnumTest(WgpuPowerPreference preference){
        Pointer output = wgpuTest.get_power_preference_name(preference);

        standardEnumTest(preference, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuPrimitiveTopology.class)
    void wgpuPrimitiveTopologyTest(WgpuPrimitiveTopology primitive){
        Pointer output = wgpuTest.get_primitive_topology_name(primitive);

        standardEnumTest(primitive, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuPresentMode.class)
    void wgpuPresentModeTest(WgpuPresentMode mode){
        Pointer output = wgpuTest.get_present_mode_name(mode);

        standardEnumTest(mode, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuLoadOp.class)
    void wgpuLoadOpTest(WgpuLoadOp op){
        Pointer output = wgpuTest.get_load_op_name(op);

        standardEnumTest(op, output);
    }

    @ParameterizedTest
    @EnumSource(WgpuStoreOp.class)
    void wgpuStoreOpTest(WgpuStoreOp op){
        Pointer output = wgpuTest.get_store_op_name(op);

        standardEnumTest(op, output);
    }

    private <E extends Enum> void standardEnumTest(E e, Pointer output){
        String actual = RustCString.fromPointer(output);
        String expected = toRustEnumName(e);

        Assertions.assertEquals(expected, actual);
    }

    private <E extends Enum> String toRustEnumName(E enumType){
        var nameParts = enumType.name().split("_");

        return Arrays.stream(nameParts).map(input ->
                input.substring(0, 1) + input.substring(1).toLowerCase()).collect(Collectors.joining());
    }
}
