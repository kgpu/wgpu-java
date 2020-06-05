package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuColor extends WgpuJavaStruct {

    private final Struct.Double r = new Struct.Double();
    private final Struct.Double g = new Struct.Double();
    private final Struct.Double b = new Struct.Double();
    private final Struct.Double a = new Struct.Double();

    public void set(double r, double g, double b, double a){
        this.r.set(r);
        this.g.set(g);
        this.b.set(b);
        this.a.set(a);
    }
}
