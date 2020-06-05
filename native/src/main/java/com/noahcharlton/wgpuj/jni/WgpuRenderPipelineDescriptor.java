package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuRenderPipelineDescriptor extends WgpuJavaStruct {

    private final Struct.Unsigned64 layout = new Struct.Unsigned64();
    private final WgpuProgrammableStageDescriptor vertexStage = inner(new WgpuProgrammableStageDescriptor());
    private final Struct.Pointer fragmentStage = new Struct.Pointer();
    private final Struct.Enum<WgpuPrimitiveTopology> primitiveTopology = new Struct.Enum<>(WgpuPrimitiveTopology.class);
    private final Struct.Pointer rasterizationState = new Struct.Pointer();
    private final Struct.StructRef colorStates = new Struct.StructRef<>(WgpuColorStateDescriptor.class);
    private final Struct.UnsignedLong colorStatesLength = new Struct.UnsignedLong();
    private final Struct.Pointer depthStencilState = new Struct.Pointer();
    private final WgpuVertexStateDescriptor vertexState = inner(new WgpuVertexStateDescriptor());
    private final Struct.Unsigned32 sampleCount = new Struct.Unsigned32();
    private final Struct.Unsigned32 sampleMask = new Struct.Unsigned32();
    private final Struct.Boolean alphaToCoverage = new Struct.Boolean();

    public WgpuRenderPipelineDescriptor() {
        useDirectMemory();
    }

    public Unsigned64 getLayout() {
        return layout;
    }

    public WgpuProgrammableStageDescriptor getVertexStage() {
        return vertexStage;
    }

    public Pointer getFragmentStage() {
        return fragmentStage;
    }

    public Enum<WgpuPrimitiveTopology> getPrimitiveTopology() {
        return primitiveTopology;
    }

    public Pointer getRasterizationState() {
        return rasterizationState;
    }

    public StructRef<WgpuColorStateDescriptor> getColorStates() {
        return colorStates;
    }

    public UnsignedLong getColorStatesLength() {
        return colorStatesLength;
    }

    public Pointer getDepthStencilState() {
        return depthStencilState;
    }

    public WgpuVertexStateDescriptor getVertexState() {
        return vertexState;
    }

    public Unsigned32 getSampleCount() {
        return sampleCount;
    }

    public Unsigned32 getSampleMask() {
        return sampleMask;
    }

    public Boolean getAlphaToCoverage() {
        return alphaToCoverage;
    }
}
