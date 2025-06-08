/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.event;

import com.ancevt.d2d2.event.core.Event;
import com.ancevt.d2d2.event.core.EventPool;
import com.ancevt.d2d2.event.core.EventPooled;
import lombok.Getter;

public abstract class InputEvent extends Event {

    @EventPooled
    @Getter
    public static final class MouseDown extends InputEvent {
        private float x;
        private float y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static MouseDown create(float x,
                                       float y,
                                       int button,
                                       boolean left,
                                       boolean right,
                                       boolean middle,
                                       boolean alt,
                                       boolean control,
                                       boolean shift) {
            MouseDown e = EventPool.obtain(MouseDown.class);
            e.x = x;
            e.y = y;
            e.button = button;
            e.left = left;
            e.right = right;
            e.middle = middle;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class MouseUp extends InputEvent {
        private float x;
        private float y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
        private boolean onArea;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static MouseUp create(float x,
                                     float y,
                                     int button,
                                     boolean left,
                                     boolean right,
                                     boolean middle,
                                     boolean onArea,
                                     boolean alt,
                                     boolean control,
                                     boolean shift) {
            MouseUp e = EventPool.obtain(MouseUp.class);
            e.x = x;
            e.y = y;
            e.button = button;
            e.left = left;
            e.right = right;
            e.middle = middle;
            e.onArea = onArea;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class MouseMove extends InputEvent {
        private float x;
        private float y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
        private boolean onArea;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static MouseMove create(float x,
                                       float y,
                                       int button,
                                       boolean left,
                                       boolean right,
                                       boolean middle,
                                       boolean onArea,
                                       boolean alt,
                                       boolean control,
                                       boolean shift) {
            MouseMove e = EventPool.obtain(MouseMove.class);
            e.x = x;
            e.y = y;
            e.button = button;
            e.left = left;
            e.right = right;
            e.middle = middle;
            e.onArea = onArea;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class MouseWheel extends InputEvent {
        private int delta;
        private float x;
        private float y;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static MouseWheel create(int delta,
                                        float x,
                                        float y,
                                        boolean alt,
                                        boolean control,
                                        boolean shift) {
            MouseWheel e = EventPool.obtain(MouseWheel.class);
            e.delta = delta;
            e.x = x;
            e.y = y;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class KeyDown extends InputEvent {
        private int keyCode;
        private char character;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static KeyDown create(int keyCode,
                                     char character,
                                     boolean alt,
                                     boolean control,
                                     boolean shift) {
            KeyDown e = EventPool.obtain(KeyDown.class);
            e.keyCode = keyCode;
            e.character = character;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class KeyRepeat extends InputEvent {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static KeyRepeat create(int keyCode,
                                       boolean alt,
                                       boolean control,
                                       boolean shift) {
            KeyRepeat e = EventPool.obtain(KeyRepeat.class);
            e.keyCode = keyCode;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class KeyUp extends InputEvent {
        private int keyCode;
        private char character;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static KeyUp create(int keyCode,
                                   char character,
                                   boolean alt,
                                   boolean control,
                                   boolean shift) {
            KeyUp e = EventPool.obtain(KeyUp.class);
            e.keyCode = keyCode;
            e.character = character;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class KeyType extends InputEvent {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;
        private char character;
        private int codepoint;
        private String keyType;

        public static KeyType create(int keyCode,
                                     boolean alt,
                                     boolean control,
                                     boolean shift,
                                     char character,
                                     int codepoint,
                                     String keyType) {
            KeyType e = EventPool.obtain(KeyType.class);
            e.keyCode = keyCode;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            e.character = character;
            e.codepoint = codepoint;
            e.keyType = keyType;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class MouseHover extends InputEvent {
        private float x;
        private float y;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static MouseHover create(float x,
                                        float y,
                                        boolean alt,
                                        boolean control,
                                        boolean shift) {
            MouseHover e = EventPool.obtain(MouseHover.class);
            e.x = x;
            e.y = y;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class MouseOut extends InputEvent {
        private float x;
        private float y;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static MouseOut create(float x,
                                      float y,
                                      boolean alt,
                                      boolean control,
                                      boolean shift) {
            MouseOut e = EventPool.obtain(MouseOut.class);
            e.x = x;
            e.y = y;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class MouseDrag extends InputEvent {
        private float x;
        private float y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
        private boolean alt;
        private boolean control;
        private boolean shift;

        public static MouseDrag create(float x,
                                       float y,
                                       int button,
                                       boolean left,
                                       boolean right,
                                       boolean middle,
                                       boolean alt,
                                       boolean control,
                                       boolean shift) {
            MouseDrag e = EventPool.obtain(MouseDrag.class);
            e.x = x;
            e.y = y;
            e.button = button;
            e.left = left;
            e.right = right;
            e.middle = middle;
            e.alt = alt;
            e.control = control;
            e.shift = shift;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class FocusIn extends InputEvent {
        private boolean byMouseDown;

        public static FocusIn create(boolean byMouseDown) {
            var e = EventPool.obtain(FocusIn.class);
            e.byMouseDown = byMouseDown;
            return e;
        }
    }

    @EventPooled
    public static final class FocusOut extends InputEvent {
        public static FocusOut create() {
            return EventPool.obtain(FocusOut.class);
        }
    }
}
