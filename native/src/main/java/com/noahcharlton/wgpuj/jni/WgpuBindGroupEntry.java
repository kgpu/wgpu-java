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

    public WgpuBindGroupEntry setTextureView(int binding, long textureViewId){
        this.binding.set(binding);
        this.resource.setTag(WgpuBindingResourceTag.TEXTURE_VIEW);
        this.resource.getData().setTextureViewId(textureViewId);

        return this;
    }

    public WgpuBindGroupEntry setSampler(int binding, long samplerId) {
        this.binding.set(binding);
        this.resource.setTag(WgpuBindingResourceTag.SAMPLER);
        this.resource.getData().setSamplerId(samplerId);

        return this;
    }

    public WgpuBindGroupEntry setBuffer(int binding, long buffer, long size){
        this.binding.set(binding);
        this.resource.setTag(WgpuBindingResourceTag.BUFFER);
        this.resource.getData().getBinding().set(buffer, 0, size);

        return this;
    }

    public Unsigned32 getBinding() {
        return binding;
    }

    public WgpuBindingResource getResource() {
        return resource;
    }
}

