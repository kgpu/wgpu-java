package com.noahcharlton.wgpuj.core.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents an image in rgba8
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
            for(int y = 0; y < image.getWidth(); y++){
                int argb = image.getRGB(x, y);

                pixels[x + y * image.getWidth()] = argbToRgba(argb);
            }
        }

        return new ImageData(pixels, image.getWidth(), image.getHeight());
    }

    static int argbToRgba(int argb) {
        int a = (argb & 0xff000000) >>> 24;
        int r = (argb & 0x00ff0000) >>> 16;
        int g = (argb & 0x0000ff00) >>> 8;
        int b = (argb & 0x000000ff);

        return (r << 24) | (g << 16) | (b << 8) | a;
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
