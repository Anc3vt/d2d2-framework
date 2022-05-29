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

import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.common.BorderedRect;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.InteractiveButtonEvent;
import com.ancevt.d2d2.interactive.InteractiveButton;

import static com.ancevt.d2d2.D2D2.init;
import static com.ancevt.d2d2.D2D2.loop;
import static com.ancevt.d2d2.D2D2.stage;

public class Tests_InteractiveButtonInInteractiveButton {

    public static void main(String[] args) {
        init(new LWJGLBackend(800, 600, "(floating)"));

        stage().setBackgroundColor(Color.of(0x000011));

        Button outer = new Button(200, 200);
        stage().add(outer, 200, 200);

        Button inner = new Button(100, 100);
        stage().add(inner, 250, 250);

        Button inner2 = new Button(50, 50);
        inner.add(inner2, 25, 25);

        stage().add(new FpsMeter());

        loop();
    }

    private static class Button extends InteractiveButton {

        private final BorderedRect bg = new BorderedRect();

        private Button(float width, float height) {
            setEnabled(true);
            bg.setFillColor(Color.GRAY);
            bg.setBorderColor(Color.BLACK);
            setSize(width, height);
            add(bg);

            addEventListener(this, InteractiveButtonEvent.DOWN, this::this_down);
            addEventListener(this, InteractiveButtonEvent.UP, this::this_up);
            addEventListener(this, InteractiveButtonEvent.HOVER, this::this_hover);
            addEventListener(this, InteractiveButtonEvent.OUT, this::this_out);
            addEventListener(this, InteractiveButtonEvent.DRAG, this::this_drag);
        }

        private void this_drag(Event event) {
            bg.setFillColor(Color.BLUE);
        }

        private void this_up(Event event) {
            var e = (InteractiveButtonEvent) event;
            bg.setFillColor(e.isOnArea() ? Color.GREEN : Color.GRAY);
        }

        private void this_down(Event event) {
            bg.setFillColor(Color.YELLOW);
        }

        private void this_out(Event event) {
            bg.setFillColor(Color.GRAY);
        }

        private void this_hover(Event event) {
            bg.setFillColor(Color.GREEN);
        }

        @Override
        public void setSize(float width, float height) {
            super.setSize(width, height);
            bg.setSize(width, height);
        }

    }
}
