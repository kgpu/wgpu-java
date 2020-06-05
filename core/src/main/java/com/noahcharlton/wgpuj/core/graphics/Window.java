package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.core.util.GlfwHandler;
import com.noahcharlton.wgpuj.core.util.Platform;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuDeviceDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRequestAdapterOptions;
import jnr.ffi.Pointer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long handle;
    private final long device;
    private final long surface;

    private final RenderPipeline renderPipeline;

    private Dimension currentDimension;
    private SwapChain swapChain;

    public Window(RenderPipelineSettings renderSettings, WindowSettings windowSettings) {
        this.handle = windowSettings.createWindow();

        if(this.handle == NULL)
            throw new RuntimeException("Failed to create window!");

        currentDimension = GlfwHandler.getWindowDimension(handle);
        GlfwHandler.centerWindow(handle, currentDimension);

        surface = createSurface();
        device = createDevice();
        renderPipeline = new RenderPipeline(renderSettings, device);

        createNewSwapChain();
    }

    private long createDevice() {
        var options = new WgpuRequestAdapterOptions(WgpuPowerPreference.DEFAULT, surface);
        var adapter = requestAdapter(options);
        var descriptor = new WgpuDeviceDescriptor(false, 1);

        return requestDevice(adapter, descriptor);
    }

    private long requestDevice(long adapter, WgpuDeviceDescriptor desc) {
        return WgpuJava.wgpuNative.wgpu_adapter_request_device(adapter, desc.getPointerTo(), null);
    }

    private long requestAdapter(WgpuRequestAdapterOptions options) {
        AtomicLong adapter = new AtomicLong(0);

        WgpuJava.wgpuNative.wgpu_request_adapter_async(
                options.getPointerTo(),
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

    public void render(){
        GLFW.glfwPollEvents();
        var newDimension = GlfwHandler.getWindowDimension(handle);

        if(!newDimension.equals(currentDimension)){
            currentDimension = newDimension;

            onResize();
        }

        swapChain.render(renderPipeline, device);
    }

    private void onResize() {
        createNewSwapChain();
    }

    private void createNewSwapChain() {
        swapChain = SwapChain.create(currentDimension, device, surface);
    }

    public Dimension getWindowDimension() {
        return currentDimension;
    }

    public boolean isCloseRequested(){
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void dispose(){
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
    }
}
