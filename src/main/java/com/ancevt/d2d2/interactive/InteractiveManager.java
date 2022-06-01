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
package com.ancevt.d2d2.interactive;

import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.exception.InteractiveException;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.MouseButton;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ancevt.d2d2.D2D2.stage;
import static com.ancevt.d2d2.event.InteractiveEvent.DOWN;
import static com.ancevt.d2d2.event.InteractiveEvent.DRAG;
import static com.ancevt.d2d2.event.InteractiveEvent.HOVER;
import static com.ancevt.d2d2.event.InteractiveEvent.OUT;
import static com.ancevt.d2d2.event.InteractiveEvent.UP;

public class InteractiveManager {

    private static InteractiveManager instance;

    public static InteractiveManager getInstance() {
        return instance == null ? instance = new InteractiveManager() : instance;
    }

    private final List<Interactive> interactiveList;
    private static final int KEY_HOLD_TIME = 30;

    private boolean leftMouseButton;
    private boolean rightMouseButton;
    private boolean middleMouseButton;
    private Interactive hoveredInteractive;
    private Interactive focusedInteractive;
    private int focusedInteractiveIndex;
    private boolean tabbingEnabled;
    private int keyHoldTime = KEY_HOLD_TIME;
    private int keyHoldTabDirection;

    private InteractiveManager() {
        interactiveList = new CopyOnWriteArrayList<>();
        focusedInteractiveIndex = -1;

        stage().addEventListener(InputEvent.KEY_DOWN, event -> {
            var e = (InputEvent) event;
            Interactive focused = getFocused();
            if (focused != null) {
                focused.dispatchEvent(InteractiveEvent.builder()
                        .type(InteractiveEvent.KEY_DOWN)
                        .keyChar(e.getKeyChar())
                        .keyCode(e.getKeyCode())
                        .alt(e.isAlt())
                        .control(e.isControl())
                        .shift(e.isShift())
                        .build());
            }
        });

        stage().addEventListener(InputEvent.KEY_UP, event -> {
            var e = (InputEvent) event;
            Interactive focused = getFocused();
            if (focused != null) {
                focused.dispatchEvent(InteractiveEvent.builder()
                        .type(InteractiveEvent.KEY_UP)
                        .keyChar(e.getKeyChar())
                        .keyCode(e.getKeyCode())
                        .alt(e.isAlt())
                        .control(e.isControl())
                        .shift(e.isShift())
                        .build());
            }
        });

        stage().addEventListener(InputEvent.KEY_TYPE, event -> {
            var e = (InputEvent) event;
            Interactive focused = getFocused();
            if (focused != null) {
                focused.dispatchEvent(InteractiveEvent.builder()
                        .type(InteractiveEvent.KEY_TYPE)
                        .keyChar(e.getKeyChar())
                        .keyCode(e.getKeyCode())
                        .keyType(e.getKeyType())
                        .alt(e.isAlt())
                        .control(e.isControl())
                        .shift(e.isShift())
                        .build());
            }
        });
    }

    public void registerInteractive(final Interactive interactive) {
        if (!interactiveList.contains(interactive)) {
            interactive.addEventListener(this, Event.REMOVE_FROM_STAGE, event -> {
                if (interactive.isFocused()) {
                    resetFocus();
                }
            });
            interactiveList.add(interactive);
        }
    }

    public final void unregisterInteractive(final Interactive interactive) {
        interactiveList.remove(interactive);
        interactive.removeEventListener(this, Event.REMOVE_FROM_STAGE);
    }

    public final void clear() {
        while (!interactiveList.isEmpty()) {
            interactiveList.remove(0);
        }
    }

    public final void screenTouch(final int x, final int y, final int pointer, int mouseButton, final boolean down) {
        switch (mouseButton) {
            case MouseButton.LEFT -> leftMouseButton = down;
            case MouseButton.RIGHT -> rightMouseButton = down;
            case MouseButton.MIDDLE -> middleMouseButton = down;
        }

        Interactive pressedInteractive = null;

        if (down) {

            int maxIndex = 0;
            float _tcX = 0.0f, _tcY = 0.0f;

            for (Interactive interactive : interactiveList) {
                final float tcX = interactive.getAbsoluteX();
                final float tcY = interactive.getAbsoluteY();
                final float tcW = interactive.getInteractiveArea().getWidth() * interactive.getAbsoluteScaleX();
                final float tcH = interactive.getInteractiveArea().getHeight() * interactive.getAbsoluteScaleY();

                if (interactive.isOnScreen() && x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH) {
                    int index = interactive.getAbsoluteZOrderIndex();
                    if (index >= maxIndex) {
                        pressedInteractive = interactive;
                        maxIndex = index;
                        _tcX = tcX;
                        _tcY = tcY;
                    }
                }
            }

            if (pressedInteractive != null) {
                setFocused(pressedInteractive, true);

                dispatch(pressedInteractive,
                        DOWN,
                        (int) (x - _tcX),
                        (int) (y - _tcY),
                        true,
                        leftMouseButton,
                        rightMouseButton,
                        middleMouseButton,
                        mouseButton
                );

                pressedInteractive.setDragging(true);
            }

        } else {
            for (Interactive interactive : interactiveList) {
                if (interactive != null) {

                    if (interactive.isOnScreen()) {
                        final float tcX = interactive.getAbsoluteX();
                        final float tcY = interactive.getAbsoluteY();
                        final float tcW = interactive.getInteractiveArea().getWidth() * interactive.getAbsoluteScaleX();
                        final float tcH = interactive.getInteractiveArea().getHeight() * interactive.getAbsoluteScaleY();

                        final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

                        if (interactive.isDragging()) {
                            dispatch(interactive,
                                    UP,
                                    (int) (x - tcX),
                                    (int) (y - tcY),
                                    onArea,
                                    leftMouseButton,
                                    rightMouseButton,
                                    middleMouseButton,
                                    mouseButton
                            );

                            interactive.setDragging(false);
                        }
                    }
                }
            }
        }
    }

