package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.core.Device;
import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.core.util.Color;
import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuDepthStencilStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuRasterizationStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRenderPipelineDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferLayoutDescriptor;

public class RenderPipelineSettings {

    private ShaderData vertexStage;
    private ShaderData fragmentStage;
    private WgpuPrimitiveTopology primitiveTopology;
    private WgpuRasterizationStateDescriptor rasterizationState;
    private WgpuColorStateDescriptor[] colorStates;
    private WgpuDepthStencilStateDescriptor depthStencilState;
    private WgpuIndexFormat vertexIndexFormat;
    private WgpuVertexBufferLayoutDescriptor[] bufferLayouts;
    private Color clearColor;
    private int sampleCount;
    private int sampleMask;
    private boolean alphaToCoverage;
    private long[] bindGroupLayouts;

    public RenderPipelineSettings() {

    }

    public WgpuRenderPipelineDescriptor build(Device device, long layout){
        var descriptor = WgpuRenderPipelineDescriptor.createDirect();

        var fragment = fragmentStage.build(device);
        long vertexModule = vertexStage.createModule(device);

        descriptor.setLayout(layout);
        descriptor.getVertexStage().setModule(vertexModule);
        descriptor.getVertexStage().setEntryPoint(vertexStage.getEntryPoint());
        descriptor.setFragmentStage(fragment);
        descriptor.setPrimitiveTopology(primitiveTopology);
        descriptor.setRasterizationState(rasterizationState);
        descriptor.setColorStates(colorStates);
        descriptor.setColorStatesLength(colorStates.length);
        descriptor.setDepthStencilState(depthStencilState);
        descriptor.getVertexState().setIndexFormat(vertexIndexFormat);
        descriptor.getVertexState().setVertexBuffers(bufferLayouts);
        descriptor.getVertexState().setVertexBuffersLength(bufferLayouts.length);
        descriptor.setSampleCount(sampleCount);
        descriptor.setSampleMask(sampleMask);
        descriptor.setAlphaToCoverageEnabled(alphaToCoverage);

        return descriptor;
    }

    public ShaderData getVertexStage() {
        return vertexStage;
    }

    public RenderPipelineSettings setVertexStage(ShaderData vertexStage) {
        this.vertexStage = vertexStage;
        return this;
    }

    public ShaderData getFragmentStage() {
        return fragmentStage;
    }

    public RenderPipelineSettings setFragmentStage(ShaderData fragmentStage) {
        this.fragmentStage = fragmentStage;
        return this;
    }

    public WgpuPrimitiveTopology getPrimitiveTopology() {
        return primitiveTopology;
    }

    public RenderPipelineSettings setPrimitiveTopology(WgpuPrimitiveTopology primitiveTopology) {
        this.primitiveTopology = primitiveTopology;

        return this;
    }

    public RenderPipelineSettings setBufferLayouts(WgpuVertexBufferLayoutDescriptor... bufferLayouts) {
        this.bufferLayouts = bufferLayouts;

        return this;
    }

    public WgpuVertexBufferLayoutDescriptor[] getBufferLayouts() {
        return bufferLayouts;
    }

    public WgpuRasterizationStateDescriptor getRasterizationState() {
        return rasterizationState;
    }

    public RenderPipelineSettings setRasterizationState(WgpuRasterizationStateDescriptor rasterizationState) {
        this.rasterizationState = rasterizationState;

        return this;
    }

    public WgpuColorStateDescriptor[] getColorStates() {
        return colorStates;
    }

    public RenderPipelineSettings setColorStates(WgpuColorStateDescriptor... colorStates) {
        this.colorStates = colorStates;

        return this;
    }

    public WgpuDepthStencilStateDescriptor getDepthStencilState() {
        return depthStencilState;
    }

    public RenderPipelineSettings setDepthStencilState(WgpuDepthStencilStateDescriptor depthStencilState) {
        this.depthStencilState = depthStencilState;

        return this;
    }

    public WgpuIndexFormat getVertexIndexFormat() {
        return vertexIndexFormat;
    }

    public RenderPipelineSettings setVertexIndexFormat(WgpuIndexFormat vertexIndexFormat) {
        this.vertexIndexFormat = vertexIndexFormat;

        return this;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public RenderPipelineSettings setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;

        return this;
    }

    public int getSampleMask() {
        return sampleMask;
    }

    public RenderPipelineSettings setSampleMask(int sampleMask) {
        this.sampleMask = sampleMask;

        return this;
    }

    public boolean isAlphaToCoverage() {
        return alphaToCoverage;
    }

    public RenderPipelineSettings setAlphaToCoverage(boolean alphaToCoverage) {
        this.alphaToCoverage = alphaToCoverage;

        return this;
    }

    public RenderPipelineSettings setBindGroupLayouts(long... bindGroupLayouts) {
        this.bindGroupLayouts = bindGroupLayouts;

        return this;
    }

    public RenderPipelineSettings setClearColor(Color clearColor) {
        this.clearColor = clearColor;

        return this;
    }

    public Color getClearColor() {
        return clearColor;
    }

    public long[] getBindGroupLayouts() {
        return bindGroupLayouts;
    }
}
