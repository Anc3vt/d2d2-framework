/**
 * Copyright (C) 2025 the original author or authors.
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

package com.ancevt.d2d2.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public abstract class InputEvent<S> extends Event<S> {

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class MouseDown<S> extends InputEvent<S> {
        private int x;
        private int y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class MouseUp<S> extends InputEvent<S> {
        private int x;
        private int y;
        private int button;
        private boolean left;
        private boolean right;
        private boolean middle;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class MouseMove<S> extends InputEvent<S> {
        private int x;
        private int y;
        private boolean onArea;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class MouseWheel<S> extends InputEvent<S> {
        private int delta;
        private int x;
        private int y;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class KeyDown<S> extends InputEvent<S> {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class KeyRepeat<S> extends InputEvent<S> {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class KeyUp<S> extends InputEvent<S> {
        private int keyCode;
        private boolean alt;
        private boolean control;
        private boolean shift;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class KeyType<S> extends InputEvent<S> {
        private char character;
        private int codepoint;
        private String keyType;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class MouseHover<S> extends InputEvent<S> {
        private int x;
        private int y;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class MouseOut<S> extends InputEvent<S> {
        private int x;
        private int y;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class MouseDrag<S> extends InputEvent<S> {
        private int x;
        private int y;
        private int button;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class FocusIn<S> extends InputEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class FocusOut<S> extends InputEvent<S> {
    }
}
