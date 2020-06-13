package com.noahcharlton.wgpuj.core.graphics;

import com.noahcharlton.wgpuj.core.input.Key;

public interface WindowEventHandler {

    void onResize();

    void onKeyPressed(Key key);

    void onKeyReleased(Key key);

    class EmptyWindowHandler implements WindowEventHandler{

        @Override
        public void onResize() {
            
        }

        @Override
        public void onKeyPressed(Key key) {

        }

        @Override
        public void onKeyReleased(Key key) {

        }
    }
}
