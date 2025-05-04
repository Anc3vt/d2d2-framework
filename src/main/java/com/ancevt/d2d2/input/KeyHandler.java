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

package com.ancevt.d2d2.input;

import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.core.EventDispatcher;
import lombok.Getter;

import java.util.function.Consumer;

public class KeyHandler {

    private static final String EMPTY_STRING = "";

    private final int keyCode;
    private final int mods;
    private final EventDispatcher eventDispatcher;
    private final int keyAlias;
    private Consumer<Options> onKeyDown;
    private Consumer<Options> onKeyUp;
    private Consumer<Options> onKeyRepeat;
    private Consumer<Options> onKeyType;

    @Getter
    private boolean enabled;

    public KeyHandler(int keyCode, EventDispatcher eventDispatcher) {
        this(keyCode, 0, eventDispatcher);
    }

    public KeyHandler(int keyCode, int mods, EventDispatcher eventDispatcher) {
        this(keyCode, mods, eventDispatcher, 0);
    }

    public KeyHandler(int keyCode, EventDispatcher eventDispatcher, int keyAlias) {
        this(keyCode, 0, eventDispatcher, keyAlias);
    }

    public KeyHandler(int keyCode, int mods, EventDispatcher eventDispatcher, int keyAlias) {
        this.keyCode = keyCode;
        this.mods = mods;
        this.eventDispatcher = eventDispatcher;
        this.keyAlias = keyAlias;
        setEnabled(true);
    }

    public void setEnabled(boolean enabled) {
        if (enabled == this.enabled) return;
        this.enabled = enabled;

        if (enabled) {
            eventDispatcher.addEventListener(this, InputEvent.KeyDown.class, e -> {
                if ((e.keyCode() == keyCode || e.keyCode() == keyAlias) && onKeyDown != null) {
                    final boolean shift = (mods & 1) != 0;
                    final boolean control = (mods & 2) != 0;
                    final boolean alt = (mods & 4) != 0;

                    if (keyCode != KeyCode.LEFT_ALT && keyCode != KeyCode.RIGHT_ALT &&
                            keyCode != KeyCode.LEFT_SHIFT && keyCode != KeyCode.RIGHT_SHIFT &&
                            keyCode != KeyCode.LEFT_CONTROL && keyCode != KeyCode.RIGHT_CONTROL &&
                            ((e.shift() && !shift) || (e.shift() && !control) || (e.alt() && !alt) ||
                                    (!e.shift() && shift) || (!e.control() && control) || (!e.alt() && alt))) return;

                    onKeyDown.accept(new Options(e.shift(), e.control(), e.alt(), e.keyCode(), (char) 0, EMPTY_STRING));
                }
            });
            eventDispatcher.addEventListener(this, InputEvent.KeyUp.class, e -> {
                if ((e.keyCode() == keyCode || e.keyCode() == keyAlias) && onKeyUp != null) {
                    final boolean shift = (mods & 1) != 0;
                    final boolean control = (mods & 2) != 0;
                    final boolean alt = (mods & 4) != 0;

                    if (keyCode != KeyCode.LEFT_ALT && keyCode != KeyCode.RIGHT_ALT &&
                            keyCode != KeyCode.LEFT_SHIFT && keyCode != KeyCode.RIGHT_SHIFT &&
                            keyCode != KeyCode.LEFT_CONTROL && keyCode != KeyCode.RIGHT_CONTROL &&
                            ((e.shift() && !shift) || (e.control() && !control) || (e.alt() && !alt) ||
                                    (!e.shift() && shift) || (!e.control() && control) || (!e.alt() && alt))) return;

                    onKeyUp.accept(new Options(e.shift(), e.control(), e.alt(), e.keyCode(), (char) 0, EMPTY_STRING));
                }
            });
            eventDispatcher.addEventListener(this, InputEvent.KeyRepeat.class, e -> {
                if ((e.keyCode() == keyCode || e.keyCode() == keyAlias) && onKeyRepeat != null) {
                    final boolean shift = (mods & 1) != 0;
                    final boolean control = (mods & 2) != 0;
                    final boolean alt = (mods & 4) != 0;

                    if (keyCode != KeyCode.LEFT_ALT && keyCode != KeyCode.RIGHT_ALT &&
                            keyCode != KeyCode.LEFT_SHIFT && keyCode != KeyCode.RIGHT_SHIFT &&
                            keyCode != KeyCode.LEFT_CONTROL && keyCode != KeyCode.RIGHT_CONTROL &&
                            ((e.shift() && !shift) || (e.control() && !control) || (e.alt() && !alt) ||
                                    (!e.shift() && shift) || (!e.control() && control) || (!e.alt() && alt))) return;

                    onKeyRepeat.accept(new Options(e.shift(), e.control(), e.alt(), e.keyCode(), (char) 0, EMPTY_STRING));
                }
            });
            eventDispatcher.addEventListener(this, InputEvent.KeyType.class, e -> {
                if ((e.keyCode() == keyCode || e.keyCode() == keyAlias) && onKeyType != null) {
                    final boolean shift = (mods & 1) != 0;
                    final boolean control = (mods & 2) != 0;
                    final boolean alt = (mods & 4) != 0;

                    if (keyCode != KeyCode.LEFT_ALT && keyCode != KeyCode.RIGHT_ALT &&
                            keyCode != KeyCode.LEFT_SHIFT && keyCode != KeyCode.RIGHT_SHIFT &&
                            keyCode != KeyCode.LEFT_CONTROL && keyCode != KeyCode.RIGHT_CONTROL &&
                            ((e.shift() && !shift) || (e.control() && !control) || (e.alt() && !alt) ||
                                    (!e.shift() && shift) || (!e.control() && control) || (!e.alt() && alt))) return;

                    onKeyType.accept(new Options(e.shift(), e.control(), e.alt(), e.keyCode(), e.character(), e.keyType()));
                }
            });
        } else {
            eventDispatcher.removeEventListener(this, InputEvent.KeyDown.class);
            eventDispatcher.removeEventListener(this, InputEvent.KeyUp.class);
            eventDispatcher.removeEventListener(this, InputEvent.KeyRepeat.class);
            eventDispatcher.removeEventListener(this, InputEvent.KeyType.class);
        }
    }

    public KeyHandler registerOnKeyDown(Consumer<Options> fn) {
        onKeyDown = fn;
        return this;
    }

    public KeyHandler registerOnKeyUp(Consumer<Options> fn) {
        onKeyUp = fn;
        return this;
    }

    public KeyHandler registerOnKeyRepeat(Consumer<Options> fn) {
        onKeyRepeat = fn;
        return this;
    }

    public KeyHandler registerOnKeyType(Consumer<Options> fn) {
        onKeyType = fn;
        return this;
    }

    public KeyHandler registerOkKeyRepeatAsOnKeyDown() {
        onKeyRepeat = onKeyDown;
        return this;
    }

    public record Options(
            boolean shift,
            boolean control,
            boolean alt,
            int keyCode,
            char character,
            String keyType
    ) {
    }


    public static void main(String[] args) {
        int shift = 1;
        int control = 2;
        int alt = 4;

        int c = shift | alt;


        final boolean sh = (c & 1) != 0;
        final boolean co = (c & 2) != 0;
        final boolean al = (c & 4) != 0;

        System.out.println("shift: " + sh);
        System.out.println("control: " + co);
        System.out.println("alt: " + al);
    }


}
