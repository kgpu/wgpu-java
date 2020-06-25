package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.input.GlfwKeyHandler;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.core.util.GlfwHandler;
import com.noahcharlton.wgpuj.core.util.ImageData;
import com.noahcharlton.wgpuj.core.util.Platform;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWNativeX11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

    public void setIcon(ImageData data){
        ByteBuffer buffer = ByteBuffer.allocateDirect(data.getPixels().length * Integer.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN); //Red Channel comes first

        for(int pixel : data.getPixels()){
            buffer.putInt(pixel);
        }
        buffer.position(0);

        GLFWImage icon = GLFWImage.malloc();
        icon.set(data.getWidth(), data.getHeight(), buffer);
        GLFWImage.Buffer imgBuffer = GLFWImage.malloc(1);
        imgBuffer.put(0, icon);

        GLFW.glfwSetWindowIcon(handle, imgBuffer);

        imgBuffer.free();
        icon.free();
    }

    private long createSurface() {
        long osHandle = GlfwHandler.getOsWindowHandle(this.handle);

        if(Platform.isWindows){
            return WgpuJava.wgpuNative.wgpu_create_surface_from_windows_hwnd(WgpuJava.createNullPointer(), osHandle);
        }else if(Platform.isLinux){
            long display = GLFWNativeX11.glfwGetX11Display();

            return WgpuJava.wgpuNative.wgpu_create_surface_from_xlib(display, osHandle);
        }

        throw new UnsupportedOperationException("Platform not supported. See " +
                "https://github.com/DevOrc/wgpu-java/issues/4");
    }

    public void initRenderPipeline(RenderPipelineSettings settings, Device device){
        renderPipeline = new RenderPipeline(settings, device);

        createNewSwapChain(device);
    }

    public RenderPass renderStart(Device device){
        GLFW.glfwPollEvents();
        var newDimension = GlfwHandler.getWindowDimension(handle);

        if(!newDimension.equals(currentDimension)){
            currentDimension = newDimension;
            createNewSwapChain(device);
            eventHandler.onResize();
        }

        swapChain.renderStart(renderPipeline);

        return swapChain.getRenderPass();
    }

    public void renderEnd() {
        swapChain.renderEnd();
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
