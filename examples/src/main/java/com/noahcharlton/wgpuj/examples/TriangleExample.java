package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.BlendDescriptor;
import com.noahcharlton.wgpuj.core.graphics.ColorState;
import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.graphics.GraphicApplicationSettings;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuRasterizationStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;

public class TriangleExample {

    public static void main(String[] args){
        WgpuCore.loadWgpuNative();

        RenderPipelineSettings renderPipelineSettings = createPipelineSettings();
        GraphicApplicationSettings appSettings = new GraphicApplicationSettings("Wgpu-Java example", 640, 480);

        try(var application = WgpuGraphicApplication.create(appSettings)) {
            application.init(renderPipelineSettings);

            while(!application.getWindow().isCloseRequested()){
                var renderPass = application.renderStart();
                renderPass.draw(3, 1);

                application.renderEnd();
            }
        }
    }

    private static RenderPipelineSettings createPipelineSettings(){
        ShaderData vertex = ShaderData.fromRawClasspathFile("/triangle.vert", "main");
        ShaderData fragment = ShaderData.fromRawClasspathFile("/triangle.frag", "main");

        return new RenderPipelineSettings()
                .setVertexStage(vertex)
                .setFragmentStage(fragment)
                .setRasterizationState(new WgpuRasterizationStateDescriptor(
                        WgpuFrontFace.CCW,
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
                .setVertexIndexFormat(WgpuIndexFormat.UINT16)
                .setBufferLayouts()
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts()
                .setClearColor(Color.BLACK);
    }

}
