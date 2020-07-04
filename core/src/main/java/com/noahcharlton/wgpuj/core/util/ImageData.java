package com.noahcharlton.wgpuj.core.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents an image in rgba where
 * each pixel is an int with 1 byte for each channel:
 * <ul>
 *     <b>Least Significant Bits</b>
 *     <p>Red</p>
 *     <p>Green</p>
 *     <p>Blue</p>
 *     <p>Alpha</p>
 *     <b>Most Significant Bits</b>
 * </ul>
 *
 * @see com.noahcharlton.wgpuj.jni.WgpuTextureFormat#RGBA8_UNORM_SRGB
 */
public class ImageData {

    /**
     * starts from the top left corner
     */
    private final int[] pixels;
    private final int width;
    private final int height;

    public ImageData(int[] pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
    }

    public static ImageData fromFile(String path) throws IOException {
        var inputStream = ImageData.class.getResourceAsStream(path);

        if(inputStream == null)
            throw new FileNotFoundException("Failed to find image: " + path);

        BufferedImage image = ImageIO.read(inputStream);

        int[] pixels = new int[image.getWidth() * image.getHeight()];

        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int argb = image.getRGB(x, y);

                pixels[x + y * image.getWidth()] = argbToRgba(argb);
            }
        }

        return new ImageData(pixels, image.getWidth(), image.getHeight());
    }

    public int getPixel(int x, int y){
       return pixels[x + y * width];
    }

    static int argbToRgba(int argb) {
        int a = (argb & 0xff000000) >>> 24;
        int r = (argb & 0x00ff0000) >>> 16;
        int g = (argb & 0x0000ff00) >>> 8;
        int b = (argb & 0x000000ff);

        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
