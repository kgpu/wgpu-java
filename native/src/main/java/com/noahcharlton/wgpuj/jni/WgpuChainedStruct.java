package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuChainedStruct extends WgpuJavaStruct {

    /**
     * This is a pointer because of unknown bug that is crashing for
     * self referencing structs.
     *
     * See https://github.com/DevOrc/wgpu-java/issues/24
     */
    private final Struct.Pointer next = new Struct.Pointer();
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


    public void setNext(jnr.ffi.Pointer value){
        next.set(value);
    }

    public Pointer getNext() {
        return next;
    }

    public WgpuSType getSType(){
        return sType.get();
    }

    public void setSType(WgpuSType x){
        this.sType.set(x);
    }

}