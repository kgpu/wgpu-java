package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.util.ShaderConfig;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.*;
import com.noahcharlton.wgpuj.core.util.Buffer;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.jni.Wgpu;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuInputStepMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.jni.WgpuVertexFormat;

public class VertexExample {

    private static final float[] VERTICES = new float[]{
            .5f, -.2f, 0f,
            .5f, .2f, 0f,
            .2f, .5f, 0f,
            -.2f, .5f, 0f,
            -.5f, .2f, 0f,
            -.5f, -.2f, 0f,
            -.2f, -.5f, 0f,
            .2f, -.5f, 0f,
            0f, 0f, 0f
    };

    private static final short[] INDICES = new short[]{
            1, 8, 0,
            2, 8, 1,
            3, 8, 2,
            4, 8, 3,
            5, 8, 4,
            6, 8, 5,
            7, 8, 6,
            0, 8, 7
    };

    private static final int FLOATS_PER_VERTEX = 3;

    public static void main(String[] args){
        WgpuCore.loadWgpuNative();

        GraphicApplicationConfig appConfig = new GraphicApplicationConfig("Wgpu-Java vertex example", 302, 302);

        try(var application = WgpuGraphicApplication.create(appConfig)) {
            Device device = application.getDevice();
            RenderPipelineConfig pipelineConfig = createPipelineConfig(device);
            RenderPipeline pipeline = device.createRenderPipeline(pipelineConfig);

            var vertexBuffer = device.createVertexBuffer("Vertex Buffer", VERTICES);
            var indexBuffer = device.createIndexBuffer("Index Buffer", INDICES);

            application.initializeSwapChain();

            while(!application.getWindow().isCloseRequested()){
                var renderPass = application.renderStart(Color.BLACK);
                renderPass.setIndexBuffer(indexBuffer);
                renderPass.setVertexBuffer(vertexBuffer, 0);
                renderPass.setPipeline(pipeline);

                renderPass.drawIndexed(INDICES.length, 1, 0);

                application.renderEnd();
                application.update();
            }
        }
    }

    private static RenderPipelineConfig createPipelineConfig(Device device){
        ShaderConfig vertex = ShaderConfig.fromRawClasspathFile("/vertex.vert", "main");
        ShaderConfig fragment = ShaderConfig.fromRawClasspathFile("/vertex.frag", "main");

        return new RenderPipelineConfig()
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
                        Buffer.vertexAttribute(0, WgpuVertexFormat.FLOAT3, 0)))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts();
    }
}
