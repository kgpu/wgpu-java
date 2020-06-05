package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.graphics.PipelineSettings;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuRenderPipelineDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 layout;
    private final WgpuProgrammableStageDescriptor vertexStage;
    private final Struct.Pointer fragmentStage;
    private final Struct.Enum<WgpuPrimitiveTopology> primitiveTopology;
    private final Struct.Pointer rasterizationState;
    private final Struct.StructRef colorStates;
    private final Struct.UnsignedLong colorStatesLength;
    private final Struct.Pointer depthStencilState;
    private final WgpuVertexStateDescriptor vertexState;
    private final Struct.Unsigned32 sampleCount;
    private final Struct.Unsigned32 sampleMask;
    private final Struct.Boolean alphaToCoverage;

    public WgpuRenderPipelineDescriptor(PipelineSettings pipelineSettings) {
        var vertexStageLocal = new WgpuProgrammableStageDescriptor();
        var vertexBufferLocal = new WgpuVertexStateDescriptor();

        layout = new Struct.Unsigned64();
        vertexStage = inner(vertexStageLocal);
        fragmentStage = new Struct.Pointer();
        primitiveTopology = new Struct.Enum<>(WgpuPrimitiveTopology.class);
        rasterizationState = new Struct.Pointer();
        colorStates = new Struct.StructRef<>(WgpuColorStateDescriptor.class);
        colorStatesLength = new Struct.UnsignedLong();
        depthStencilState = new Struct.Pointer();
        vertexState = inner(vertexBufferLocal);
        sampleCount = new Struct.Unsigned32();
        sampleMask = new Struct.Unsigned32();
        alphaToCoverage = new Struct.Boolean();
        useDirectMemory();

        layout.set(pipelineSettings.getLayout());
        vertexStageLocal.set(pipelineSettings.getVertexModule(), pipelineSettings.getVertexEntryPoint());
        fragmentStage.set(pipelineSettings.getFragmentStage());
        primitiveTopology.set(pipelineSettings.getPrimitiveTopology().getIntValue());
        rasterizationState.set(pipelineSettings.getRasterizationState());
        colorStates.set(pipelineSettings.getColorStates());
        colorStatesLength.set(pipelineSettings.getColorStates().length);
        depthStencilState.set(pipelineSettings.getDepthStencilState());
        vertexBufferLocal.set(pipelineSettings.getVertexIndexFormat());
        sampleCount.set(pipelineSettings.getSampleCount());
        sampleMask.set(pipelineSettings.getSampleMask());
        alphaToCoverage.set(pipelineSettings.isAlphaToCoverage());
    }
}
