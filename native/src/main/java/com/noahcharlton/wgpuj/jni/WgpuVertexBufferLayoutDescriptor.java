package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuVertexBufferLayoutDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 arrayStride = new Struct.Unsigned64();
    private final Struct.Enum<WgpuInputStepMode> stepMode = new Struct.Enum<>(WgpuInputStepMode.class);
    private final Struct.StructRef<WgpuVertexBufferAttributeDescriptor> attributes
            = new Struct.StructRef<>(WgpuVertexBufferAttributeDescriptor.class);
    private final Struct.UnsignedLong attributesLength = new Struct.UnsignedLong();

    public WgpuVertexBufferLayoutDescriptor(Runtime runtime) {
        super(runtime);
    }

    public WgpuVertexBufferLayoutDescriptor(long stride, WgpuInputStepMode mode,
                                            WgpuVertexBufferAttributeDescriptor... attributes) {
        useDirectMemory();

        arrayStride.set(stride);
        stepMode.set(mode);
        this.attributes.set(attributes);
        attributesLength.set(attributes.length);
    }
}
