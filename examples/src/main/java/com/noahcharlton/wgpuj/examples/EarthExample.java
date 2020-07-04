package com.noahcharlton.wgpuj.examples;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.WgpuCore;
import com.noahcharlton.wgpuj.core.WgpuGraphicApplication;
import com.noahcharlton.wgpuj.core.graphics.*;
import com.noahcharlton.wgpuj.core.math.MathUtils;
import com.noahcharlton.wgpuj.core.math.MatrixUtils;
import com.noahcharlton.wgpuj.core.util.*;
import com.noahcharlton.wgpuj.jni.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

public class EarthExample {

    static class Sphere {

        private final float radius = 2f;
        private final int chunks;
        private final int slices;

        public Sphere(int chunks, int slices) {
            this.chunks = chunks;
            this.slices = slices;

            if((chunks + 1) * (slices + 1) > Short.MAX_VALUE){
                throw new UnsupportedOperationException("Too many vertices!");
            }
        }

        public float[] generateVertices(){
            float[] vertices = new float[(chunks + 1) * (slices + 1) * FLOATS_PER_VERTEX];
            float angleXY;
            float angleZ;

            for(int slice = 0; slice <= slices; slice++){
                angleZ = (MathUtils.PIf / slices * slice) - (MathUtils.PIf / 2f);

                //last vertex overlaps the first one, but with the other end of the texture
                for(int chunk = 0; chunk <= chunks; chunk++){
                    int index = ((slice * (chunks + 1)) + chunk) * FLOATS_PER_VERTEX;
                    angleXY = (MathUtils.PIf * 2f) / chunks * chunk;
                    Vector3f pos = new Vector3f(
                            MathUtils.cosf(angleZ) * MathUtils.cosf(angleXY),
                            MathUtils.cosf(angleZ) * MathUtils.sinf(angleXY),
                            MathUtils.sinf(angleZ)).mul(radius);

                    vertices[index] = pos.x;
                    vertices[index + 1] = pos.y;
                    vertices[index + 2] = pos.z;
                    vertices[index + 3] = (float) chunk / chunks;
                    vertices[index + 4] = 1f - (float) slice / slices;

                    pos.normalize();
                    vertices[index + 5] = pos.x;
                    vertices[index + 6] = pos.y;
                    vertices[index + 7] = pos.z;
                }
            }

            return vertices;
        }

        public short[] generateIndices(){
            short[] indices = new short[6 * chunks * slices];

            for(int slice = 0; slice < slices; slice++){
                int sliceIndex = (chunks + 1) * slice;
                int nextSliceIndex = (chunks + 1) * (slice + 1);

                for(int chunk = 0; chunk < chunks; chunk++){
                    int index = ((slice * chunks) + chunk) * 6;

                    indices[index] = (short) (sliceIndex + chunk);
                    indices[index + 1] = (short) (sliceIndex + chunk + 1);
                    indices[index + 2] = (short) (nextSliceIndex + chunk);
                    indices[index + 3] = (short) (nextSliceIndex + chunk);
                    indices[index + 4] = (short) (sliceIndex + chunk + 1);
                    indices[index + 5] = (short) (nextSliceIndex + chunk + 1);
                }
            }

            return indices;
        }
    }

    private static final int FLOATS_PER_VERTEX = 8;

    private final WgpuGraphicApplication app;
    private final Window window;
    private final Device device;

    private final Matrix4f modelMatrix = new Matrix4f().rotateY(0.2f);
    private final Matrix4f viewMatrix = new Matrix4f().lookAt(
            new Vector3f(-5f, -5f, 3.5f),
            new Vector3f(),
            MathUtils.UNIT_Z
    );

    private final Buffer transMatrixBuffer;
    private final Buffer indexBuffer;
    private final Buffer vertexBuffer;
    private final Buffer modelMatrixBuffer;
    private final Buffer normalMatrixBuffer;
    private final long bindGroupId;

