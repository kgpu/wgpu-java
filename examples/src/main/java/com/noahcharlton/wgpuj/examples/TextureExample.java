package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.*;
import com.noahcharlton.wgpuj.core.graphics.*;
import com.noahcharlton.wgpuj.core.util.*;
import com.noahcharlton.wgpuj.jni.*;

import java.io.IOException;

public class TextureExample {

    /**
     * Each vertex has 5 floats:
     * <ul>
     * <li> First 3 are the position
     * <li> Second 2 are texture coordinate
     * </ul>
     */
    private static final float[] VERTICES = new float[]{
            -.5f, -.5f, 0f, 0, 1,
            - .5f, .5f, 0f, 0, 0,
            .5f, .5f, 0f, 1, 0,
            .5f, -.5f, 0f, 1, 1
    };

    private static final int FLOATS_PER_VERTEX = 5;

    private static final short[] INDICES = new short[]{
            0, 1, 2,
            0, 2, 3
    };

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();

        ImageData texture = loadTexture();
        GraphicApplicationConfig appConfig = new GraphicApplicationConfig("Wgpu-Java Texture Example", 350, 350);

        try(var application = WgpuGraphicApplication.create(appConfig)) {
            Device device = application.getDevice();
            Queue queue = application.getDefaultQueue();

            var vertexBuffer = device.createVertexBuffer("Vertex Buffer", VERTICES);
            var indexBuffer = device.createIndexBuffer("index buffer", INDICES);

            var textureDesc = WgpuTextureDescriptor.createDirect();
            textureDesc.setLabel("Texture");
            textureDesc.setDimension(WgpuTextureDimension.D2);
            textureDesc.getSize().setWidth(texture.getWidth());
            textureDesc.getSize().setHeight(texture.getHeight());
            textureDesc.getSize().setDepth(1);
            textureDesc.setMipLevelCount(1);
            textureDesc.setSampleCount(1);
            textureDesc.setFormat(WgpuTextureFormat.RGBA8_UNORM_SRGB);
            textureDesc.setUsage(Wgpu.TextureUsage.COPY_DST | Wgpu.TextureUsage.SAMPLED);
            long textureId = device.createTexture(textureDesc);
            var textureBuffer = device.createIntBuffer("texture buffer", texture.getPixels(), BufferUsage.COPY_SRC);

            CommandEncoder encoder = device.createCommandEncoder("Command Encoder");
            encoder.copyBufferToTexture(textureBuffer, textureId, texture);
            long commandBuffer = encoder.finish(WgpuCommandBufferDescriptor.createDirect());
            queue.submit(commandBuffer);

            var samplerDesc = WgpuSamplerDescriptor.createDirect();
            samplerDesc.setAddressModeU(WgpuAddressMode.CLAMP_TO_EDGE);
            samplerDesc.setAddressModeW(WgpuAddressMode.CLAMP_TO_EDGE);
            samplerDesc.setAddressModeV(WgpuAddressMode.CLAMP_TO_EDGE);
            samplerDesc.setMagFilter(WgpuFilterMode.NEAREST);
            samplerDesc.setMinFilter(WgpuFilterMode.NEAREST);
            samplerDesc.setMipmapFilter(WgpuFilterMode.NEAREST);
            samplerDesc.setLodMinClamp(-100f);
            samplerDesc.setLodMaxClamp(100f);
            samplerDesc.setCompare(WgpuCompareFunction.ALWAYS);

            long textureView = WgpuJava.wgpuNative.wgpu_texture_create_view(textureId, WgpuJava.createNullPointer());
            long sampler = WgpuJava.wgpuNative.wgpu_device_create_sampler(application.getDevice().getId(),
                    samplerDesc.getPointerTo());

            var textureBindGroupLayout = device.createBindGroupLayout("texture bind group layout",
                    BindGroupUtils.textureLayout(0, Wgpu.ShaderStage.FRAGMENT, WgpuBindingType.SAMPLED_TEXTURE,
                            false, WgpuTextureViewDimension.D2, WgpuTextureComponentType.UINT),
                    BindGroupUtils.samplerLayout(1, Wgpu.ShaderStage.FRAGMENT,
                                    WgpuBindingType.SAMPLER, false));

            var textureBindGroup = device.createBindGroup("texture bind group", textureBindGroupLayout,
                    BindGroupUtils.textureViewEntry(0, textureView),
                    BindGroupUtils.samplerEntry(1, sampler));

            RenderPipelineSettings renderPipelineSettings = createPipelineSettings(device);
            renderPipelineSettings.setBindGroupLayouts(textureBindGroup);
            application.initializeSwapChain();
            RenderPipeline pipeline = device.createRenderPipeline(renderPipelineSettings);

            while(!application.getWindow().isCloseRequested()) {
                RenderPass renderPass = application.renderStart(Color.BLACK);
                renderPass.setPipeline(pipeline);
                renderPass.setBindGroup(0, textureBindGroup);
                renderPass.setIndexBuffer(indexBuffer);
                renderPass.setVertexBuffer(vertexBuffer, 0);
                renderPass.drawIndexed(INDICES.length, 1, 0);

                application.renderEnd();
                application.update();
            }
        }
    }

    static ImageData loadTexture() {
        try {
            return ImageData.fromFile("/texture.png");
        } catch(IOException | RuntimeException e) {
            throw new RuntimeException("Failed to load example texture", e);
        }
    }

    private static RenderPipelineSettings createPipelineSettings(Device device) {
        ShaderConfig vertex = ShaderConfig.fromRawClasspathFile("/texture.vert", "main");
        ShaderConfig fragment = ShaderConfig.fromRawClasspathFile("/texture.frag", "main");

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
                        FLOATS_PER_VERTEX * Float.BYTES,
                        WgpuInputStepMode.VERTEX,
                        Buffer.vertexAttribute(0, WgpuVertexFormat.FLOAT3, 0),
                        Buffer.vertexAttribute(3 * Float.BYTES, WgpuVertexFormat.FLOAT2, 1)
                ))
                .setSampleCount(1)
                .setSampleMask(0xFFFFFFFF)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts();
    }
}
