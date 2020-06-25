package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.*;
import com.noahcharlton.wgpuj.core.math.MatrixUtils;
import com.noahcharlton.wgpuj.core.util.*;
import com.noahcharlton.wgpuj.jni.*;
import jnr.ffi.Pointer;
import org.joml.Matrix4f;

public class SimulationExample {

    private static final float[] VERTICES = new float[]{
            -4f, 0f,
            4f, 0f,
            0f, 16f,
    };

    private static final long FLOATS_PER_VERTEX = 2;

    private final WgpuGraphicApplication app;
    private final Device device;
    private final Window window;
    private final Buffer vertexBuffer;
    private final Buffer projMatrixBuffer;
    private final Matrix4f viewMatrix;

    private final long bindGroup;

    public SimulationExample(GraphicApplicationSettings settings) {
        app = WgpuGraphicApplication.create(settings);
        device = app.getDevice();
        window = app.getWindow();
        viewMatrix = new Matrix4f();

        vertexBuffer = device.createVertexBuffer("Vertex Buffer", VERTICES);
        var projMatrix = createProjectionMatrix();
        projMatrixBuffer = device.createFloatBuffer("Projection Matrix buffer", MatrixUtils.toFloats(projMatrix),
                BufferUsage.UNIFORM, BufferUsage.COPY_DST);

        var bindGroupLayout = device.createBindGroupLayout("Bind Group Layout",
                BindGroupUtils.partialLayout(0, Wgpu.ShaderStage.VERTEX, WgpuBindingType.UNIFORM_BUFFER));

        bindGroup = device.createBindGroup("Bind Group", bindGroupLayout,
                new WgpuBindGroupEntry().setBuffer(0, projMatrixBuffer.getId(), projMatrixBuffer.getSize()));


        var pipelineSettings = createPipelineSettings();
        pipelineSettings.setBindGroupLayouts(bindGroupLayout);

        app.init(pipelineSettings);
        run();
    }

    private void run() {
        while(!window.isCloseRequested()){
            render();
            update();
        }

        app.close();
    }

    private void update() {
        long queue = device.getDefaultQueue();
        long matrixBuffer = projMatrixBuffer.getId();
        var matrix = createTransformationMatrix();

        Pointer pointer = WgpuJava.createDirectPointer(16 * Float.BYTES);
        pointer.put(0, MatrixUtils.toFloats(matrix), 0, 16);

        WgpuJava.wgpuNative.wgpu_queue_write_buffer(queue, matrixBuffer, 0, pointer, 16 * Float.BYTES);
    }

    private void render() {
        var renderPass = app.renderStart();
        renderPass.setBindGroup(0, bindGroup);
        renderPass.setVertexBuffer(vertexBuffer, 0);
        renderPass.draw(3, 1);

        app.renderEnd();
    }

    private Matrix4f createTransformationMatrix() {
        return MatrixUtils.generateMatrix(createProjectionMatrix(), viewMatrix);
    }

    private Matrix4f createProjectionMatrix() {
        Dimension dimension = app.getWindow().getWindowDimension();
        var projectionWidth = dimension.getWidth() / 2f;
        var projectionHeight = dimension.getHeight() / 2f;

        return new Matrix4f().ortho2D(-projectionWidth, projectionWidth,
                -projectionHeight, projectionHeight);
    }

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();

        var settings = new GraphicApplicationSettings("Pandemic Simulation", 640, 480);

        new SimulationExample(settings);
    }

    private static RenderPipelineSettings createPipelineSettings(){
        ShaderData vertex = ShaderData.fromRawClasspathFile("/simulation.vert", "main");
        ShaderData fragment = ShaderData.fromRawClasspathFile("/simulation.frag", "main");

        return new RenderPipelineSettings()
                .setVertexStage(vertex)
                .setFragmentStage(fragment)
                .setRasterizationState(RasterizationState.of(
                        WgpuFrontFace.CCW,
                        WgpuCullMode.NONE,
                        0,
                        0.0f,
                        0.0f))
                .setPrimitiveTopology(WgpuPrimitiveTopology.TRIANGLE_LIST)
                .setColorStates(new ColorState(
                        WgpuTextureFormat.BGRA8_UNORM,
                        new BlendDescriptor(WgpuBlendFactor.ONE, WgpuBlendFactor.ZERO, WgpuBlendOperation.ADD),
                        new BlendDescriptor(WgpuBlendFactor.ONE, WgpuBlendFactor.ZERO, WgpuBlendOperation.ADD),
                        Wgpu.ColorWrite.ALL).build())
                .setDepthStencilState(null)
                .setVertexIndexFormat(WgpuIndexFormat.UINT16)
                .setBufferLayouts(Buffer.createLayout(
                        FLOATS_PER_VERTEX * Float.BYTES,
                        WgpuInputStepMode.VERTEX,
                        Buffer.vertexAttribute(0, WgpuVertexFormat.FLOAT2, 0)
                ))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts()
                .setClearColor(Color.BLACK);
    }
}
