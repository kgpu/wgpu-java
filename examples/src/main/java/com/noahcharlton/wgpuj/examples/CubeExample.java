package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.BlendDescriptor;
import com.noahcharlton.wgpuj.core.graphics.ColorState;
import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.graphics.Window;
import com.noahcharlton.wgpuj.core.graphics.WindowSettings;
import com.noahcharlton.wgpuj.core.math.MathUtils;
import com.noahcharlton.wgpuj.core.math.MatrixUtils;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.BufferUsage;
import com.noahcharlton.wgpuj.core.util.Dimension;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindingType;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuInputStepMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuRasterizationStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferAttributeDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexFormat;
import jnr.ffi.Pointer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CubeExample {

    private static final float[] VERTICES = new float[]{
//            //Bottom Rectangle
//            -.5f, -.5f, -.5f, 1f, 0f, 0f,
//            -.5f, .5f, -.5f, 1f, 0f, 0f,
//            .5f, .5f, -.5f, 1f, 0f, 0f,
//            .5f, -.5f, -.5f, 1f, 0f, 0f,
//
//            //Top Rectangle
//            -.5f, -.5f, .5f, 0f, 0f, 1f,
//            -.5f, .5f, .5f, 0f, 0f, 1f,
//            .5f, .5f, .5f, 0f, 0f, 1f,
//            .5f, -.5f, .5f, 0f, 0f, 1f,
//
//            -.5f, .5f, -.5f, .25f, 0f, .75f,
//            -.5f, .5f, .5f, .25f, 0f, .75f,
//            -.5f, -.5f, -.5f, .25f, 0f, .75f,
//            -.5f, -.5f, .5f, .25f, 0f, .75f,

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
//            0, 1, 2,
//            0, 2, 3,
//
//            4, 5, 6,
//            4, 6, 7,
//
//            8, 10, 9,
//            9, 10, 11

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
        WindowSettings windowSettings = new WindowSettings("Wgpu-Java cube example", 302, 332);

        Matrix4f viewMatrix = new Matrix4f().lookAt(
                new Vector3f(1.5f, -5f, 3.0f),
                new Vector3f(0f, 0f, 0f),
                MathUtils.UNIT_Z
        );

        try(var application = new WgpuGraphicApplication(windowSettings)) {
            Matrix4f matrix = updateMatrix(application.getWindow(), viewMatrix);

            var vertices = Buffer.createVertexBuffer("Vertices", VERTICES, application.getDevice());
            var indices = Buffer.createIndexBuffer("Indices", INDICES, application.getDevice());
            var matrixBuffer = Buffer.createFloatBuffer("Matrix", matrix.get(new float[16]),
                    application.getDevice(), BufferUsage.UNIFORM, BufferUsage.COPY_DST);

            var bindGroupLayout = WgpuJava.wgpuNative.
                    wgpu_device_create_bind_group_layout(application.getDevice(), new WgpuBindGroupLayoutDescriptor(
                            "matrix group layout",
                            new WgpuBindGroupLayoutEntry()
                                    .setPartial(0, WgpuBindGroupLayoutEntry.SHADER_STAGE_VERTEX,
                                            WgpuBindingType.UNIFORM_BUFFER)
                    ).getPointerTo());

            var bindGroup = WgpuJava.wgpuNative.wgpu_device_create_bind_group(application.getDevice(),
                    new WgpuBindGroupDescriptor(
                            "matrix bind group",
                            bindGroupLayout,
                            new WgpuBindGroupEntry().setBuffer(0, matrixBuffer.getId(), matrixBuffer.getSize())
                    ).getPointerTo());

            renderPipelineSettings.setBindGroupLayouts(bindGroupLayout);
            application.init(renderPipelineSettings);

            while(!application.getWindow().isCloseRequested()) {
                var swapChain = application.renderStart();
                swapChain.setBindGroup(0, bindGroup);
                swapChain.setVertexBuffer(vertices, 0);
                swapChain.setIndexBuffer(indices);

                swapChain.drawIndexed(INDICES.length, 1, 0);

                swapChain.renderEnd();

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
                .setRasterizationState(new WgpuRasterizationStateDescriptor(
                        WgpuFrontFace.COUNTER_CLOCKWISE,
                        WgpuCullMode.BACK,
                        0,
                        0.0f,
                        0.0f).getPointerTo())
                .setPrimitiveTopology(WgpuPrimitiveTopology.TRIANGLE_LIST)
                .setColorStates(new ColorState(
                        WgpuTextureFormat.BGRA8_UNORM,
                        new BlendDescriptor(WgpuBlendFactor.ONE, WgpuBlendFactor.ZERO, WgpuBlendOperation.ADD),
                        new BlendDescriptor(WgpuBlendFactor.ONE, WgpuBlendFactor.ZERO, WgpuBlendOperation.ADD),
                        WgpuColorStateDescriptor.ALL).build())
                .setDepthStencilState(WgpuJava.createNullPointer())
                .setVertexIndexFormat(WgpuIndexFormat.Uint16)
                .setBufferLayouts(new WgpuVertexBufferLayoutDescriptor(
                        Float.BYTES * FLOATS_PER_VERTEX,
                        WgpuInputStepMode.VERTEX,
                        new WgpuVertexBufferAttributeDescriptor(0, WgpuVertexFormat.FLOAT3, 0),
                        new WgpuVertexBufferAttributeDescriptor(3 * Float.BYTES, WgpuVertexFormat.FLOAT3, 1)
                ))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts();
    }
}