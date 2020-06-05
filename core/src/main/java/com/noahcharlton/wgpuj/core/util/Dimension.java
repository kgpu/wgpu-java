package com.noahcharlton.wgpuj.core.util;

import java.util.Objects;

public final class Dimension {

    private final int width;
    private final int height;

    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Dimension)) return false;
        Dimension dimension = (Dimension) o;
        return getWidth() == dimension.getWidth() &&
                getHeight() == dimension.getHeight();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWidth(), getHeight());
    }

    @Override
    public String toString() {
        return "Dimension{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
