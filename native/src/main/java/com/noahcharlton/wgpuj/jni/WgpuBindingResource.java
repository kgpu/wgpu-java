package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuBindingResource extends WgpuJavaStruct {

    private final Struct.Enum<WgpuBindingResourceTag> tag = new Struct.Enum<>(WgpuBindingResourceTag.class);
    private final WgpuBindingResourceData id = inner(new WgpuBindingResourceData());

    public WgpuBindingResource() {

    }

    public void setTag(WgpuBindingResourceTag tag){
        this.tag.set(tag);
    }

    public WgpuBindingResourceData getData() {
        return id;
    }
}
