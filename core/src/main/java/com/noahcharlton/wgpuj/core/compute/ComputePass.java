package com.noahcharlton.wgpuj.core.compute;

import com.noahcharlton.wgpuj.WgpuJava;
import com.noahcharlton.wgpuj.core.RawPass;
import com.noahcharlton.wgpuj.jni.WgpuRawPass;
import jnr.ffi.Pointer;

public class ComputePass extends RawPass {

    public ComputePass(WgpuRawPass pass) {
        super(pass);
    }

    public void setPipeline(long pipelineId){
        natives.wgpu_compute_pass_set_pipeline(passPointer, pipelineId);
    }

    public void setBindGroup(int index, long bindGroup, Pointer offsets, int offsetLength){
        natives.wgpu_compute_pass_set_bind_group(passPointer, index, bindGroup, offsets, offsetLength);
    }

    public void dispatch(int groupsX, int groupsY, int groupsZ){
        natives.wgpu_compute_pass_dispatch(passPointer, groupsX, groupsY, groupsZ);
    }

    @Override
    public void endPass() {
        natives.wgpu_compute_pass_end_pass(passPointer);

        pass = null;
        passPointer = null;
    }
}
