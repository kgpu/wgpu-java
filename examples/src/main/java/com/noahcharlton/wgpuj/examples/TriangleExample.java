package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.util.ShaderConfig;
import com.noahcharlton.wgpuj.core.util.ShaderModule;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.*;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.jni.Wgpu;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;

public class TriangleExample {

    public static void main(String[] args){
        WgpuCore.loadWgpuNative();

        GraphicApplicationSettings appSettings = new GraphicApplicationSettings("Wgpu-Java example", 640, 480);

        try(var application = WgpuGraphicApplication.create(appSettings)) {
            RenderPipelineSettings renderPipelineSettings = createPipelineSettings(application.getDevice());
            RenderPipeline pipeline = application.getDevice().createRenderPipeline(renderPipelineSettings);
            application.initializeSwapChain();

            while(!application.getWindow().isCloseRequested()){
                var renderPass = application.renderStart(Color.BLACK);
                renderPass.setPipeline(pipeline);
                renderPass.draw(3, 1);

                application.renderEnd();
                application.update();
            }
        }
    }

    private static RenderPipelineSettings createPipelineSettings(Device device){
        ShaderConfig vertex = ShaderConfig.fromRawClasspathFile("/triangle.vert", "main");
        ShaderConfig fragment = ShaderConfig.fromRawClasspathFile("/triangle.frag", "main");

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
                .setBufferLayouts()
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts();
    }

}
