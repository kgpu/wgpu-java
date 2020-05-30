package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import jnr.ffi.Pointer;

public class WgpuStructs {

    public static Pointer createWgpuRequestAdapterOptions(WGPUPowerPreference powerPreference, long surface){
        var pointer = WgpuJava.getRuntime().getMemoryManager().allocateDirect(12);
        pointer.putInt(0, powerPreference.getIntValue());
        pointer.putLongLong(4, surface);

        return pointer;
    }

}
