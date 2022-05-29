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
import com.ancevt.d2d2.common.BorderedRect;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.FocusEvent;
import com.ancevt.d2d2.event.InteractiveButtonEvent;
import com.ancevt.d2d2.interactive.InteractiveButton;

public class Tests_InteractiveButtonsFocus {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));

        InteractiveButton.setTabbingEnabled(true);

        stage.setBackgroundColor(Color.WHITE);

        for (int i = 0; i < 10; i++) {
            Button button = new Button(100, 50);
            stage.add(button, 50, 25 + i * 55);
        }

        D2D2.loop();
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
            addEventListener(this, FocusEvent.FOCUS_IN, this::this_focusIn);
            addEventListener(this, FocusEvent.FOCUS_OUT, this::this_focusOut);
        }

        private void this_focusIn(Event event) {
            bg.setBorderColor(Color.YELLOW);
        }

        private void this_focusOut(Event event) {
            bg.setBorderColor(Color.BLACK);
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
