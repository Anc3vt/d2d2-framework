/**
 * Copyright (C) 2024 the original author or authors.
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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class InteractiveEvent extends Event {

    public static final String DOWN  = "interactiveDown";
    public static final String UP    = "interactiveUp";
    public static final String DRAG  = "interactiveDrag";
    public static final String HOVER = "interactiveHover";
    public static final String OUT   = "interactiveOut";
    public static final String WHEEL = "interactiveWheel";
    public static final String FOCUS_IN = "interactiveFocusIn";
    public static final String FOCUS_OUT = "interactiveFocusOut";
    public static final String KEY_DOWN = "interactiveKeyDown";
    public static final String KEY_REPEAT = "interactiveKeyRepeat";
    public static final String KEY_UP = "interactiveKeyUp";
    public static final String KEY_TYPE = "interactiveKeyType";

    private final int x;
    private final int y;
    private final int mouseButton;
    private final boolean leftMouseButton;
    private final boolean rightMouseButton;
    private final boolean middleMouseButton;
    private final boolean onArea;
    private final int keyCode;
    private final char keyChar;
    private final boolean alt;
    private final boolean control;
    private final boolean shift;
    private final String keyType;
    private final boolean byMouseDown;
    private final int delta;
}
