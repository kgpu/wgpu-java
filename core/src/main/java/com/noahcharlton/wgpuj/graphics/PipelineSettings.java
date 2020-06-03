package com.noahcharlton.wgpuj.graphics;

import com.noahcharlton.wgpuj.jni.WgpuColorStateDescriptor;
import com.noahcharlton.wgpuj.jni.WgpuIndexFormat;
import com.noahcharlton.wgpuj.jni.WgpuPrimitiveTopology;
import com.noahcharlton.wgpuj.jni.WgpuRenderPipelineDescriptor;
import jnr.ffi.Pointer;

public class PipelineSettings {

    private long layout;
    private long vertexModule;
    private String vertexEntryPoint;
    private Pointer fragmentStage;
    private WgpuPrimitiveTopology primitiveTopology;
    private Pointer rasterizationState;
    private WgpuColorStateDescriptor[] colorStates;
    private Pointer depthStencilState;
    private WgpuIndexFormat vertexIndexFormat;
    private int sampleCount;
    private int sampleMask;
    private boolean alphaToCoverage;

    public PipelineSettings() {

    }

    public WgpuRenderPipelineDescriptor build(){
        return new WgpuRenderPipelineDescriptor(this);
    }

    public long getLayout() {
        return layout;
    }

    public long getVertexModule() {
        return vertexModule;
    }

    public PipelineSettings setVertexModule(long vertexModule) {
        this.vertexModule = vertexModule;

        return this;
    }

    public String getVertexEntryPoint() {
        return vertexEntryPoint;
    }

    public PipelineSettings setVertexEntryPoint(String vertexEntryPoint) {
        this.vertexEntryPoint = vertexEntryPoint;

        return this;
    }

    public PipelineSettings setLayout(long layout) {
        this.layout = layout;

        return this;
    }

    public Pointer getFragmentStage() {
        return fragmentStage;
    }

    public PipelineSettings setFragmentStage(Pointer fragmentStage) {
        this.fragmentStage = fragmentStage;

        return this;
    }

    public WgpuPrimitiveTopology getPrimitiveTopology() {
        return primitiveTopology;
    }

    public PipelineSettings setPrimitiveTopology(WgpuPrimitiveTopology primitiveTopology) {
        this.primitiveTopology = primitiveTopology;

        return this;
    }

    public Pointer getRasterizationState() {
        return rasterizationState;
    }

    public PipelineSettings setRasterizationState(Pointer rasterizationState) {
        this.rasterizationState = rasterizationState;

        return this;
    }

    public WgpuColorStateDescriptor[] getColorStates() {
        return colorStates;
    }

    public PipelineSettings setColorStates(WgpuColorStateDescriptor... colorStates) {
        this.colorStates = colorStates;

        return this;
    }

    public Pointer getDepthStencilState() {
        return depthStencilState;
    }

    public PipelineSettings setDepthStencilState(Pointer depthStencilState) {
        this.depthStencilState = depthStencilState;

        return this;
    }

    public WgpuIndexFormat getVertexIndexFormat() {
        return vertexIndexFormat;
    }

    public PipelineSettings setVertexIndexFormat(WgpuIndexFormat vertexIndexFormat) {
        this.vertexIndexFormat = vertexIndexFormat;

        return this;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public PipelineSettings setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;

        return this;
    }

    public int getSampleMask() {
        return sampleMask;
    }

    public PipelineSettings setSampleMask(int sampleMask) {
        this.sampleMask = sampleMask;

        return this;
    }

    public boolean isAlphaToCoverage() {
        return alphaToCoverage;
    }

    public PipelineSettings setAlphaToCoverage(boolean alphaToCoverage) {
        this.alphaToCoverage = alphaToCoverage;

        return this;
    }
}
