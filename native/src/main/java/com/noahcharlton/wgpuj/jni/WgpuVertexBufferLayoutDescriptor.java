package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

/** NOTE: THIS FILE WAS PRE-GENERATED BY JNR_GEN! */
public class WgpuVertexBufferLayoutDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 arrayStride = new Struct.Unsigned64();
    private final Struct.Enum<WgpuInputStepMode> stepMode = new Struct.Enum<>(WgpuInputStepMode.class);
    private final DynamicStructRef<WgpuVertexAttributeDescriptor> attributes = new DynamicStructRef<>(WgpuVertexAttributeDescriptor.class);
    private final Struct.Unsigned64 attributesLength = new Struct.Unsigned64();

    private WgpuVertexBufferLayoutDescriptor(){}

    @Deprecated
    public WgpuVertexBufferLayoutDescriptor(Runtime runtime){
        super(runtime);
    }

    public static WgpuVertexBufferLayoutDescriptor createHeap(){
        return new WgpuVertexBufferLayoutDescriptor();
    }

    public static WgpuVertexBufferLayoutDescriptor createDirect(){
        var struct = new WgpuVertexBufferLayoutDescriptor();
        struct.useDirectMemory();
        return struct;
    }


    public long getArrayStride(){
        return arrayStride.get();
    }

    public void setArrayStride(long x){
        this.arrayStride.set(x);
    }

    public WgpuInputStepMode getStepMode(){
        return stepMode.get();
    }

    public void setStepMode(WgpuInputStepMode x){
        this.stepMode.set(x);
    }

    public DynamicStructRef<WgpuVertexAttributeDescriptor> getAttributes(){
        return attributes;
    }

    public void setAttributes(WgpuVertexAttributeDescriptor... x){
        if(x.length == 0 || x[0] == null){
            this.attributes.set(WgpuJava.createNullPointer());
        } else {
            this.attributes.set(x);
        }
    }

    public long getAttributesLength(){
        return attributesLength.get();
    }

    public void setAttributesLength(long x){
        this.attributesLength.set(x);
    }

}