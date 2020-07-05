package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.core.util.ShaderModule;
import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuDepthStencilStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuRasterizationStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuRenderPipelineDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuVertexBufferLayoutDescriptor;

public class RenderPipelineConfig {

    private ShaderModule vertexStage;
    private ShaderModule fragmentStage;
    private WgpuPrimitiveTopology primitiveTopology;
    private WgpuRasterizationStateDescriptor rasterizationState;
    private WgpuColorStateDescriptor[] colorStates;
    private WgpuDepthStencilStateDescriptor depthStencilState;
    private WgpuIndexFormat vertexIndexFormat;
    private WgpuVertexBufferLayoutDescriptor[] bufferLayouts;
    private int sampleCount;
    private int sampleMask;
    private boolean alphaToCoverage;
    private long[] bindGroupLayouts;

    public RenderPipelineConfig() {

    }

    public WgpuRenderPipelineDescriptor build(long layout){
        var descriptor = WgpuRenderPipelineDescriptor.createDirect();

        var fragment = fragmentStage.toDescriptor();
        long vertexModule = vertexStage.getModule();

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

    public ShaderModule getVertexStage() {
        return vertexStage;
    }

    public RenderPipelineConfig setVertexStage(ShaderModule vertexStage) {
        this.vertexStage = vertexStage;
        return this;
    }

    public ShaderModule getFragmentStage() {
        return fragmentStage;
    }

    public RenderPipelineConfig setFragmentStage(ShaderModule fragmentStage) {
        this.fragmentStage = fragmentStage;
        return this;
    }

    public WgpuPrimitiveTopology getPrimitiveTopology() {
        return primitiveTopology;
    }

    public RenderPipelineConfig setPrimitiveTopology(WgpuPrimitiveTopology primitiveTopology) {
        this.primitiveTopology = primitiveTopology;

        return this;
    }

    public RenderPipelineConfig setBufferLayouts(WgpuVertexBufferLayoutDescriptor... bufferLayouts) {
        this.bufferLayouts = bufferLayouts;

        return this;
    }

    public WgpuVertexBufferLayoutDescriptor[] getBufferLayouts() {
        return bufferLayouts;
    }

    public WgpuRasterizationStateDescriptor getRasterizationState() {
        return rasterizationState;
    }

    public RenderPipelineConfig setRasterizationState(WgpuRasterizationStateDescriptor rasterizationState) {
        this.rasterizationState = rasterizationState;

        return this;
    }

    public WgpuColorStateDescriptor[] getColorStates() {
        return colorStates;
    }

    public RenderPipelineConfig setColorStates(WgpuColorStateDescriptor... colorStates) {
        this.colorStates = colorStates;

        return this;
    }

    public WgpuDepthStencilStateDescriptor getDepthStencilState() {
        return depthStencilState;
    }

    public RenderPipelineConfig setDepthStencilState(WgpuDepthStencilStateDescriptor depthStencilState) {
        this.depthStencilState = depthStencilState;

        return this;
    }

    public WgpuIndexFormat getVertexIndexFormat() {
        return vertexIndexFormat;
    }

    public RenderPipelineConfig setVertexIndexFormat(WgpuIndexFormat vertexIndexFormat) {
        this.vertexIndexFormat = vertexIndexFormat;

        return this;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public RenderPipelineConfig setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;

        return this;
    }

    public int getSampleMask() {
        return sampleMask;
    }

    public RenderPipelineConfig setSampleMask(int sampleMask) {
        this.sampleMask = sampleMask;

        return this;
    }

    public boolean isAlphaToCoverage() {
        return alphaToCoverage;
    }

    public RenderPipelineConfig setAlphaToCoverage(boolean alphaToCoverage) {
        this.alphaToCoverage = alphaToCoverage;

        return this;
    }

    public RenderPipelineConfig setBindGroupLayouts(long... bindGroupLayouts) {
        this.bindGroupLayouts = bindGroupLayouts;

        return this;
    }

    public long[] getBindGroupLayouts() {
        return bindGroupLayouts;
    }
}
