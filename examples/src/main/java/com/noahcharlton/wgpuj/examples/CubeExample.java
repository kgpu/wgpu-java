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
import org.joml.Vector3f;

public class CubeExample {

    private static final float[] VERTICES = new float[]{
            -1, -1, 1, 1f, 0f, 0f,
            1, -1, 1, 1f, 0f, 0f,
            1, 1, 1, 1f, 0f, 0f,
            -1, 1, 1, 1f, 0f, 0f,

            -1, 1, -1, 0f, 0f, 1f,
            1, 1, -1, 0f, 0f, 1f,
            1, -1, -1, 0f, 0f, 1f,
            -1, -1, -1, 0f, 0f, 1f,

            1, -1, -1, .25f, .4f, .5f,
            1, 1, -1, .25f, .4f, .5f,
            1, 1, 1, .25f, .4f, .5f,
            1, -1, 1, .25f, .4f, .5f,

            -1, -1, 1, .7f, .7f, .2f,
            -1, 1, 1, .7f, .7f, .2f,
            -1, 1, -1, .7f, .7f, .2f,
            -1, -1, -1, .7f, .7f, .2f,

            1, 1, -1, .1f, .4f, .1f,
            -1, 1, -1, .1f, .4f, .1f,
            -1, 1, 1, .1f, .4f, .1f,
            1, 1, 1, .1f, .4f, .1f,

            1, -1, 1, .9f, .5f, .5f,
            -1, -1, 1, .9f, .5f, .5f,
            -1, -1, -1, .9f, .5f, .5f,
            1, -1, -1, .9f, .5f, .5f,
    };

    private static final int FLOATS_PER_VERTEX = 6;

    private static final short[] INDICES = new short[]{
            0, 1, 2, 2, 3, 0,
            4, 5, 6, 6, 7, 4,
            8, 9, 10, 10, 11, 8,
            12, 13, 14, 14, 15, 12,
            16, 17, 18, 18, 19, 16,
            20, 21, 22, 22, 23, 20
    };

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();

        GraphicApplicationSettings appSettings = new GraphicApplicationSettings("Wgpu-Java cube example", 302, 332);

        Matrix4f viewMatrix = new Matrix4f().lookAt(
                new Vector3f(1.5f, -5f, 3.0f),
                new Vector3f(0f, 0f, 0f),
                MathUtils.UNIT_Z
        );

        try(var application = WgpuGraphicApplication.create(appSettings)) {
            Device device = application.getDevice();
            Queue queue = application.getDefaultQueue();
            Matrix4f matrix = updateMatrix(application.getWindow(), viewMatrix);

            var vertices = device.createVertexBuffer("Vertices", VERTICES);
            var indices = device.createIndexBuffer("Indices", INDICES);
            var matrixBuffer = device.createFloatBuffer("Matrix", matrix.get(new float[16]),
                    BufferUsage.UNIFORM, BufferUsage.COPY_DST);

            var bindGroupLayout = device.createBindGroupLayout("matrix group layout",
                    BindGroupUtils.partialLayout(0, Wgpu.ShaderStage.VERTEX, WgpuBindingType.UNIFORM_BUFFER));

            var bindGroup = device.createBindGroup("matrix bind group", bindGroupLayout,
                            BindGroupUtils.bufferEntry(0, matrixBuffer));

            RenderPipelineSettings renderPipelineSettings = createPipelineSettings(device);
            renderPipelineSettings.setBindGroupLayouts(bindGroupLayout);
            RenderPipeline pipeline = device.createRenderPipeline(renderPipelineSettings);
            application.initializeSwapChain();

            while(!application.getWindow().isCloseRequested()) {
                var renderPass = application.renderStart(Color.BLACK);
                renderPass.setPipeline(pipeline);
                renderPass.setBindGroup(0, bindGroup);
                renderPass.setVertexBuffer(vertices, 0);
                renderPass.setIndexBuffer(indices);

                renderPass.drawIndexed(INDICES.length, 1, 0);

                application.renderEnd();
                application.update();

                var newMatrix = updateMatrix(application.getWindow(), viewMatrix);
                queue.writeFloatsToBuffer(matrixBuffer, MatrixUtils.toFloats(newMatrix));
            }
        }
    }

    private static Matrix4f updateMatrix(Window window, Matrix4f view) {
        Dimension dimension = window.getWindowDimension();
        float aspectRatio = (float) dimension.getWidth() / dimension.getHeight();
        Matrix4f projection = new Matrix4f().perspective(MathUtils.toRadians(45f), aspectRatio, 1, 10);

        view.rotate(.01f, MathUtils.UNIT_Z);

        return MatrixUtils.generateTransMatrix(projection, view);
    }

    private static RenderPipelineSettings createPipelineSettings(Device device) {
        var vertex = ShaderConfig.fromRawClasspathFile("/cube.vert", "main");
        var fragment = ShaderConfig.fromRawClasspathFile("/cube.frag", "main");

        return new RenderPipelineSettings()
                .setVertexStage(device.createShaderModule(vertex))
                .setFragmentStage(device.createShaderModule(fragment))
                .setRasterizationState(RasterizationState.of(
                        WgpuFrontFace.CCW,
                        WgpuCullMode.BACK,
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
                        Buffer.vertexAttribute(0, WgpuVertexFormat.FLOAT3, 0),
                        Buffer.vertexAttribute(3 * Float.BYTES, WgpuVertexFormat.FLOAT3, 1)
                ))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts();
    }
}
