package com.noahcharlton.wgpuj.jni;

public enum WgpuTextureFormat {

    R8_UNORM(0),
    R8_SNORM(1),
    R8_UINT(2),
    R8_SINT(3),
    R16_UINT(4),
    R16_SINT(5),
    R16_FLOAT(6),
    RG8_UNORM(7),
    RG8_SNORM(8),
    RG8_UINT(9),
    RG8_SINT(10),
    R32_UINT(11),
    R32_SINT(12),
    R32_FLOAT(13),
    RG16_UINT(14),
    RG16_SINT(15),
    RG16_FLOAT(16),
    RGBA8_UNORM(17),
    RGBA8_UNORM_SRGB(18),
    RGBA8_SNORM(19),
    RGBA8_UINT(20),
    RGBA8_SINT(21),
    BGRA8_UNORM(22),
    BGRA8_UNORM_SRGB(23),
    RGB10A2_UNORM(24),
    RG11B10_FLOAT(25),
    RG32_UINT(26),
    RG32_SINT(27),
    RG32_FLOAT(28),
    RGBA16_UINT(29),
    RGBA16_SINT(30),
    RGBA16_FLOAT(31),
    RGBA32_UINT(32),
    RGBA32_SINT(33),
    RGBA32_FLOAT(34),
    DEPTH32_FLOAT(35),
    DEPTH24_PLUS(36),
    DEPTH24_PLUS_STENCIL_8(37);
    
    private final int value;

    WgpuTextureFormat(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
