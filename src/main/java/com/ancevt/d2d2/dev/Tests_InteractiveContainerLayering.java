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
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.interactive.InteractiveContainer;

import static com.ancevt.d2d2.D2D2.init;
import static com.ancevt.d2d2.D2D2.loop;
import static com.ancevt.d2d2.D2D2.stage;

public class Tests_InteractiveContainerLayering {

    public static void main(String[] args) {
        init(new LWJGLBackend(800, 600, "(floating)"));

        stage().setBackgroundColor(Color.of(0x001122));


        for (int i = 0; i < 2500; i++) {
            Button button = new Button();
            button.setName("__" + i);
            stage().add(button, (float) (Math.random() * 800), (float) (Math.random() * 600));
        }

        stage().add(new FpsMeter());

        loop();
    }

    private static class Button extends InteractiveContainer {

        private final BorderedRect bg = new BorderedRect();

        private Button() {
            setEnabled(true);
            bg.setFillColor(Color.GRAY);
            bg.setBorderColor(Color.BLACK);
            setSize(100, 100);
            add(bg);

            addEventListener(this, InteractiveEvent.DOWN, this::this_down);
            addEventListener(this, InteractiveEvent.UP, this::this_up);
            addEventListener(this, InteractiveEvent.HOVER, this::this_hover);
            addEventListener(this, InteractiveEvent.OUT, this::this_out);
            addEventListener(this, InteractiveEvent.DRAG, this::this_drag);
        }

        private void this_drag(Event event) {
            bg.setFillColor(Color.BLUE);
        }

        private void this_up(Event event) {
            var e = (InteractiveEvent) event;
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
