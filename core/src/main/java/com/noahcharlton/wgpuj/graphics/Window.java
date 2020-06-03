package com.noahcharlton.wgpuj.graphics;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.jni.WGPUPowerPreference;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuDeviceDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuPipelineLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuProgrammableStageDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRasterizationStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRequestAdapterOptions;
import com.noahcharlton.wgpuj.jni.WgpuShaderModuleDescription;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.util.Dimension;
import com.noahcharlton.wgpuj.util.GlfwHandler;
import com.noahcharlton.wgpuj.util.Platform;
import jnr.ffi.Pointer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.concurrent.atomic.AtomicLong;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long handle;
    private final long device;
    private final long surface;

    private long vertexModule;
    private long fragmentModule;

    private Dimension currentDimension;
    private SwapChain swapChain;

    public Window() {
        this.handle = GLFW.glfwCreateWindow(300, 300, "Title", NULL, NULL);

        if(this.handle == NULL)
            throw new RuntimeException("Failed to create window!");

        initializeGlfwWindow();
        surface = createSurface();
        device = createDevice();

        loadShaders();
        createPipeline();
        createSwapChain();
    }

    private void createPipeline() {
        var layoutDescriptor = new WgpuBindGroupLayoutDescriptor("bind group layout");
        var bindGroupLayout = WgpuJava.wgpuNative.wgpu_device_create_bind_group_layout(device,
                layoutDescriptor.getPointerTo());
        var groupDescriptor = new WgpuBindGroupDescriptor("bind group", bindGroupLayout);
        var bindGroup = WgpuJava.wgpuNative.wgpu_device_create_bind_group(device, groupDescriptor.getPointerTo());
        var pipelineLayoutDescriptor = new WgpuPipelineLayoutDescriptor(bindGroupLayout);
        var pipelineLayoutID = WgpuJava.wgpuNative.wgpu_device_create_pipeline_layout(device,
                pipelineLayoutDescriptor.getPointerTo());
        var pipelineSettings = createPipelineSettings(pipelineLayoutID).build().getPointerTo();
        var pipelineId = WgpuJava.wgpuNative.wgpu_device_create_render_pipeline(device, pipelineSettings);

        System.out.println("Bind Group Layout: " + bindGroupLayout);
        System.out.println("Bind Group: " + bindGroup);
        System.out.println("Pipeline Layout: " + pipelineLayoutID);
        System.out.println("Pipeline: " + pipelineId);
    }

    private PipelineSettings createPipelineSettings(long pipelineLayout){
        var fragment = new WgpuProgrammableStageDescriptor(fragmentModule, "main").getPointerTo();

        return new PipelineSettings()
                .setLayout(pipelineLayout)
                .setVertexModule(vertexModule)
                .setVertexEntryPoint("main")
                .setFragmentStage(fragment)
                .setRasterizationState(new WgpuRasterizationStateDescriptor(
                        WgpuFrontFace.COUNTER_CLOCKWISE,
                        WgpuCullMode.NONE,
                        0,
                        0.0f,
                        0.0f).getPointerTo())
                .setPrimitiveTopology(WgpuPrimitiveTopology.TriangleList)
                .setColorStates(new WgpuColorStateDescriptor(
                        WgpuTextureFormat.Bgra8Unorm,
                        new BlendDescriptor(WgpuBlendFactor.One, WgpuBlendFactor.Zero, WgpuBlendOperation.ADD),
                        new BlendDescriptor(WgpuBlendFactor.One, WgpuBlendFactor.Zero, WgpuBlendOperation.ADD),
                        WgpuColorStateDescriptor.ALL))
                .setDepthStencilState(WgpuJava.createNullPointer())
                .setVertexIndexFormat(WgpuIndexFormat.Uint16)
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false);
    }

    private void loadShaders() {
        vertexModule = WgpuShaderModuleDescription.fromFile("triangle.vert").load(device);
        fragmentModule = WgpuShaderModuleDescription.fromFile("triangle.frag").load(device);

        System.out.printf("Shaders {vertex = %d, frag = %d} \n", vertexModule, fragmentModule);
    }

    private void initializeGlfwWindow() {
        GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
                GLFW.glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        currentDimension = GlfwHandler.getWindowDimension(handle);
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        // Center the window
        GLFW.glfwSetWindowPos(
                handle,
                (vidmode.width() - currentDimension.getWidth()) / 2,
                (vidmode.height() - currentDimension.getHeight()) / 2
        );
    }

    private long createDevice() {
        var options = new WgpuRequestAdapterOptions(WGPUPowerPreference.DEFAULT, surface);
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

    public void update(){
        GLFW.glfwPollEvents();
        var newDimension = GlfwHandler.getWindowDimension(handle);

        if(!newDimension.equals(currentDimension)){
            currentDimension = newDimension;

            onResize();
        }

        swapChain.update();
    }

    private void onResize() {
        System.out.println("Resize: " + currentDimension);

        createSwapChain();
    }

    private void createSwapChain() {
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

    long getWgpuDevice() {
        return device;
    }
}
