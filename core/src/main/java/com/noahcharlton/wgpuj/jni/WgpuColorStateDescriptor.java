package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.graphics.BlendDescriptor;
import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class WgpuColorStateDescriptor extends WgpuJavaStruct {

    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 4;
    public static final int ALPHA = 8;
    public static final int COLOR = 7;
    public static final int ALL = 15;

    private Struct.Enum<WgpuTextureFormat> textureFormat;
    private WgpuBlendDescriptor alphaBlend;
    private WgpuBlendDescriptor colorBlend;
    private Struct.Unsigned32 writeMask;

    public WgpuColorStateDescriptor(Runtime runtime) {
        super(runtime);
    }

    public WgpuColorStateDescriptor(WgpuTextureFormat textureFormat, BlendDescriptor alpha, BlendDescriptor color,
                                    int writeMask) {
        var alphaBlend = new WgpuBlendDescriptor();
        var colorBlend = new WgpuBlendDescriptor();

        this.textureFormat = new Struct.Enum<>(WgpuTextureFormat.class);
        this.alphaBlend = inner(alphaBlend);
        this.colorBlend = inner(colorBlend);
        this.writeMask = new Struct.Unsigned32();

        useDirectMemory();
        this.textureFormat.set(textureFormat);
        this.writeMask.set(writeMask);
        alphaBlend.set(alpha);
        colorBlend.set(color);
    }
}