    public final void screenDrag(int pointer, final int x, final int y) {
        float _tcX = 0.0f, _tcY = 0.0f;
        int maxIndex = 0;
        Interactive upperIntaractive = null;

        for (final Interactive interactive : interactiveList) {
            final float tcX = interactive.getAbsoluteX();
            final float tcY = interactive.getAbsoluteY();
            final float tcW = interactive.getInteractiveArea().getWidth() * interactive.getAbsoluteScaleX();
            final float tcH = interactive.getInteractiveArea().getHeight() * interactive.getAbsoluteScaleY();

            final boolean onScreen = interactive.isOnScreen();
            final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

            if (onScreen) {


                if (onArea) {
                    int index = interactive.getAbsoluteZOrderIndex();
                    if (index >= maxIndex) {
                        maxIndex = index;
                        _tcX = tcX;
                        _tcY = tcY;
                        upperIntaractive = interactive;
                    }
                }

                if (interactive.isDragging()) {
                    dispatch(interactive,
                            DRAG,
                            (int) (x - tcX),
                            (int) (y - tcY),
                            onArea,
                            leftMouseButton,
                            rightMouseButton,
                            middleMouseButton,
                            0
                    );
                }

                if (interactive.isHovering() && !onArea) {
                    interactive.setHovering(false);
                    dispatch(interactive,
                            OUT,
                            (int) (x - tcX),
                            (int) (y - tcY),
                            false,
                            leftMouseButton,
                            rightMouseButton,
                            middleMouseButton,
                            0
                    );
                }

            }

        }
        if (upperIntaractive != null) {
            if (!upperIntaractive.isHovering()) {
                if (hoveredInteractive != null) {
                    dispatch(hoveredInteractive,
                            OUT,
                            (int) (x - _tcX),
                            (int) (y - _tcY),
                            false,
                            leftMouseButton,
                            rightMouseButton,
                            middleMouseButton,
                            0
                    );
                    hoveredInteractive.setHovering(false);
                }

                hoveredInteractive = upperIntaractive;

                upperIntaractive.setHovering(true);
                dispatch(upperIntaractive,
                        HOVER,
                        (int) (x - _tcX),
                        (int) (y - _tcY),
                        true,
                        leftMouseButton,
                        rightMouseButton,
                        middleMouseButton,
                        0
                );
            }
        }
    }

    public void setFocused(Interactive interactive, boolean byMouseDown) {
        if (focusedInteractive == interactive) return;


        if (focusedInteractive != null) {
            focusedInteractive.dispatchEvent(InteractiveEvent.builder()
                    .type(InteractiveEvent.FOCUS_OUT)
                    .build());
        }

        int index = interactiveList.indexOf(interactive);
        if (index == -1)
            throw new InteractiveException("Unable to focus unregistered Interactive " + interactive);

        focusedInteractive = interactive;
        focusedInteractiveIndex = index;

        focusedInteractive.dispatchEvent(InteractiveEvent.builder()
                .type(InteractiveEvent.FOCUS_IN)
                .byMouseDown(byMouseDown)
                .build());
    }

    public void setFocused(int index) {
        if (interactiveList.size() == 0) return;

        focusedInteractiveIndex = index;

        if (focusedInteractiveIndex < 0)
            focusedInteractiveIndex = 0;
        else if (focusedInteractiveIndex >= interactiveList.size())
            focusedInteractiveIndex = interactiveList.size() - 1;

        if (focusedInteractive == interactiveList.get(focusedInteractiveIndex)) return;

        if (focusedInteractive != null) {
            focusedInteractive.dispatchEvent(InteractiveEvent.builder()
                    .type(InteractiveEvent.FOCUS_OUT)
                    .build());
        }

        focusedInteractive = interactiveList.get(focusedInteractiveIndex);

        focusedInteractive.dispatchEvent(InteractiveEvent.builder()
                .type(InteractiveEvent.FOCUS_IN)
                .build());
    }

