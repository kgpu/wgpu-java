package com.noahcharlton.wgpuj.core.graphics;


public class RenderPipeline {

    private final long pipelineID;

    public RenderPipeline(long pipelineID) {
        this.pipelineID = pipelineID;
    }

    public long getPipelineID() {
        return pipelineID;
    }
}
