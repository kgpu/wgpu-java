package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuChainedStruct extends WgpuJavaStruct {

    private final DynamicStructRef<WgpuChainedStruct> next = new DynamicStructRef<>(WgpuChainedStruct.class);
    private final Struct.Enum<WgpuSType> sType = new Struct.Enum<>(WgpuSType.class);

    private WgpuChainedStruct(){}

    @Deprecated
    public WgpuChainedStruct(Runtime runtime){
        super(runtime);
    }

    public static WgpuChainedStruct createHeap(){
        return new WgpuChainedStruct();
    }

    public static WgpuChainedStruct createDirect(){
        var struct = new WgpuChainedStruct();
        struct.useDirectMemory();
        return struct;
    }


    public void setNext(WgpuChainedStruct value){
        next.set(value);
    }

    public DynamicStructRef<WgpuChainedStruct> getNext() {
        return next;
    }

    public WgpuSType getSType(){
        return sType.get();
    }

    public void setSType(WgpuSType x){
        this.sType.set(x);
    }

}