package com.noahcharlton.wgpuj.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.WGPUPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuStructs;
import com.noahcharlton.wgpuj.util.GlfwHandler;
import com.noahcharlton.wgpuj.util.Platform;
import jnr.ffi.Pointer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long handle;

    public Window() {
        this.handle = GLFW.glfwCreateWindow(300, 300, "Title", NULL, NULL);

        if(this.handle == NULL)
            throw new RuntimeException("Failed to create window!");

        GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
                GLFW.glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        try (MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(handle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center the window
            GLFW.glfwSetWindowPos(
                    handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        var surface = createSurface();
        var options = WgpuStructs.createWgpuRequestAdapterOptions(WGPUPowerPreference.LOW, surface);
        var adapter = requestAdapter(options);

        var descriptor = WgpuStructs.createWgpuDeviceDescriptor(false, 1);
        var device = requestDevice(adapter, descriptor);
    }

    private long requestDevice(long adapter, Pointer desc) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(adapter);

        return WgpuJava.wgpuNative.wgpu_adapter_request_device(adapter, desc, null);
    }

    private long requestAdapter(Pointer options) {
        AtomicLong adapter = new AtomicLong(0);

        WgpuJava.wgpuNative.wgpu_request_adapter_async(
                options,
                2 | 4 | 8, //Backends for Vulkan, Metal, Dx12
                (received, userData) -> {
                    adapter.set(received);
                },
                WgpuJava.createNullPointer()
        );

        return adapter.get();
    }

    private long createSurface() {
        long osHandle = GlfwHandler.getOsWindowHandle(this.handle);

        if(Platform.isWindows){
            Pointer hwnd = WgpuJava.createLongPointer(osHandle);

            return WgpuJava.wgpuNative.wgpu_create_surface_from_windows_hwnd(WgpuJava.createNullPointer(), hwnd);
        }

        throw new UnsupportedOperationException();
    }

    public void update(){
        GLFW.glfwPollEvents();
    }

    public boolean isCloseRequested(){
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void dispose(){
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
    }
}
