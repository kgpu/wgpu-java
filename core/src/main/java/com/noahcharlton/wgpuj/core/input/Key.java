package com.noahcharlton.wgpuj.core.input;

import org.lwjgl.glfw.GLFW;

public enum Key {

    Q(GLFW.GLFW_KEY_Q),
    W(GLFW.GLFW_KEY_W),
    E(GLFW.GLFW_KEY_E),
    R(GLFW.GLFW_KEY_R),
    T(GLFW.GLFW_KEY_T),
    Y(GLFW.GLFW_KEY_Y),
    U(GLFW.GLFW_KEY_U),
    I(GLFW.GLFW_KEY_I),
    O(GLFW.GLFW_KEY_O),
    P(GLFW.GLFW_KEY_P),
    A(GLFW.GLFW_KEY_A),
    S(GLFW.GLFW_KEY_S),
    D(GLFW.GLFW_KEY_D),
    F(GLFW.GLFW_KEY_F),
    G(GLFW.GLFW_KEY_G),
    H(GLFW.GLFW_KEY_H),
    J(GLFW.GLFW_KEY_J),
    K(GLFW.GLFW_KEY_K),
    L(GLFW.GLFW_KEY_L),
    Z(GLFW.GLFW_KEY_Z),
    X(GLFW.GLFW_KEY_X),
    C(GLFW.GLFW_KEY_C),
    V(GLFW.GLFW_KEY_V),
    B(GLFW.GLFW_KEY_B),
    N(GLFW.GLFW_KEY_N),
    M(GLFW.GLFW_KEY_M),
    NUM1(GLFW.GLFW_KEY_1),
    NUM2(GLFW.GLFW_KEY_2),
    NUM3(GLFW.GLFW_KEY_3),
    NUM4(GLFW.GLFW_KEY_4),
    NUM5(GLFW.GLFW_KEY_5),
    NUM6(GLFW.GLFW_KEY_6),
    NUM7(GLFW.GLFW_KEY_7),
    NUM8(GLFW.GLFW_KEY_8),
    NUM9(GLFW.GLFW_KEY_9),
    NUM0(GLFW.GLFW_KEY_0),
    UNKNOWN(Integer.MAX_VALUE);

    private final int keyCode;

    Key(int code) {
        this.keyCode = code;
    }

    public static Key toKey(int code){
        for(Key key : values()){
            if(key.keyCode == code){
                return key;
            }
        }

        return UNKNOWN;
    }

}
