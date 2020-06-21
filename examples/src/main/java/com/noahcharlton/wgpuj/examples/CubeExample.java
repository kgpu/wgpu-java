package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.BlendDescriptor;
import com.noahcharlton.wgpuj.core.graphics.ColorState;
import com.noahcharlton.wgpuj.core.graphics.GraphicApplicationSettings;
import com.noahcharlton.wgpuj.core.graphics.RasterizationState;
import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.graphics.Window;
import com.noahcharlton.wgpuj.core.math.MathUtils;
import com.noahcharlton.wgpuj.core.math.MatrixUtils;
import com.noahcharlton.wgpuj.core.util.BindGroupUtils;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.BufferUsage;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.jni.Wgpu;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
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
import jnr.ffi.Pointer;
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

        RenderPipelineSettings renderPipelineSettings = createPipelineSettings();
        GraphicApplicationSettings appSettings = new GraphicApplicationSettings("Wgpu-Java cube example", 302, 332);

        Matrix4f viewMatrix = new Matrix4f().lookAt(
                new Vector3f(1.5f, -5f, 3.0f),
                new Vector3f(0f, 0f, 0f),
                MathUtils.UNIT_Z
        );

        try(var application = WgpuGraphicApplication.create(appSettings)) {
            Device device = application.getDevice();
            Matrix4f matrix = updateMatrix(application.getWindow(), viewMatrix);

            var vertices = device.createVertexBuffer("Vertices", VERTICES);
            var indices = device.createIndexBuffer("Indices", INDICES);
            var matrixBuffer = device.createFloatBuffer("Matrix", matrix.get(new float[16]),
                    BufferUsage.UNIFORM, BufferUsage.COPY_DST);

            var bindGroupLayout = device.createBindGroupLayout("matrix group layout",
                    BindGroupUtils.partialLayout(0, Wgpu.ShaderStage.VERTEX,
                            WgpuBindingType.UNIFORM_BUFFER));

            var bindGroup = device.createBindGroup("matrix bind group", bindGroupLayout,
                            new WgpuBindGroupEntry().setBuffer(0, matrixBuffer.getId(), matrixBuffer.getSize()));

            renderPipelineSettings.setBindGroupLayouts(bindGroupLayout);
            application.init(renderPipelineSettings);

            while(!application.getWindow().isCloseRequested()) {
                var renderPass = application.renderStart();
                renderPass.setBindGroup(0, bindGroup);
                renderPass.setVertexBuffer(vertices, 0);
                renderPass.setIndexBuffer(indices);

                renderPass.drawIndexed(INDICES.length, 1, 0);

                application.renderEnd();

                var newMatrix = updateMatrix(application.getWindow(), viewMatrix);
                float[] matrixData = newMatrix.get(new float[16]);

                Pointer pointer = WgpuJava.createDirectPointer(16 * Float.BYTES);
                pointer.put(0, matrixData, 0, 16);
                WgpuJava.wgpuNative.wgpu_queue_write_buffer(application.getQueue(), matrixBuffer.getId(), 0,
                        pointer, 16 * Float.BYTES);
            }
        }
    }

    private static Matrix4f updateMatrix(Window window, Matrix4f view) {
        Dimension dimension = window.getWindowDimension();
        float aspectRatio = (float) dimension.getWidth() / dimension.getHeight();
        Matrix4f projection = new Matrix4f().perspective(MathUtils.toRadians(45f), aspectRatio, 1, 10);

        view.rotate(.01f, MathUtils.UNIT_Z);

        return MatrixUtils.generateMatrix(projection, view);
    }

    private static RenderPipelineSettings createPipelineSettings() {
        ShaderData vertex = ShaderData.fromRawClasspathFile("/cube.vert", "main");
        ShaderData fragment = ShaderData.fromRawClasspathFile("/cube.frag", "main");

        return new RenderPipelineSettings()
                .setVertexStage(vertex)
                .setFragmentStage(fragment)
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
                .setBindGroupLayouts()
                .setClearColor(Color.BLACK);
    }
}
