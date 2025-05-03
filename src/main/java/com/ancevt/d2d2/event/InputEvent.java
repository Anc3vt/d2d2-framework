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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public abstract class InputEvent extends Event {

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class MouseDown extends InputEvent {
        private int x;
        private int y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class MouseUp extends InputEvent {
        private int x;
        private int y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class MouseMove extends InputEvent {
        private int x;
        private int y;
        private boolean onArea;
        private boolean alt;
        private boolean control;
        private boolean shift;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class MouseWheel extends InputEvent {
        private int delta;
        private int x;
        private int y;
        private boolean alt;
        private boolean control;
        private boolean shift;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class KeyDown extends InputEvent {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class KeyRepeat extends InputEvent {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class KeyUp extends InputEvent {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class KeyType extends InputEvent {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;
        private char character;
        private int codepoint;
        private String keyType;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class MouseHover extends InputEvent {
        private int x;
        private int y;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class MouseOut extends InputEvent {
        private int x;
        private int y;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    public static final class MouseDrag extends InputEvent {
        private int x;
        private int y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
    }

    @NoArgsConstructor(staticName = "create")
    public static final class FocusIn extends InputEvent {
    }

    @NoArgsConstructor(staticName = "create")
    public static final class FocusOut extends InputEvent {
    }
}
