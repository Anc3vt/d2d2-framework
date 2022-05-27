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
