package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import jnr.ffi.Pointer;

import java.nio.ByteBuffer;

public class WgpuStructs {

    public static Pointer createWgpuRequestAdapterOptions(WGPUPowerPreference powerPreference, long surface){
        var pointer = WgpuJava.getRuntime().getMemoryManager().allocateDirect(12);
        pointer.putInt(0, powerPreference.getIntValue());
        pointer.putLongLong(4, surface);

        return pointer;
    }

    public static Pointer createWgpuDeviceDescriptor(boolean anisotropicFiltering, int maxBindGroups) {
        var pointer = WgpuJava.getRuntime().getMemoryManager().allocateDirect(5);
        pointer.putByte(0, (byte) (anisotropicFiltering ? 1 : 0));
        pointer.putInt(1, maxBindGroups);

        return pointer;
    }

    public static Pointer createWgpuU32Array(ByteBuffer buffer, int length) {
        var runtime = WgpuJava.getRuntime();
        var pointer = runtime.getMemoryManager().allocateDirect(16);
        pointer.putAddress(0, Pointer.wrap(runtime, buffer).address());
        pointer.putLong(8, Integer.toUnsignedLong(length));

        return pointer;
    }
}
