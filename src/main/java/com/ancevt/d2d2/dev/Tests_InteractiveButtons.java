/**
 * Copyright (C) 2022 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.DisplayObjectContainer;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventListener;
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.interactive.InteractiveContainer;

import java.util.Objects;

public class Tests_InteractiveButtons {

    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, Tests_InteractiveButtons.class.getName() + "(floating)"));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Button button = new Button(50, 50);
                button.setXY(i * 50, j * 50);
                D2D2.stage().add(button);
            }
        }
        D2D2.stage().add(new FpsMeter());

        D2D2.loop();
    }


    private static class Button extends DisplayObjectContainer implements EventListener {
        private final PlainRect plainRect;

        public Button(int w, int h) {
            plainRect = new PlainRect(w, h, Color.DARK_GRAY);
            InteractiveContainer interactiveButton = new InteractiveContainer(w, h);
            interactiveButton.setEnabled(true);
            interactiveButton.addEventListener(InteractiveEvent.DOWN, this);
            interactiveButton.addEventListener(InteractiveEvent.DRAG, this::interactiveButton_drag);
            interactiveButton.addEventListener(InteractiveEvent.HOVER, this::interactiveButton_hover);
            interactiveButton.addEventListener(InteractiveEvent.OUT, this::interactiveButton_out);

            add(plainRect);
            add(interactiveButton);
        }

        private void interactiveButton_out(Event event) {
            plainRect.setColor(Color.DARK_GRAY);
        }

        private void interactiveButton_hover(Event event) {
            plainRect.setColor(Color.GRAY);
        }

        private void interactiveButton_drag(Event event) {
            InteractiveEvent e = (InteractiveEvent) event;
            if (e.isOnArea()) {
                plainRect.setColor(Color.DARK_GREEN);
            } else {
                plainRect.setColor(Color.DARK_RED);
            }
        }

        @Override
        public void onEvent(Event event) {
            if(Objects.equals(event.getType(), InteractiveEvent.DOWN)) {
                System.out.println(this);
            }
        }
    }
}
