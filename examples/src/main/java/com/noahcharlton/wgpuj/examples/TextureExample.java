package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.BlendDescriptor;
import com.noahcharlton.wgpuj.core.graphics.ColorState;
import com.noahcharlton.wgpuj.core.graphics.GraphicApplicationSettings;
import com.noahcharlton.wgpuj.core.graphics.RenderPipelineSettings;
import com.noahcharlton.wgpuj.core.util.BufferUsage;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.core.util.ImageData;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindGroupLayoutEntry;
import com.noahcharlton.wgpuj.jni.WgpuBindingType;
import com.noahcharlton.wgpuj.jni.WgpuBlendFactor;
import com.noahcharlton.wgpuj.jni.WgpuBlendOperation;
import com.noahcharlton.wgpuj.jni.WgpuBufferCopyView;
import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuCullMode;
import com.noahcharlton.wgpuj.jni.WgpuExtent3d;
import com.noahcharlton.wgpuj.jni.WgpuFrontFace;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuInputStepMode;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuRasterizationStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuTextureComponentType;
import com.noahcharlton.wgpuj.jni.WgpuTextureCopyView;
import com.noahcharlton.wgpuj.jni.WgpuTextureDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuTextureDimension;
import com.noahcharlton.wgpuj.jni.WgpuTextureFormat;
import com.noahcharlton.wgpuj.jni.WgpuTextureViewDimension;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferAttributeDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferLayoutDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexFormat;

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
            -.5f, -.5f, 0f, 0, 0
            - .5f, .5f, 0f, 0, 1,
            .5f, .5f, 0f, 1, 1,
            .5f, -.5f, 0f, 1, 0
    };

    private static final int FLOATS_PER_VERTEX = 5;

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();

        if(true)
            throw new UnsupportedOperationException("This example is currently disabled due to an issue in wgpu-native");

        ImageData texture = loadTexture();
        RenderPipelineSettings renderPipelineSettings = createPipelineSettings();
        GraphicApplicationSettings appSettings = new GraphicApplicationSettings("Wgpu-Java Texture Example", 640, 400);

        try(var application = WgpuGraphicApplication.create(appSettings)) {
            Device device = application.getDevice();

            var vertexBuffer = device.createVertexBuffer("Vertex Buffer", VERTICES);
            var textureDesc = new WgpuTextureDescriptor();
            textureDesc.set("texture", texture.getWidth(), texture.getHeight(), 1, 1, 1, WgpuTextureDimension.D2,
                    WgpuTextureFormat.RGBA8_UINT, WgpuTextureDescriptor.COPY_DST | WgpuTextureDescriptor.SAMPLED);
            long textureId = device.createTexture(textureDesc);

            var textureBuffer = device.createIntBuffer("texture buffer", texture.getPixels(), BufferUsage.COPY_SRC);
            textureBuffer.getMappedData().put(0, texture.getPixels(), 0, texture.getPixels().length);
            textureBuffer.unmap();

            long encoder = device.createCommandEncoder("Command Encoder");
            WgpuJava.wgpuNative.wgpu_command_encoder_copy_buffer_to_texture(encoder,
                    new WgpuBufferCopyView(textureBuffer.getId(), 0, Integer.BYTES * texture.getWidth(),
                            texture.getHeight()).getPointerTo(),
                    new WgpuTextureCopyView(textureId, 0, 0, 0, 0).getPointerTo(),
                    new WgpuExtent3d(texture.getWidth(), texture.getHeight(), 1).getPointerTo());
            WgpuJava.wgpuNative.wgpu_command_encoder_finish(encoder, WgpuJava.createNullPointer());

            long textureView = WgpuJava.wgpuNative.wgpu_texture_create_view(textureId, WgpuJava.createNullPointer());
            //See WgpuSamplerDescriptor, blocked by https://github.com/gfx-rs/wgpu-native/pull/34
            long sampler = WgpuJava.wgpuNative.wgpu_device_create_sampler(application.getDevice().getId(),
                    WgpuJava.createNullPointer());

            var textureBindGroupLayout = device.createBindGroupLayout("texture bind group layout",
                    new WgpuBindGroupLayoutEntry()
                            .setPartial(0, WgpuBindGroupLayoutEntry.SHADER_STAGE_FRAGMENT,
                                    WgpuBindingType.SAMPLED_TEXTURE)
                            .setSampledTextureData(false, WgpuTextureViewDimension.D2,
                                    WgpuTextureComponentType.UINT),
                    new WgpuBindGroupLayoutEntry()
                            .setPartial(1, WgpuBindGroupLayoutEntry.SHADER_STAGE_FRAGMENT,
                                    WgpuBindingType.SAMPLER));

            var textureBindGroup = device.createBindGroup("texture bind group", textureBindGroupLayout,
                    new WgpuBindGroupEntry().setTextureView(0, textureView),
                    new WgpuBindGroupEntry().setSampler(1, sampler));

            renderPipelineSettings.setBindGroupLayouts(textureBindGroup);
            application.init(renderPipelineSettings);

            while(!application.getWindow().isCloseRequested()) {
                var renderPass = application.renderStart();
                renderPass.setBindGroup(0, textureBindGroup);
                renderPass.setVertexBuffer(vertexBuffer, 0);
                renderPass.draw(VERTICES.length, 1);

                application.renderEnd();
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

    private static RenderPipelineSettings createPipelineSettings() {
        ShaderData vertex = ShaderData.fromRawClasspathFile("/texture.vert", "main");
        ShaderData fragment = ShaderData.fromCompiledClasspathFile("/texture.frag", "main");

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
                .setBufferLayouts(new WgpuVertexBufferLayoutDescriptor(
                        FLOATS_PER_VERTEX * Float.BYTES,
                        WgpuInputStepMode.VERTEX,
                        new WgpuVertexBufferAttributeDescriptor(
                                0, WgpuVertexFormat.FLOAT3, 0),
                        new WgpuVertexBufferAttributeDescriptor(
                                3 * Float.BYTES, WgpuVertexFormat.FLOAT2, 1)
                ))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts()
                .setClearColor(Color.BLACK);
    }
}
