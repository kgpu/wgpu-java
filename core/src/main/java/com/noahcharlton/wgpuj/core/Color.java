package com.noahcharlton.wgpuj.core;

import java.util.Objects;

public class Color {

    public static final Color RED = new Color(1.0, 0.0, 0.0, 1.0);
    public static final Color GREEN = new Color(0.0, 1.0, 0.0, 1.0);
    public static final Color BLUE = new Color(0.0, 0.0, 1.0, 1.0);
    public static final Color WHITE = new Color(1.0, 1.0, 1.0, 1.0);
    public static final Color CLEAR = new Color(1.0, 1.0, 1.0, 0.0);

    public final double r;
    public final double g;
    public final double b;
    public final double a;

    public Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Color)) return false;
        Color color = (Color) o;
        return Double.compare(color.r, r) == 0 &&
                Double.compare(color.g, g) == 0 &&
                Double.compare(color.b, b) == 0 &&
                Double.compare(color.a, a) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b, a);
    }
}
