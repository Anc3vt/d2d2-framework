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
import com.ancevt.d2d2.display.DisplayObjectContainer;
import com.ancevt.d2d2.display.IDisplayObjectContainer;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.interactive.InteractiveContainer;
import com.ancevt.d2d2.interactive.InteractiveManager;

public class Tests_InteractiveButtonsFocus {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));

        InteractiveManager.getInstance().setTabbingEnabled(true);

        stage.setBackgroundColor(Color.BLACK);

        IDisplayObjectContainer doc1 = new DisplayObjectContainer();
        for (int i = 0; i < 10; i++) {
            Button button = new Button(100, 50);
            button.setName("button_1_" + i);
            doc1.add(button, 0, i * 55);
        }

        IDisplayObjectContainer doc2 = new DisplayObjectContainer();
        for (int i = 0; i < 10; i++) {
            Button button = new Button(100, 50);
            button.setName("button_2_" + i);
            doc2.add(button, 0, i * 55);

            if(i == 5) button.removeFromParent();
        }

        stage.add(doc1, 50, 50);
        stage.add(doc2, 200, 50);

        D2D2.loop();
    }

    private static class Button extends InteractiveContainer {

        private final BorderedRect bg = new BorderedRect();

        private Button(float width, float height) {
            setEnabled(true);
            setTabbingEnabled(true);
            bg.setFillColor(Color.GRAY);
            bg.setBorderColor(Color.BLACK);
            setSize(width, height);
            add(bg);

            addEventListener(this, InteractiveEvent.DOWN, this::this_down);
            addEventListener(this, InteractiveEvent.UP, this::this_up);
            addEventListener(this, InteractiveEvent.HOVER, this::this_hover);
            addEventListener(this, InteractiveEvent.OUT, this::this_out);
            addEventListener(this, InteractiveEvent.DRAG, this::this_drag);
            addEventListener(this, InteractiveEvent.FOCUS_IN, this::this_focusIn);
            addEventListener(this, InteractiveEvent.FOCUS_OUT, this::this_focusOut);
            addEventListener(this, InteractiveEvent.KEY_DOWN, this::this_keyDown);
            addEventListener(this, InteractiveEvent.KEY_UP, this::this_keyUp);
            addEventListener(this, InteractiveEvent.KEY_TYPE, this::this_keyType);
        }

        private void this_keyDown(Event event) {
            var e = (InteractiveEvent) event;
            System.out.println(getName() + " KEY_DOWN " + e.getKeyCode());
        }

        private void this_keyUp(Event event) {
            var e = (InteractiveEvent) event;
            System.out.println(getName() + " KEY_UP " + e.getKeyCode());
        }

        private void this_keyType(Event event) {
            var e = (InteractiveEvent) event;
            System.out.println(getName() + " KEY_TYPE " + e.getKeyCode() + " " + e.getKeyType());
        }

        private void this_focusIn(Event event) {
            bg.setBorderColor(Color.YELLOW);
            System.out.println("FOCUS_IN " + getName());
        }

        private void this_focusOut(Event event) {
            bg.setBorderColor(Color.BLACK);
            System.out.println("FOCUS_OUT " + getName());
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
