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
/*
 *     Copyright 2015-2022 Ancevt (me@ancevt.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class InputEvent extends Event {

    public static final String BACK_PRESS = "backPress";
    public static final String MOUSE_MOVE = "mouseMove";
    public static final String MOUSE_DOWN = "mouseDown";
    public static final String MOUSE_UP = "mouseUp";
    public static final String MOUSE_WHEEL = "mouseWheel";
    public static final String KEY_DOWN = "keyDown";
    public static final String KEY_UP = "keyUp";
    public static final String KEY_TYPE = "keyType";

    private final int x;
    private final int y;
    private final int mouseButton;
    private final int delta;
    private final boolean drag;
    private final int pointer;
    private final int keyCode;
    private final char keyChar;
    private final boolean shift;
    private final boolean control;
    private final boolean alt;
    private final String keyType;
    private final int codepoint;

    @Override
    public String toString() {
        return "InputEvent{" +
                "x=" + x +
                ", y=" + y +
                ", mouseButton=" + mouseButton +
                ", delta=" + delta +
                ", drag=" + drag +
                ", pointer=" + pointer +
                ", keyCode=" + keyCode +
                ", keyChar=" + keyChar +
                ", shift=" + shift +
                ", control=" + control +
                ", alt=" + alt +
                '}';
    }
}
