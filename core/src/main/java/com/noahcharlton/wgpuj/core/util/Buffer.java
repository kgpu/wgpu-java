package com.noahcharlton.wgpuj.core.util;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.BufferMapCallback;
import jnr.ffi.Pointer;

public class Buffer {

    private final long id;
    private final long size;

    protected Buffer(long id, long size) {
        this.id = id;
        this.size = size;
    }

    public void readAsync(){
        readAsync((status, userdata) -> {}, WgpuJava.createNullPointer());
    }

    public void readAsync(BufferMapCallback callback, Pointer userData){
        WgpuJava.wgpuNative.wgpu_buffer_map_read_async(id, 0, size, callback, userData);
    }

    public void copyTo(Buffer destination, long commandEncoder){
        WgpuJava.wgpuNative.wgpu_command_encoder_copy_buffer_to_buffer(commandEncoder,
                id, 0, destination.id, 0, size);
    }

    public Pointer getMappedData(){
        return WgpuJava.wgpuNative.wgpu_buffer_get_mapped_range(id, 0, size);
    }

    public void unmap(){
        WgpuJava.wgpuNative.wgpu_buffer_unmap(id);
    }

    public long getId() {
        return id;
    }
}
