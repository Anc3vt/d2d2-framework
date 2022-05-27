
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.DisplayObjectContainer;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventListener;
import com.ancevt.d2d2.event.TouchButtonEvent;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.interactive.TouchButton;

import java.util.Objects;

public class Tests_TouchButton {

    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, Tests_TouchButton.class.getName() + "(floating)"));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = new Button(50, 50);
                button.setXY(i * 50, j * 50);
                D2D2.getStage().getRoot().add(button);
            }
        }
        D2D2.getStage().getRoot().add(new FpsMeter());

        D2D2.loop();
    }


    private static class Button extends DisplayObjectContainer implements EventListener {
        private final PlainRect plainRect;

        public Button(int w, int h) {
            plainRect = new PlainRect(w, h, Color.DARK_GRAY);
            TouchButton touchButton = new TouchButton(w, h);
            touchButton.setEnabled(true);
            touchButton.addEventListener(TouchButtonEvent.TOUCH_DOWN, this);
            touchButton.addEventListener(TouchButtonEvent.TOUCH_DRAG, this::touchDrag);
            touchButton.addEventListener(TouchButtonEvent.TOUCH_HOVER, this::touchHover);


            add(plainRect);
            add(touchButton);
        }

        private void touchHover(Event event) {
            TouchButtonEvent e = (TouchButtonEvent) event;
            plainRect.setColor(Color.GRAY);
        }

        private void touchDrag(Event event) {
            TouchButtonEvent e = (TouchButtonEvent) event;
            if (e.isOnArea()) {
                plainRect.setColor(Color.DARK_GREEN);
            } else {
                plainRect.setColor(Color.DARK_GRAY);
            }
        }

        @Override
        public void onEvent(Event event) {
            if(Objects.equals(event.getType(), TouchButtonEvent.TOUCH_DOWN)) {
                System.out.println(this);
            }
        }
    }
}
