package com.noahcharlton.wgpuj.jni;

public enum WgpuTextureFormat {

    R8Unorm(0),
    R8Snorm(1),
    R8Uint(2),
    R8Sint(3),
    R16Uint(4),
    R16Sint(5),
    R16Float(6),
    Rg8Unorm(7),
    Rg8Snorm(8),
    Rg8Uint(9),
    Rg8Sint(10),
    R32Uint(11),
    R32Sint(12),
    R32Float(13),
    Rg16Uint(14),
    Rg16Sint(15),
    Rg16Float(16),
    Rgba8Unorm(17),
    Rgba8UnormSrgb(18),
    Rgba8Snorm(19),
    Rgba8Uint(20),
    Rgba8Sint(21),
    Bgra8Unorm(22),
    Bgra8UnormSrgb(23),
    Rgb10a2Unorm(24),
    Rg11b10Float(25),
    Rg32Uint(26),
    Rg32Sint(27),
    Rg32Float(28),
    Rgba16Uint(29),
    Rgba16Sint(30),
    Rgba16Float(31),
    Rgba32Uint(32),
    Rgba32Sint(33),
    Rgba32Float(34),
    Depth32Float(35),
    Depth24Plus(36),
    Depth24PlusStencil8(37);
    
    private final int value;

    WgpuTextureFormat(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