    public EarthExample(GraphicApplicationSettings settings) {
        app = WgpuGraphicApplication.create(settings);
        device = app.getDevice();
        window = app.getWindow();

        var sphere = new Sphere(100, 100);

        float[] lightSrc = new float[]{-3f, -3f, 2f};
        Buffer lightSrcBuffer = device.createFloatBuffer("Light Src", lightSrc, BufferUsage.UNIFORM);
        vertexBuffer = device.createVertexBuffer("Vertices", sphere.generateVertices());
        indexBuffer = device.createIndexBuffer("Indices", sphere.generateIndices());
        transMatrixBuffer = device.createFloatBuffer("Transformation Matrix", getTransformationMatrixData(),
                BufferUsage.UNIFORM, BufferUsage.COPY_DST);
        modelMatrixBuffer = device.createFloatBuffer("Model Matrix", MatrixUtils.toFloats(modelMatrix),
                BufferUsage.UNIFORM, BufferUsage.COPY_DST);
        normalMatrixBuffer = device.createFloatBuffer("Normal Matrix", getNormalMatrixData(),
                BufferUsage.UNIFORM, BufferUsage.COPY_DST);


        var texture = loadTexture();
        var textureDesc = WgpuTextureDescriptor.createDirect();
        textureDesc.setLabel("Earth Texture");
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

        long encoder = device.createCommandEncoder("Command Encoder");
        textureBuffer.copyToTexture(encoder, textureId, texture);
        long commandBuffer = WgpuJava.wgpuNative.wgpu_command_encoder_finish(encoder, WgpuJava.createNullPointer());
        WgpuJava.wgpuNative.wgpu_queue_submit(device.getDefaultQueue(),
                WgpuJava.createDirectLongPointer(commandBuffer), 1);

        var samplerDesc = WgpuSamplerDescriptor.createDirect();
        samplerDesc.setAddressModeU(WgpuAddressMode.MIRROR_REPEAT);
        samplerDesc.setAddressModeW(WgpuAddressMode.MIRROR_REPEAT);
        samplerDesc.setAddressModeV(WgpuAddressMode.MIRROR_REPEAT);
        samplerDesc.setMagFilter(WgpuFilterMode.NEAREST);
        samplerDesc.setMinFilter(WgpuFilterMode.NEAREST);
        samplerDesc.setMipmapFilter(WgpuFilterMode.NEAREST);
        samplerDesc.setLodMinClamp(-100f);
        samplerDesc.setLodMaxClamp(200f);
        samplerDesc.setCompare(WgpuCompareFunction.GREATER);

        long textureView = WgpuJava.wgpuNative.wgpu_texture_create_view(textureId, WgpuJava.createNullPointer());
        long sampler = WgpuJava.wgpuNative.wgpu_device_create_sampler(app.getDevice().getId(),
                samplerDesc.getPointerTo());

        var bindGroupLayout = device.createBindGroupLayout("Bind group layout",
                BindGroupUtils.partialLayout(0, Wgpu.ShaderStage.VERTEX, WgpuBindingType.UNIFORM_BUFFER),
                BindGroupUtils.textureLayout(1, Wgpu.ShaderStage.FRAGMENT, WgpuBindingType.SAMPLED_TEXTURE,
                        false, WgpuTextureViewDimension.D2, WgpuTextureComponentType.UINT),
                BindGroupUtils.samplerLayout(2, Wgpu.ShaderStage.FRAGMENT, WgpuBindingType.SAMPLER, false),
                BindGroupUtils.partialLayout(3, Wgpu.ShaderStage.FRAGMENT, WgpuBindingType.UNIFORM_BUFFER),
                BindGroupUtils.partialLayout(4, Wgpu.ShaderStage.VERTEX, WgpuBindingType.UNIFORM_BUFFER),
                BindGroupUtils.partialLayout(5, Wgpu.ShaderStage.VERTEX, WgpuBindingType.UNIFORM_BUFFER));

        bindGroupId = device.createBindGroup("Bind group", bindGroupLayout,
                new WgpuBindGroupEntry().setBuffer(0, transMatrixBuffer.getId(), transMatrixBuffer.getSize()),
                new WgpuBindGroupEntry().setTextureView(1, textureView),
                new WgpuBindGroupEntry().setSampler(2, sampler),
                new WgpuBindGroupEntry().setBuffer(3, lightSrcBuffer.getId(), lightSrcBuffer.getSize()),
                new WgpuBindGroupEntry().setBuffer(4, modelMatrixBuffer.getId(), modelMatrixBuffer.getSize()),
                new WgpuBindGroupEntry().setBuffer(5, normalMatrixBuffer.getId(), normalMatrixBuffer.getSize()));

        var pipeline = createPipelineSettings();
        pipeline.setBindGroupLayouts(bindGroupLayout);

        app.init(pipeline);
    }

    private ImageData loadTexture() {
        try{
            return ImageData.fromFile("/earth.png");
        }catch (IOException e){
            throw new RuntimeException("Failed to load planet texture!", e);
        }
    }

    private void run() {
        while (!window.isCloseRequested()) {
            RenderPass pass = app.renderStart();
            pass.setBindGroup(0, bindGroupId);
            pass.setIndexBuffer(indexBuffer);
            pass.setVertexBuffer(vertexBuffer, 0);
            pass.drawIndexed((int) indexBuffer.getSize() / 2, 1, 0);
            app.renderEnd();

            modelMatrix.rotate(.01f, MathUtils.UNIT_Z);
            device.queueWriteFloatBuffer(modelMatrixBuffer, MatrixUtils.toFloats(modelMatrix));
            device.queueWriteFloatBuffer(transMatrixBuffer, getTransformationMatrixData());
            device.queueWriteFloatBuffer(normalMatrixBuffer, getNormalMatrixData());
        }
    }

    private float[] getNormalMatrixData(){
        Matrix4f matrix = new Matrix4f(modelMatrix).invert().transpose().mul(viewMatrix);

        return MatrixUtils.toFloats(matrix);
    }

    private float[] getTransformationMatrixData(){
        Dimension dimension = window.getWindowDimension();
        float aspectRatio = (float) dimension.getWidth() / dimension.getHeight();
        Matrix4f projection = new Matrix4f().perspective(MathUtils.toRadians(45f), aspectRatio, 1, 10);

        return MatrixUtils.toFloats(MatrixUtils.generateTransMatrix(projection, viewMatrix));
    }

    public static void main(String[] args) {
        WgpuCore.loadWgpuNative();
        GraphicApplicationSettings settings = new GraphicApplicationSettings();
        settings.setTitle("Wgpuj: Model Earth");
        settings.setWidth(400);
        settings.setHeight(400);

        new EarthExample(settings).run();
    }

    private static RenderPipelineSettings createPipelineSettings() {
        ShaderData vertex = ShaderData.fromRawClasspathFile("/earth.vert", "main");
        ShaderData fragment = ShaderData.fromRawClasspathFile("/earth.frag", "main");

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
                        Buffer.vertexAttribute(3 * Float.BYTES, WgpuVertexFormat.FLOAT2, 1),
                        Buffer.vertexAttribute(5 * Float.BYTES, WgpuVertexFormat.FLOAT3, 2)
                ))
                .setSampleCount(1)
                .setSampleMask(0)
                .setAlphaToCoverage(false)
                .setBindGroupLayouts()
                .setClearColor(Color.BLACK);
    }
}
