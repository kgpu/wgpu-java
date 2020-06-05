package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.Color;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuColor extends WgpuJavaStruct {

    private final Struct.Double r = new Struct.Double();
    private final Struct.Double g = new Struct.Double();
    private final Struct.Double b = new Struct.Double();
    private final Struct.Double a = new Struct.Double();

    public void set(Color color){
        r.set(color.r);
        g.set(color.g);
        b.set(color.b);
        a.set(color.a);
    }
}
