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
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.interactive.InteractiveContainer;

public class Tests_InteractiveButton {

    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        Button button = new Button();
        button.setName("___");
        D2D2.stage().add(button, 0, 0);
        D2D2.stage().add(new FpsMeter());
        D2D2.loop();
    }

    private static class Button extends InteractiveContainer {

        private final PlainRect bg = new PlainRect();

        private Button() {
            setEnabled(true);
            bg.setColor(Color.GRAY);
            setSize(100, 100);
            add(bg);

            addEventListener(this, InteractiveEvent.DOWN, this::this_down);
            addEventListener(this, InteractiveEvent.UP, this::this_up);
            addEventListener(this, InteractiveEvent.HOVER, this::this_hover);
            addEventListener(this, InteractiveEvent.OUT, this::this_out);

            InteractiveContainer tb = new InteractiveContainer(50, 50, true);
            tb.addEventListener(InteractiveEvent.DOWN, event -> {
                System.out.println("SMALL BUTTON PRESSED");

                Button button = new Button();
                button.addEventListener(InteractiveEvent.DOWN, event1 -> {
                    var e = (InteractiveEvent) event1;
                    button.bg.setColor(Color.YELLOW);

                });
                add(button, 40, 40);

            });
            add(tb);
        }

        private void this_up(Event event) {
            bg.setColor(Color.GRAY);
        }

        private void this_down(Event event) {
            bg.setColor(Color.YELLOW);
        }

        private void this_out(Event event) {
            bg.setColor(Color.GRAY);
        }

        private void this_hover(Event event) {
            bg.setColor(Color.GREEN);
        }

        @Override
        public void setSize(float width, float height) {
            super.setSize(width, height);
            bg.setSize(width, height);
        }

    }
}
