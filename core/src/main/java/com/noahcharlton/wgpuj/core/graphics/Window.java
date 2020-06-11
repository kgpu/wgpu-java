package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.util.Backend;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.core.util.GlfwHandler;
import com.noahcharlton.wgpuj.core.util.Platform;
import com.noahcharlton.wgpuj.jni.WgpuLimits;
import com.noahcharlton.wgpuj.jni.WgpuPowerPreference;
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

    private RenderPipeline renderPipeline;
    private Dimension currentDimension;
    private SwapChain swapChain;

    public Window(WindowSettings windowSettings) {
        this.handle = windowSettings.createWindow();

        if(this.handle == NULL)
            throw new RuntimeException("Failed to create window!");

        currentDimension = GlfwHandler.getWindowDimension(handle);
        GlfwHandler.centerWindow(handle, currentDimension);

        surface = createSurface();
        device = createDevice();
    }

    private long createDevice() {
        var options = new WgpuRequestAdapterOptions(WgpuPowerPreference.DEFAULT, surface);
        var adapter = requestAdapter(options);
        var limits = new WgpuLimits(1).getPointerTo();

        return WgpuJava.wgpuNative.wgpu_adapter_request_device(adapter, 0, limits, null);
    }

    private long requestAdapter(WgpuRequestAdapterOptions options) {
        AtomicLong adapter = new AtomicLong(0);

        WgpuJava.wgpuNative.wgpu_request_adapter_async(
                options.getPointerTo(),
                Backend.of(Backend.VULKAN, Backend.METAL, Backend.DX12),
                false,
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

        throw new UnsupportedOperationException("Platform not supported. See " +
                "https://github.com/DevOrc/wgpu-java/issues/4");
    }

    public void initRenderPipeline(RenderPipelineSettings settings){
        renderPipeline = new RenderPipeline(settings, device);

        createNewSwapChain();
    }

    public SwapChain renderStart(){
        GLFW.glfwPollEvents();
        var newDimension = GlfwHandler.getWindowDimension(handle);

        if(!newDimension.equals(currentDimension)){
            currentDimension = newDimension;

            onResize();
        }

        swapChain.renderStart(renderPipeline, device);

        return swapChain;
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

    public long getDevice() {
        return device;
    }
}
