package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.core.*;
import com.noahcharlton.wgpuj.core.graphics.*;
import com.noahcharlton.wgpuj.core.input.Key;
import com.noahcharlton.wgpuj.core.math.MathUtils;
import com.noahcharlton.wgpuj.core.math.MatrixUtils;
import com.noahcharlton.wgpuj.core.util.*;
import com.noahcharlton.wgpuj.jni.Wgpu;
import com.noahcharlton.wgpuj.jni.WgpuBindingType;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuInputStepMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.jni.WgpuVertexFormat;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.EnumSet;

public class WindowEventExample {

    private static final float SIZE = 50;

    private static final float[] VERTICES = new float[]{
            -SIZE, -SIZE, 1f, 0f, 0f,
            -SIZE, SIZE, 0f, 1f, 0f,
            SIZE, SIZE, 0f, 0f, 1f,
            SIZE, -SIZE, 1f, 1f, 1f,
    };

    private static final short[] INDICES = new short[]{
            0, 1, 2,
            0, 2, 3
    };

    private static final int FLOATS_PER_VERTEX = 5;

    private final Matrix4f viewMatrix = new Matrix4f().lookAt(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, -1),
            MathUtils.UNIT_Y
    );

    private final Window window;
    private final Device device;
    private final Queue queue;
    private final Buffer vertexBuffer;
    private final Buffer indexBuffer;
    private final Buffer matrixBuffer;
    private final long bindGroup;
    private final EnumSet<Key> keysPressed = EnumSet.noneOf(Key.class);

    public WindowEventExample() {
        GraphicApplicationConfig appConfig = new GraphicApplicationConfig("Wgpu-Java event example", 640, 480);

        try(var application = WgpuGraphicApplication.create(appConfig)) {
            device = application.getDevice();
            window = application.getWindow();
            queue = application.getDefaultQueue();
            window.setEventHandler(new WindowHandler(this));

            vertexBuffer = device.createVertexBuffer("Vertex buffer", VERTICES);
            indexBuffer = device.createIndexBuffer("Index buffer", INDICES);

            Matrix4f projection = createProjectionMatrix();
            Matrix4f transformationMatrix = MatrixUtils.generateTransMatrix(projection, viewMatrix);

            matrixBuffer = device.createFloatBuffer("Matrix", MatrixUtils.toFloats(transformationMatrix),
                    BufferUsage.UNIFORM, BufferUsage.COPY_DST);

            var bindGroupLayout = device.createBindGroupLayout("matrix group layout",
                    BindGroupUtils.partialLayout(0, Wgpu.ShaderStage.VERTEX, WgpuBindingType.UNIFORM_BUFFER));

            bindGroup = device.createBindGroup("matrix bind group", bindGroupLayout,
                            BindGroupUtils.bufferEntry(0, matrixBuffer));

            RenderPipelineSettings renderPipelineSettings = createPipelineSettings(device);
            renderPipelineSettings.setBindGroupLayouts(bindGroupLayout);
            application.initializeSwapChain();

            runMainLoop(application, device.createRenderPipeline(renderPipelineSettings));
        }
    }

    private Matrix4f createProjectionMatrix() {
        Dimension dimension = window.getWindowDimension();
        var projectionWidth = dimension.getWidth() / 2f;
        var projectionHeight = dimension.getHeight() / 2f;

        return new Matrix4f().ortho2D(-projectionWidth, projectionWidth,
                -projectionHeight, projectionHeight);
    }

    private void updateMatrix() {
        Matrix4f projection = createProjectionMatrix();
        Matrix4f transformationMatrix = MatrixUtils.generateTransMatrix(projection, viewMatrix);

        queue.writeFloatsToBuffer(matrixBuffer, MatrixUtils.toFloats(transformationMatrix));
    }

    private void runMainLoop(WgpuGraphicApplication application, RenderPipeline pipeline) {
        while(!application.getWindow().isCloseRequested()) {
            updateInput();

            var renderPass = application.renderStart(Color.BLACK);
            renderPass.setPipeline(pipeline);
            renderPass.setBindGroup(0, bindGroup);
            renderPass.setIndexBuffer(indexBuffer);
            renderPass.setVertexBuffer(vertexBuffer, 0);

            renderPass.drawIndexed(INDICES.length, 1, 0);

            application.renderEnd();
            application.update();
        }
    }

    private void updateInput() {
        boolean dirty = false;
        float speed = 4f;
        float rotateSpeed = .1f;

        if(keysPressed.contains(Key.W)) {
            viewMatrix.translate(0f, speed, 0f);
            dirty = true;
        }

        if(keysPressed.contains(Key.A)) {
            viewMatrix.translate(-speed, 0f, 0f);
            dirty = true;
        }

        if(keysPressed.contains(Key.S)) {
            viewMatrix.translate(0f, -speed, 0f);
            dirty = true;
        }

        if(keysPressed.contains(Key.D)) {
            viewMatrix.translate(speed, 0f, 0f);
            dirty = true;
        }

        if(keysPressed.contains(Key.Q)) {
            viewMatrix.rotate(rotateSpeed, MathUtils.UNIT_Z);
            dirty = true;
        }

        if(keysPressed.contains(Key.E)) {
            viewMatrix.rotate(-rotateSpeed, MathUtils.UNIT_Z);
            dirty = true;
        }

        if(dirty)
            updateMatrix();
    }

    private RenderPipelineSettings createPipelineSettings(Device device) {
        ShaderConfig vertex = ShaderConfig.fromRawClasspathFile("/window.vert", "main");
        ShaderConfig fragment = ShaderConfig.fromRawClasspathFile("/window.frag", "main");

        return new RenderPipelineSettings()
                .setVertexStage(device.createShaderModule(vertex))
                .setFragmentStage(device.createShaderModule(fragment))
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
                        Float.BYTES * FLOATS_PER_VERTEX,
                        WgpuInputStepMode.VERTEX,
                        Buffer.vertexAttribute(0, WgpuVertexFormat.FLOAT2, 0),
                        Buffer.vertexAttribute(Float.BYTES * 2, WgpuVertexFormat.FLOAT3, 1)))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts();
    }

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();

        new WindowEventExample();
    }

    static class WindowHandler implements WindowEventHandler {

        private final WindowEventExample app;

        public WindowHandler(WindowEventExample app) {
            this.app = app;
        }

        @Override
        public void onResize() {
            app.updateMatrix();
        }

        @Override
        public void onKeyPressed(Key key) {
            app.keysPressed.add(key);
        }

        @Override
        public void onKeyReleased(Key key) {
            app.keysPressed.remove(key);
        }
    }
}
