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

package com.ancevt.d2d2.input;

import com.ancevt.d2d2.event.dispatch.EventDispatcher;
import com.ancevt.d2d2.event.InteractiveEvent_toRemove;
import lombok.Getter;

import java.util.function.Consumer;

public class KeyHandler {

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
            eventDispatcher.addEventListener(this, InteractiveEvent_toRemove.KEY_DOWN, event -> {
                InteractiveEvent_toRemove e = event.casted();
                if ((e.getKeyCode() == keyCode || e.getKeyCode() == keyAlias) && onKeyDown != null) {
                    final boolean shift = (mods & 1) != 0;
                    final boolean control = (mods & 2) != 0;
                    final boolean alt = (mods & 4) != 0;

                    if (keyCode != KeyCode.LEFT_ALT && keyCode != KeyCode.RIGHT_ALT &&
                        keyCode != KeyCode.LEFT_SHIFT && keyCode != KeyCode.RIGHT_SHIFT &&
                        keyCode != KeyCode.LEFT_CONTROL && keyCode != KeyCode.RIGHT_CONTROL &&
                        ((e.isShift() && !shift) || (e.isControl() && !control) || (e.isAlt() && !alt) ||
                            (!e.isShift() && shift) || (!e.isControl() && control) || (!e.isAlt() && alt))) return;

                    onKeyDown.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent_toRemove.KEY_UP, event -> {
                InteractiveEvent_toRemove e = event.casted();
                if ((e.getKeyCode() == keyCode || e.getKeyCode() == keyAlias) && onKeyUp != null) {
                    final boolean shift = (mods & 1) != 0;
                    final boolean control = (mods & 2) != 0;
                    final boolean alt = (mods & 4) != 0;

                    if (keyCode != KeyCode.LEFT_ALT && keyCode != KeyCode.RIGHT_ALT &&
                        keyCode != KeyCode.LEFT_SHIFT && keyCode != KeyCode.RIGHT_SHIFT &&
                        keyCode != KeyCode.LEFT_CONTROL && keyCode != KeyCode.RIGHT_CONTROL &&
                        ((e.isShift() && !shift) || (e.isControl() && !control) || (e.isAlt() && !alt) ||
                            (!e.isShift() && shift) || (!e.isControl() && control) || (!e.isAlt() && alt))) return;

                    onKeyUp.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent_toRemove.KEY_REPEAT, event -> {
                InteractiveEvent_toRemove e = event.casted();
                if ((e.getKeyCode() == keyCode || e.getKeyCode() == keyAlias) && onKeyRepeat != null) {
                    final boolean shift = (mods & 1) != 0;
                    final boolean control = (mods & 2) != 0;
                    final boolean alt = (mods & 4) != 0;

                    if (keyCode != KeyCode.LEFT_ALT && keyCode != KeyCode.RIGHT_ALT &&
                        keyCode != KeyCode.LEFT_SHIFT && keyCode != KeyCode.RIGHT_SHIFT &&
                        keyCode != KeyCode.LEFT_CONTROL && keyCode != KeyCode.RIGHT_CONTROL &&
                        ((e.isShift() && !shift) || (e.isControl() && !control) || (e.isAlt() && !alt) ||
                            (!e.isShift() && shift) || (!e.isControl() && control) || (!e.isAlt() && alt))) return;

                    onKeyRepeat.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent_toRemove.KEY_TYPE, event -> {
                InteractiveEvent_toRemove e = event.casted();
                if ((e.getKeyCode() == keyCode || e.getKeyCode() == keyAlias) && onKeyType != null) {
                    final boolean shift = (mods & 1) != 0;
                    final boolean control = (mods & 2) != 0;
                    final boolean alt = (mods & 4) != 0;

                    if (keyCode != KeyCode.LEFT_ALT && keyCode != KeyCode.RIGHT_ALT &&
                        keyCode != KeyCode.LEFT_SHIFT && keyCode != KeyCode.RIGHT_SHIFT &&
                        keyCode != KeyCode.LEFT_CONTROL && keyCode != KeyCode.RIGHT_CONTROL &&
                        ((e.isShift() && !shift) || (e.isControl() && !control) || (e.isAlt() && !alt) ||
                            (!e.isShift() && shift) || (!e.isControl() && control) || (!e.isAlt() && alt))) return;

                    onKeyType.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
        } else {
            eventDispatcher.removeEventListener(this, InteractiveEvent_toRemove.KEY_DOWN);
            eventDispatcher.removeEventListener(this, InteractiveEvent_toRemove.KEY_UP);
            eventDispatcher.removeEventListener(this, InteractiveEvent_toRemove.KEY_REPEAT);
            eventDispatcher.removeEventListener(this, InteractiveEvent_toRemove.KEY_TYPE);
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
