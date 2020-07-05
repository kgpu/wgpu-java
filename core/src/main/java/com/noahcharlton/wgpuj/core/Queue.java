package com.noahcharlton.wgpuj.core;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.Buffer;
import jnr.ffi.Pointer;

public class Queue {

    private final long id;

    public Queue(long id){
        this.id = id;
    }

    public void writeFloatsToBuffer(Buffer buffer, float[] data){
        Pointer ptr = WgpuJava.createDirectPointer(data.length * Float.BYTES);
        ptr.put(0, data, 0, data.length);

        WgpuJava.wgpuNative.wgpu_queue_write_buffer(id, buffer.getId(), 0, ptr, data.length * Float.BYTES);
    }

    public void submit(long... commandBuffers){
        Pointer pointer = WgpuJava.createLongArrayPointer(commandBuffers);

        WgpuJava.wgpuNative.wgpu_queue_submit(id, pointer, commandBuffers.length);
    }
}
