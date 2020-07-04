package com.noahcharlton.wgpuj.core.math;

import org.joml.Matrix4f;

public class MatrixUtils {

    private static final Matrix4f OPEN_GL_TO_WGPU = new Matrix4f(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f, 1.0f);

    public static Matrix4f generateTransMatrix(Matrix4f projection, Matrix4f view){
        return new Matrix4f(OPEN_GL_TO_WGPU).mul(projection).mul(view);
    }

    public static float[] toFloats(Matrix4f matrix){
        return matrix.get(new float[16]);
    }

}
