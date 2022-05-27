package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.TouchButtonEvent;
import com.ancevt.d2d2.interactive.TouchButton;

public class Tests_Hover {

    public static void main(String[] args) {
        Root root = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));

        root.add(new Button(), 200, 200);

        D2D2.loop();
    }

    private static class Button extends TouchButton {

        private final PlainRect bg = new PlainRect();

        private Button() {
            setEnabled(true);
            bg.setColor(Color.GRAY);
            setSize(100, 100);
            add(bg);

            addEventListener(this, TouchButtonEvent.TOUCH_HOVER, this::this_touchHover);
            addEventListener(this, TouchButtonEvent.TOUCH_HOVER_OUT, this::this_touchHoverOut);
        }

        private void this_touchHoverOut(Event event) {
            bg.setColor(Color.GRAY);
        }

        private void this_touchHover(Event event) {
            bg.setColor(Color.GREEN);
        }

        @Override
        public void setSize(float width, float height) {
            super.setSize(width, height);
            bg.setSize(width, height);
        }

    }
}
