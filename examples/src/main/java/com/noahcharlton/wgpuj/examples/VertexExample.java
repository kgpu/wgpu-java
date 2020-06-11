package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.BlendDescriptor;
import com.noahcharlton.wgpuj.core.graphics.ColorState;
import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.graphics.WindowSettings;
import com.noahcharlton.wgpuj.core.util.BufferSettings;
import com.noahcharlton.wgpuj.core.util.BufferUsage;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuInputStepMode;
import com.noahcharlton.wgpuj.jni.WgpuLogLevel;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuRasterizationStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferAttributeDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferLayoutDescriptor;
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
        WgpuJava.setWgpuLogLevel(WgpuLogLevel.INFO);

        RenderPipelineSettings renderPipelineSettings = createPipelineSettings();
        WindowSettings windowSettings = new WindowSettings("Wgpu-Java vertex example", 302, 302);

        try(var application = new WgpuGraphicApplication(windowSettings)){
            var vertexBuffer = new BufferSettings()
                    .setLabel("Vertex buffer")
                    .setSize(FLOATS_PER_VERTEX * Float.BYTES * VERTICES.length)
                    .setUsages(BufferUsage.VERTEX)
                    .setMapped(true)
                    .createBuffer(application.getDevice());
            vertexBuffer.getMappedData().put(0, VERTICES, 0, VERTICES.length);
            vertexBuffer.unmap();

            var indexBuffer = new BufferSettings()
                    .setLabel("Index buffer")
                    .setSize(Float.BYTES * INDICES.length)
                    .setUsages(BufferUsage.INDEX)
                    .setMapped(true)
                    .createBuffer(application.getDevice());
            indexBuffer.getMappedData().put(0, INDICES, 0, INDICES.length);
            indexBuffer.unmap();

            application.init(renderPipelineSettings);

            while(!application.getWindow().isCloseRequested()){
                var swapChain = application.renderStart();
                swapChain.setIndexBuffer(indexBuffer);
                swapChain.setVertexBuffer(vertexBuffer, 0);

                swapChain.drawIndexed(VERTICES.length, 1, 0);

                swapChain.renderEnd();
            }
        }
    }

    private static RenderPipelineSettings createPipelineSettings(){
        ShaderData vertex = ShaderData.fromClasspathFile("/vertex.vert.spv", "main");
        ShaderData fragment = ShaderData.fromClasspathFile("/vertex.frag.spv", "main");

        return new RenderPipelineSettings()
                .setVertexStage(vertex)
                .setFragmentStage(fragment)
                .setRasterizationState(new WgpuRasterizationStateDescriptor(
                        WgpuFrontFace.COUNTER_CLOCKWISE,
                        WgpuCullMode.NONE,
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
                        new WgpuVertexBufferAttributeDescriptor(0, WgpuVertexFormat.FLOAT3, 0)))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts();
    }
}
