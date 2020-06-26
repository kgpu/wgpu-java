package com.noahcharlton.wgpuj.core.math;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class MathUtils {

    public static final float PIf = (float) Math.PI;

    public static final Vector3f UNIT_X = new Vector3f(1f, 0f, 0f);
    public static final Vector3f UNIT_Y = new Vector3f(0f, 1f, 0f);
    public static final Vector3f UNIT_Z = new Vector3f(0f, 0f, 1f);

    public static float toRadians(float degrees){
        return degrees * PIf / 180f;
    }

    public static float clamp(float val, float min, float max){
        return Math.min(max, Math.max(min, val));
    }

}
