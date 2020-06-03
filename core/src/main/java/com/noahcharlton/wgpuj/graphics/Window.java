package com.noahcharlton.wgpuj.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.WGPUPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuDeviceDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRequestAdapterOptions;
import com.noahcharlton.wgpuj.jni.WgpuShaderModuleDescription;
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
    private final long device;

    public Window() {
        this.handle = GLFW.glfwCreateWindow(300, 300, "Title", NULL, NULL);

        if(this.handle == NULL)
            throw new RuntimeException("Failed to create window!");

        initializeGlfwWindow();
        device = createDevice();

        loadShaders();
        createPipeline();
    }

    private void createPipeline() {
        var layoutDescriptor = new WgpuBindGroupLayoutDescriptor("bind group layout");
        var bindGroupLayout = WgpuJava.wgpuNative.wgpu_device_create_bind_group_layout(device,
                layoutDescriptor.getPointerTo());
        System.out.println("Bind Group Layout: " + bindGroupLayout);
        var groupDescriptor = new WgpuBindGroupDescriptor("bind group", bindGroupLayout);
        var bindGroup = WgpuJava.wgpuNative.wgpu_device_create_bind_group(device, groupDescriptor.getPointerTo());

        System.out.println("Bind Group: " + bindGroup);
    }

    private void loadShaders() {
        var vertex = WgpuShaderModuleDescription.fromFile("triangle.vert").load(device);
        var frag = WgpuShaderModuleDescription.fromFile("triangle.frag").load(device);

        System.out.printf("Shaders {vertex = %d, frag = %d} \n", vertex, frag);
    }

    private void initializeGlfwWindow() {
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
    }

    private long createDevice() {
        var surface = createSurface();
        var options = new WgpuRequestAdapterOptions(WGPUPowerPreference.DEFAULT, surface);
        var adapter = requestAdapter(options);
        var descriptor = new WgpuDeviceDescriptor(false, 1);

        return requestDevice(adapter, descriptor);
    }

    private long requestDevice(long adapter, WgpuDeviceDescriptor desc) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(adapter);

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
