package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuPipelineLayoutDescriptor extends WgpuJavaStruct {

    private final Struct.Pointer bindGroupLayouts = new Struct.Pointer();
    private final Struct.UnsignedLong length = new Struct.UnsignedLong();

    public WgpuPipelineLayoutDescriptor(long... layouts) {
        useDirectMemory();

        jnr.ffi.Pointer bindGroups = WgpuJava.createDirectPointer(layouts.length * Long.BYTES);

        for(int i = 0; i < layouts.length; i++){
            bindGroups.putLongLong(i * Long.BYTES, layouts[i]);
        }

        bindGroupLayouts.set(bindGroups);
        length.set(layouts.length);
    }
}