    public Interactive getFocused() {
        return focusedInteractive;
    }

    public void focusNext() {
        if (interactiveList.size() == 0 || getTabbingEnabledAndOnScreenAndVisibleCount() == 0) return;
        focusedInteractiveIndex++;
        if (focusedInteractiveIndex >= interactiveList.size()) focusedInteractiveIndex = 0;

        if (focusedInteractive != null) {
            dispatch(focusedInteractive, OUT, 0, 0, false, false, false, false, 0);
        }

        setFocused(focusedInteractiveIndex);

        if (!getFocused().isTabbingEnabled() || !getFocused().isOnScreen() || !getFocused().isVisible()) focusNext();
    }

    public void focusPrevious() {
        if (interactiveList.size() == 0 || getTabbingEnabledAndOnScreenAndVisibleCount() == 0) return;
        focusedInteractiveIndex--;
        if (focusedInteractiveIndex < 0) focusedInteractiveIndex = interactiveList.size() - 1;

        if (focusedInteractive != null) {
            dispatch(focusedInteractive, OUT, 0, 0, false, false, false, false, 0);
        }

        setFocused(focusedInteractiveIndex);

        if (!getFocused().isTabbingEnabled() || !getFocused().isOnScreen() || !getFocused().isVisible())
            focusPrevious();
    }

    private int getTabbingEnabledAndOnScreenAndVisibleCount() {
        int count = 0;
        for (Interactive interactive : interactiveList) {
            if (interactive.isTabbingEnabled() && interactive.isOnScreen() && interactive.isVisible())
                count++;
        }
        return count;
    }

    public void setTabbingEnabled(boolean tabbingEnabled) {
        if (this.tabbingEnabled == tabbingEnabled) return;

        this.tabbingEnabled = tabbingEnabled;

        if (tabbingEnabled) {
            stage().addEventListener(this, InputEvent.KEY_DOWN, event -> {
                var e = (InputEvent) event;
                switch (e.getKeyCode()) {
                    case KeyCode.TAB -> {
                        if (e.isShift()) {
                            focusPrevious();
                            keyHoldTabDirection = -1;
                        } else {
                            focusNext();
                            keyHoldTabDirection = 1;
                        }
                        stage().addEventListener(this, InputEvent.EACH_FRAME, event1 -> {
                            keyHoldTime--;
                            if (keyHoldTime < 0) {
                                keyHoldTime = 3;
                                if (keyHoldTabDirection == 1) {
                                    focusNext();
                                } else {
                                    focusPrevious();
                                }
                            }
                        });
                    }
                    case KeyCode.ENTER -> {
                        if (focusedInteractive != null) {
                            dispatch(focusedInteractive, DOWN, 0, 0, true, false, false, false, 0);
                        }
                    }
                    case KeyCode.ESCAPE -> {
                        resetFocus();
                    }
                }
            });
            stage().addEventListener(this, InputEvent.KEY_UP, event -> {
                var e = (InputEvent) event;

                switch (e.getKeyCode()) {
                    case KeyCode.TAB -> {
                        keyHoldTime = KEY_HOLD_TIME;
                        keyHoldTabDirection = 0;
                        stage().removeEventListener(this, Event.EACH_FRAME);
                    }
                    case KeyCode.ENTER -> {
                        if (focusedInteractive != null) {
                            dispatch(focusedInteractive, UP, 0, 0, true, false, false, false, 0);
                        }
                    }
                }
            });
        } else {
            stage().removeEventListener(this, InputEvent.KEY_DOWN);
            stage().removeEventListener(this, InputEvent.KEY_UP);
            stage().removeEventListener(this, Event.EACH_FRAME);
        }
    }

    public boolean isTabbingEnabled() {
        return tabbingEnabled;
    }

    public void resetFocus() {
        if (focusedInteractive != null) {
            focusedInteractive.dispatchEvent(InteractiveEvent.builder()
                    .type(InteractiveEvent.FOCUS_OUT)
                    .build());

            dispatch(focusedInteractive, OUT, 0, 0, false, false, false, false, 0);
        }

        focusedInteractiveIndex = -1;
        focusedInteractive = null;
    }

    private static void dispatch(Interactive interactive,
                                 String type,
                                 int x,
                                 int y,
                                 boolean onArea,
                                 boolean leftMouseButton,
                                 boolean rightMouseButton,
                                 boolean middleMouseButton,
                                 int mouseButton) {

        interactive.dispatchEvent(InteractiveEvent.builder()
                .type(type)
                .x(x)
                .y(y)
                .onArea(onArea)
                .leftMouseButton(leftMouseButton)
                .middleMouseButton(middleMouseButton)
                .rightMouseButton(rightMouseButton)
                .mouseButton(mouseButton)
                .build());

    }

    @Override
    public String toString() {
        return "InteractiveProcessor{interactiveList.size=" + interactiveList.size() + '}';
    }
}
