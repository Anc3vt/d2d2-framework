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
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.TouchButtonEvent;
import com.ancevt.d2d2.interactive.TouchButton;

public class Tests_TouchButton {

    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        Button button = new Button();
        button.setName("___");
        D2D2.stage().add(button, 0, 0);
        D2D2.stage().add(new FpsMeter());
        D2D2.loop();
    }

    private static class Button extends TouchButton {

        private final PlainRect bg = new PlainRect();

        private Button() {
            setEnabled(true);
            bg.setColor(Color.GRAY);
            setSize(100, 100);
            add(bg);

            addEventListener(this, TouchButtonEvent.DOWN, this::this_touchDown);
            addEventListener(this, TouchButtonEvent.UP, this::this_touchUp);
            addEventListener(this, TouchButtonEvent.HOVER, this::this_touchHover);
            addEventListener(this, TouchButtonEvent.OUT, this::this_touchHoverOut);

            TouchButton tb = new TouchButton(50, 50, true);
            tb.addEventListener(TouchButtonEvent.DOWN, event -> {
                System.out.println("SMALL BUTTON PRESSED");

                Button button = new Button();
                button.addEventListener(TouchButtonEvent.DOWN, event1 -> {
                    var e = (TouchButtonEvent) event1;
                    button.bg.setColor(Color.YELLOW);

                });
                add(button, 40, 40);

            });
            add(tb);
        }

        private void this_touchUp(Event event) {
            bg.setColor(Color.GRAY);
        }

        private void this_touchDown(Event event) {
            bg.setColor(Color.YELLOW);
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
