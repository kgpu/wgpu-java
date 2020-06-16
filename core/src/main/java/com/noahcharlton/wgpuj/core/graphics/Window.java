package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.input.GlfwKeyHandler;
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
    private final long surface;

    private RenderPipeline renderPipeline;
    private Dimension currentDimension;
    private SwapChain swapChain;
    private WindowEventHandler eventHandler;

    public Window(GraphicApplicationSettings settings) {
        this.handle = settings.createWindow();

        if(this.handle == NULL)
            throw new RuntimeException("Failed to create window!");

        currentDimension = GlfwHandler.getWindowDimension(handle);
        GlfwHandler.centerWindow(handle, currentDimension);
        GLFW.glfwSetKeyCallback(handle, new GlfwKeyHandler(this));

        surface = createSurface();
        eventHandler = new WindowEventHandler.EmptyWindowHandler();
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

    public void initRenderPipeline(RenderPipelineSettings settings, Device device){
        renderPipeline = new RenderPipeline(settings, device);

        createNewSwapChain(device);
    }

    public SwapChain renderStart(Device device){
        GLFW.glfwPollEvents();
        var newDimension = GlfwHandler.getWindowDimension(handle);

        if(!newDimension.equals(currentDimension)){
            currentDimension = newDimension;
            createNewSwapChain(device);
            eventHandler.onResize();
        }

        swapChain.renderStart(renderPipeline);

        return swapChain;
    }

    private void createNewSwapChain(Device device) {
        swapChain = SwapChain.create(currentDimension, device, surface);
    }

    public Dimension getWindowDimension() {
        return currentDimension;
    }

    public boolean isCloseRequested(){
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void setEventHandler(WindowEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void dispose(){
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
    }

    public long getSurface() {
        return surface;
    }

    public WindowEventHandler getEventHandler() {
        return eventHandler;
    }
}
