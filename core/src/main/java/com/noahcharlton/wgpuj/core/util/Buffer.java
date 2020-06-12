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

    public static Buffer createVertexBuffer(String name, float[] data, long device){
        return createFloatBuffer(name, data, device, BufferUsage.VERTEX);
    }

    public static Buffer createFloatBuffer(String name, float[] data, long device, BufferUsage... usages){
        var buffer = new BufferSettings()
                .setLabel(name)
                .setSize(data.length * Float.BYTES)
                .setMapped(true)
                .setUsages(usages)
                .createBuffer(device);
        var ptr = buffer.getMappedData();

        for(int i = 0; i < data.length; i++){
            ptr.put(0, data, 0, data.length);
        }

        buffer.unmap();

        return buffer;
    }

    public static Buffer createIndexBuffer(String name, short[] data, long device){
        return createShortBuffer(name, data, device, BufferUsage.INDEX);
    }

    public static Buffer createShortBuffer(String name, short[] data, long device, BufferUsage... usages){
        var buffer = new BufferSettings()
                .setLabel(name)
                .setSize(data.length * Short.BYTES)
                .setMapped(true)
                .setUsages(usages)
                .createBuffer(device);
        var ptr = buffer.getMappedData();

        for(int i = 0; i < data.length; i++){
            ptr.put(0, data, 0, data.length);
        }

        buffer.unmap();

        return buffer;
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

    public long getSize() {
        return size;
    }
}
