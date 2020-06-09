package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuBindGroupEntry extends WgpuJavaStruct {

    private final Struct.Unsigned32 binding = new Struct.Unsigned32();
    private final Struct.Unsigned32 padding = new Struct.Unsigned32(); //TODO - investigate this
    private final WgpuBindingResource resource = inner(new WgpuBindingResource());

    public WgpuBindGroupEntry(Runtime runtime) {
        super(runtime);
    }

    public WgpuBindGroupEntry() {
        useDirectMemory();
    }

    public void setBinding(int binding){
        this.binding.set(binding);
    }

    public WgpuBindingResource getResource() {
        return resource;
    }
}

