package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.core.ShaderData;
import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuRenderPipelineDescriptor;
import jnr.ffi.Pointer;

public class RenderPipelineSettings {

    private ShaderData vertexStage;
    private ShaderData fragmentStage;
    private WgpuPrimitiveTopology primitiveTopology;
    private Pointer rasterizationState;
    private WgpuColorStateDescriptor[] colorStates;
    private Pointer depthStencilState;
    private WgpuIndexFormat vertexIndexFormat;
    private int sampleCount;
    private int sampleMask;
    private boolean alphaToCoverage;

    public RenderPipelineSettings() {

    }

    public WgpuRenderPipelineDescriptor build(long device, long layout){
        WgpuRenderPipelineDescriptor descriptor = new WgpuRenderPipelineDescriptor();
        Pointer fragment = fragmentStage.build(device).getPointerTo();
        long vertexModule = vertexStage.createModule(device);

        descriptor.getLayout().set(layout);
        descriptor.getVertexStage().set(vertexModule, vertexStage.getEntryPoint());
        descriptor.getFragmentStage().set(fragment);
        descriptor.getPrimitiveTopology().set(primitiveTopology);
        descriptor.getRasterizationState().set(rasterizationState);
        descriptor.getColorStates().set(colorStates);
        descriptor.getColorStatesLength().set(colorStates.length);
        descriptor.getDepthStencilState().set(depthStencilState);
        descriptor.getVertexState().set(vertexIndexFormat);
        descriptor.getSampleCount().set(sampleCount);
        descriptor.getSampleMask().set(sampleMask);
        descriptor.getAlphaToCoverage().set(alphaToCoverage);

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

    public Pointer getRasterizationState() {
        return rasterizationState;
    }

    public RenderPipelineSettings setRasterizationState(Pointer rasterizationState) {
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

    public Pointer getDepthStencilState() {
        return depthStencilState;
    }

    public RenderPipelineSettings setDepthStencilState(Pointer depthStencilState) {
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
}
