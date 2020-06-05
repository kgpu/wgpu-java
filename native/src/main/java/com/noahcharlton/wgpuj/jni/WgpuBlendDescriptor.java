package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.graphics.BlendDescriptor;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuBlendDescriptor extends WgpuJavaStruct {

    private Struct.Enum<WgpuBlendFactor> source = new Struct.Enum<>(WgpuBlendFactor.class);
    private Struct.Enum<WgpuBlendFactor> destination = new Struct.Enum<>(WgpuBlendFactor.class);
    private Struct.Enum<WgpuBlendOperation> operation = new Struct.Enum<>(WgpuBlendOperation.class);

    public void set(BlendDescriptor desc){
        source.set(desc.getSrc());
        destination.set(desc.getDest());
        operation.set(desc.getOperation().getIntValue());
    }
}
