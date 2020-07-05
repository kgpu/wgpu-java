package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.core.*;
import com.noahcharlton.wgpuj.core.graphics.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HexWorldExample implements AutoCloseable {

    private static final int DEFAULT_WINDOW_WIDTH = 720;
    private static final int DEFAULT_WINDOW_HEIGHT = 550;

    private static final float RADIUS = 50f;
    private static final float HEX_X = (float) (RADIUS * Math.cos(MathUtils.toRadians(60)));
    private static final float HEX_Y = (float) (RADIUS * Math.sin(MathUtils.toRadians(60)));

    private static final float[] VERTICES = new float[]{
            0f, 0f,
            RADIUS, 0f,
            HEX_X, HEX_Y,
            -HEX_X, HEX_Y,
            -RADIUS, 0f,
            -HEX_X, -HEX_Y,
            HEX_X, -HEX_Y
    };

    private static final int FLOATS_PER_VERTEX = 2;

    private static final short[] INDICES = new short[]{
            0, 1, 2,
            0, 2, 3,
            0, 3, 4,
            0, 4, 5,
            0, 5, 6,
            0, 6, 1
    };

    private static final Matrix4f[] INSTANCES = generateTiles();

    private final WgpuGraphicApplication application;
    private final Device device;
    private final Queue queue;

    private Buffer vertexBuffer;
    private Buffer indexBuffer;
    private Buffer transMatrixBuffer;
    private Matrix4f viewMatrix;
    private RenderPipeline pipeline;
    private long bindGroup;

    public HexWorldExample(GraphicApplicationSettings settings) {
        application = WgpuGraphicApplication.create(settings);
        device = application.getDevice();
        queue = application.getDefaultQueue();
        viewMatrix = new Matrix4f();
    }

    private void init() {
        float[] instanceModels = new float[16 * INSTANCES.length];
        float[] instanceColors = new float[4 * INSTANCES.length];
        Random random = new Random();

        for(int i = 0; i < INSTANCES.length; i++){
            var instance = MatrixUtils.toFloats(INSTANCES[i]);
            System.arraycopy(instance, 0, instanceModels, i * 16, 16);

            instanceColors[i * 4] = random.nextFloat() / 5f + .3f;
            instanceColors[i * 4 + 1] = random.nextFloat() / 10f + .2f;
            instanceColors[i * 4 + 2] = 0f;
            instanceColors[i * 4 + 3] = 1f;
        }

        var modelsBuffer = device.createFloatBuffer("Instance Models", instanceModels, BufferUsage.STORAGE);
        var colorsBuffer = device.createFloatBuffer("Instance Color", instanceColors, BufferUsage.STORAGE);
        var transformationMatrix = createTransformationMatrix();
        transMatrixBuffer = device.createFloatBuffer("Matrix", MatrixUtils.toFloats(transformationMatrix),
                BufferUsage.UNIFORM, BufferUsage.COPY_DST);

        var bindGroupLayout = device.createBindGroupLayout("matrix group layout",
                BindGroupUtils.partialLayout(0, Wgpu.ShaderStage.VERTEX, WgpuBindingType.UNIFORM_BUFFER),
                BindGroupUtils.partialLayout(1, Wgpu.ShaderStage.VERTEX, WgpuBindingType.STORAGE_BUFFER),
                BindGroupUtils.partialLayout(2, Wgpu.ShaderStage.VERTEX, WgpuBindingType.STORAGE_BUFFER));

        bindGroup = device.createBindGroup("matrix bind group", bindGroupLayout,
                BindGroupUtils.bufferEntry(0, transMatrixBuffer),
                BindGroupUtils.bufferEntry(1, modelsBuffer),
                BindGroupUtils.bufferEntry(2, colorsBuffer));

        var pipelineSettings = createRenderPipelineSettings(device);
        pipelineSettings.setBindGroupLayouts(bindGroupLayout);

        vertexBuffer = device.createVertexBuffer("Vertices", VERTICES);
        indexBuffer = device.createIndexBuffer("Indices", INDICES);
        pipeline = device.createRenderPipeline(pipelineSettings);
        application.initializeSwapChain();
    }

    private RenderPipelineSettings createRenderPipelineSettings(Device device) {
        ShaderConfig vertex = ShaderConfig.fromRawClasspathFile("/hex_world.vert", "main");
        ShaderConfig fragment = ShaderConfig.fromRawClasspathFile("/hex_world.frag", "main");

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
                        Buffer.vertexAttribute(0, WgpuVertexFormat.FLOAT2, 0)))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts();
    }

    private void run() {
        while(!application.getWindow().isCloseRequested()) {
            var renderPass = application.renderStart(Color.BLACK);

            renderPass.setPipeline(pipeline);
            renderPass.setBindGroup(0, bindGroup);
            renderPass.setVertexBuffer(vertexBuffer, 0);
            renderPass.setIndexBuffer(indexBuffer);
            renderPass.drawIndexed(INDICES.length, INSTANCES.length, 0);

            application.renderEnd();
            application.update();
            queue.writeFloatsToBuffer(transMatrixBuffer, MatrixUtils.toFloats(createTransformationMatrix()));
        }
    }

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();

        var settings = new GraphicApplicationSettings("HexWorld", DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

        try(var hexWorld = new HexWorldExample(settings)) {
            hexWorld.init();
            hexWorld.run();
        }
    }

    private Matrix4f createTransformationMatrix() {
        return MatrixUtils.generateTransMatrix(createProjectionMatrix(), viewMatrix);
    }

    private Matrix4f createProjectionMatrix() {
        Dimension dimension = application.getWindow().getWindowDimension();
        var projectionWidth = dimension.getWidth() / 2f;
        var projectionHeight = dimension.getHeight() / 2f;

        return new Matrix4f().ortho2D(-projectionWidth, projectionWidth,
                -projectionHeight, projectionHeight);
    }

    private static Matrix4f[] generateTiles() {
        List<Matrix4f> matrices = new ArrayList<>();
        int rows = 7;
        int columns = 7;

        float startX = -rows * RADIUS / 2f;
        float startY = -columns * HEX_Y / 2f;

        for(int x = 0; x < rows; x++){
            for(int y = 0; y < columns; y++){
                Matrix4f matrix = new Matrix4f();
                float xPos = startX + 1.5f * x * RADIUS;
                float yPos = startY + 2 * y * HEX_Y;

                if(x % 2 == 1){
                    yPos -= HEX_Y;
                }

                matrix.translate(xPos, yPos, 0f);

                matrices.add(matrix);
            }
        }

        return matrices.toArray(new Matrix4f[0]);
    }

    @Override
    public void close() {
        application.close();
    }
}
