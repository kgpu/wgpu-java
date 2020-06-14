package com.noahcharlton.wgpuj.jni;

import com.noahcharlton.wgpuj.util.WgpuJavaStruct;
import jnr.ffi.Struct;

public class WgpuSwapChainDescriptor extends WgpuJavaStruct {

    public static final int TEXTURE_COPY_SOURCE = 1;
    public static final int TEXTURE_COPY_DST = 2;
    public static final int TEXTURE_SAMPLED = 4;
    public static final int TEXTURE_STORAGE = 8;
    public static final int TEXTURE_OUTPUT_ATTACHMENT = 16;

    private final Struct.Unsigned32 textureUsage = new Struct.Unsigned32();
    private final Struct.Enum<WgpuTextureFormat> textureFormat = new Struct.Enum<>(WgpuTextureFormat.class);
    private final Struct.Unsigned32 width = new Struct.Unsigned32();
    private final Struct.Unsigned32 height = new Struct.Unsigned32();
    private final Struct.Enum<WgpuPresentMode> presentMode = new Struct.Enum<>(WgpuPresentMode.class);

    public WgpuSwapChainDescriptor(int textureUsage, WgpuTextureFormat format, int windowWidth, int windowHeight,
                                   WgpuPresentMode mode) {
        useDirectMemory();

        this.textureUsage.set(textureUsage);
        this.textureFormat.set(format);
        this.width.set(windowWidth);
        this.height.set(windowHeight);
        this.presentMode.set(mode);
    }
}
