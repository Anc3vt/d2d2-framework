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
import com.ancevt.d2d2.event.FocusEvent;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.InteractiveButtonEvent;
import com.ancevt.d2d2.exception.InteractiveButtonException;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.MouseButton;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ancevt.d2d2.D2D2.stage;
import static com.ancevt.d2d2.event.InteractiveButtonEvent.DOWN;
import static com.ancevt.d2d2.event.InteractiveButtonEvent.DRAG;
import static com.ancevt.d2d2.event.InteractiveButtonEvent.HOVER;
import static com.ancevt.d2d2.event.InteractiveButtonEvent.OUT;
import static com.ancevt.d2d2.event.InteractiveButtonEvent.UP;

public class InteractiveProcessor {

    public static final InteractiveProcessor INSTANCE = new InteractiveProcessor();

    private final List<InteractiveButton> interactiveButtons;
    private final List<InteractiveButton> defaultTabbingGroup;
    private static final int KEY_HOLD_TIME = 30;

    private boolean leftMouseButton;
    private boolean rightMouseButton;
    private boolean middleMouseButton;

    private InteractiveButton hoveredInteractiveButton;

    private InteractiveButton focusedInteractiveButton;
    private int focusedInteractiveButtonIndex;
    private boolean tabbingEnabled;
    private int keyHoldTime = KEY_HOLD_TIME;
    private int keyHoldTabDirection;

    private InteractiveProcessor() {
        interactiveButtons = new CopyOnWriteArrayList<>();
        defaultTabbingGroup = new CopyOnWriteArrayList<>();
        focusedInteractiveButtonIndex = -1;
    }

    public void registerInteractiveButton(final InteractiveButton interactiveButton) {
        if (!interactiveButtons.contains(interactiveButton))
            interactiveButtons.add(interactiveButton);

        defaultTabbingGroup.remove(interactiveButton);
        defaultTabbingGroup.add(interactiveButton);
    }

    public final void unregisterInteractiveButton(final InteractiveButton interactiveButton) {
        interactiveButtons.remove(interactiveButton);
        defaultTabbingGroup.remove(interactiveButton);
    }

    public List<InteractiveButton> getDefaultTabbingGroup() {
        return defaultTabbingGroup;
    }

    public final void clear() {
        while (!interactiveButtons.isEmpty()) {
            interactiveButtons.remove(0);
        }
    }

    public final void screenTouch(final int x, final int y, final int pointer, int mouseButton, final boolean down) {
        switch (mouseButton) {
            case MouseButton.LEFT -> leftMouseButton = down;
            case MouseButton.RIGHT -> rightMouseButton = down;
            case MouseButton.MIDDLE -> middleMouseButton = down;
        }

        InteractiveButton pressedInteractiveButton = null;

        if (down) {

            int maxIndex = 0;
            float _tcX = 0.0f, _tcY = 0.0f;

            for (InteractiveButton interactiveButton : interactiveButtons) {
                final float tcX = interactiveButton.getAbsoluteX();
                final float tcY = interactiveButton.getAbsoluteY();
                final float tcW = interactiveButton.getInteractiveArea().getWidth() * interactiveButton.getAbsoluteScaleX();
                final float tcH = interactiveButton.getInteractiveArea().getHeight() * interactiveButton.getAbsoluteScaleY();

                if (interactiveButton.isOnScreen() && x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH) {
                    int index = interactiveButton.getAbsoluteZOrderIndex();
                    if (index >= maxIndex) {
                        pressedInteractiveButton = interactiveButton;
                        maxIndex = index;
                        _tcX = tcX;
                        _tcY = tcY;
                    }
                }
            }

            if (pressedInteractiveButton != null) {

                setFocused(pressedInteractiveButton);

                dispatch(pressedInteractiveButton,
                        DOWN,
                        (int) (x - _tcX),
                        (int) (y - _tcY),
                        true,
                        leftMouseButton,
                        rightMouseButton,
                        middleMouseButton,
                        mouseButton
                );

                pressedInteractiveButton.setDragging(true);
            }

        } else {
            for (InteractiveButton interactiveButton : interactiveButtons) {
                if (interactiveButton != null) {

                    if (interactiveButton.isOnScreen()) {
                        final float tcX = interactiveButton.getAbsoluteX();
                        final float tcY = interactiveButton.getAbsoluteY();
                        final float tcW = interactiveButton.getInteractiveArea().getWidth() * interactiveButton.getAbsoluteScaleX();
                        final float tcH = interactiveButton.getInteractiveArea().getHeight() * interactiveButton.getAbsoluteScaleY();

                        final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

                        if (interactiveButton.isDragging()) {
                            dispatch(interactiveButton,
                                    UP,
                                    (int) (x - tcX),
                                    (int) (y - tcY),
                                    onArea,
                                    leftMouseButton,
                                    rightMouseButton,
                                    middleMouseButton,
                                    mouseButton
                            );

                            interactiveButton.setDragging(false);
                        }
                    }
                }
            }
        }

    }

    public final void screenDrag(int pointer, final int x, final int y) {

        float _tcX = 0.0f, _tcY = 0.0f;
        int maxIndex = 0;
        InteractiveButton upperButton = null;

        for (final InteractiveButton interactiveButton : interactiveButtons) {
            final float tcX = interactiveButton.getAbsoluteX();
            final float tcY = interactiveButton.getAbsoluteY();
            final float tcW = interactiveButton.getInteractiveArea().getWidth() * interactiveButton.getAbsoluteScaleX();
            final float tcH = interactiveButton.getInteractiveArea().getHeight() * interactiveButton.getAbsoluteScaleY();

            final boolean onScreen = interactiveButton.isOnScreen();
            final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

            if (onScreen) {


                if (onArea) {
                    int index = interactiveButton.getAbsoluteZOrderIndex();
                    if (index >= maxIndex) {
                        maxIndex = index;
                        _tcX = tcX;
                        _tcY = tcY;
                        upperButton = interactiveButton;
                    }
                }

                if (interactiveButton.isDragging()) {
                    dispatch(interactiveButton,
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

                if (interactiveButton.isHovering() && !onArea) {
                    interactiveButton.setHovering(false);
                    dispatch(interactiveButton,
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
        if (upperButton != null) {
            if (!upperButton.isHovering()) {
                if (hoveredInteractiveButton != null) {
                    dispatch(hoveredInteractiveButton,
                            OUT,
                            (int) (x - _tcX),
                            (int) (y - _tcY),
                            false,
                            leftMouseButton,
                            rightMouseButton,
                            middleMouseButton,
                            0
                    );
                    hoveredInteractiveButton.setHovering(false);
                }

                hoveredInteractiveButton = upperButton;

                upperButton.setHovering(true);
                dispatch(upperButton,
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

    public void setFocused(InteractiveButton interactiveButton) {
        if (focusedInteractiveButton == interactiveButton) return;

        List<InteractiveButton> tabbingGroup = interactiveButton.getTabbingGroup();

        if (focusedInteractiveButton != null) {
            focusedInteractiveButton.dispatchEvent(FocusEvent.builder()
                    .type(FocusEvent.FOCUS_OUT)
                    .build());
        }

        int index = tabbingGroup.indexOf(interactiveButton);
        if (index == -1)
            throw new InteractiveButtonException("Unable to focus unregistered InteractiveButton " + interactiveButton);

        focusedInteractiveButton = interactiveButton;
        focusedInteractiveButtonIndex = index;

        focusedInteractiveButton.dispatchEvent(FocusEvent.builder()
                .type(FocusEvent.FOCUS_IN)
                .build());
    }

    public void setFocused(int index) {
        if (interactiveButtons.size() == 0) return;

        focusedInteractiveButtonIndex = index;

        if (focusedInteractiveButtonIndex < 0)
            focusedInteractiveButtonIndex = 0;
        else if (focusedInteractiveButtonIndex >= interactiveButtons.size())
            focusedInteractiveButtonIndex = interactiveButtons.size() - 1;

        if (focusedInteractiveButton == interactiveButtons.get(focusedInteractiveButtonIndex)) return;

        if (focusedInteractiveButton != null) {
            focusedInteractiveButton.dispatchEvent(FocusEvent.builder()
                    .type(FocusEvent.FOCUS_OUT)
                    .build());
        }

        focusedInteractiveButton = interactiveButtons.get(focusedInteractiveButtonIndex);

        focusedInteractiveButton.dispatchEvent(FocusEvent.builder()
                .type(FocusEvent.FOCUS_IN)
                .build());
    }

    public InteractiveButton getFocused() {
        return focusedInteractiveButton;
    }

    public void focusNext() {
        if (interactiveButtons.size() == 0 || getTabbingEnabledCount() <= 1) return;
        focusedInteractiveButtonIndex++;
        if (focusedInteractiveButtonIndex >= interactiveButtons.size()) focusedInteractiveButtonIndex = 0;

        if (focusedInteractiveButton != null) {
            dispatch(focusedInteractiveButton, OUT, 0, 0, false, false, false, false, 0);
        }

        setFocused(focusedInteractiveButtonIndex);

        if(!getFocused().isTabbingEnabled()) focusNext();
    }

    public void focusPrevious() {
        if (interactiveButtons.size() == 0 || getTabbingEnabledCount() <= 1) return;
        focusedInteractiveButtonIndex--;
        if (focusedInteractiveButtonIndex < 0) focusedInteractiveButtonIndex = interactiveButtons.size() - 1;

        if (focusedInteractiveButton != null) {
            dispatch(focusedInteractiveButton, OUT, 0, 0, false, false, false, false, 0);
        }

        setFocused(focusedInteractiveButtonIndex);

        if(!getFocused().isTabbingEnabled()) focusPrevious();
    }

    public int getTabbingEnabledCount() {
        int count = 0;
        for(InteractiveButton interactiveButton : interactiveButtons) {
            if(interactiveButton.isTabbingEnabled()) count++;
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
                        if (focusedInteractiveButton != null) {
                            dispatch(focusedInteractiveButton, DOWN, 0, 0, true, false, false, false, 0);
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
                        if (focusedInteractiveButton != null) {
                            dispatch(focusedInteractiveButton, UP, 0, 0, true, false, false, false, 0);
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

    public void resetFocus() {
        if (focusedInteractiveButton != null) {
            focusedInteractiveButton.dispatchEvent(FocusEvent.builder()
                    .type(FocusEvent.FOCUS_OUT)
                    .build());

            dispatch(focusedInteractiveButton, OUT, 0, 0, false, false, false, false, 0);
        }

        focusedInteractiveButtonIndex = -1;
        focusedInteractiveButton = null;
    }

    public boolean isTabbingEnabled() {
        return tabbingEnabled;
    }

    private static void dispatch(InteractiveButton interactiveButton,
                                 String type,
                                 int x,
                                 int y,
                                 boolean onArea,
                                 boolean leftMouseButton,
                                 boolean rightMouseButton,
                                 boolean middleMouseButton,
                                 int mouseButton) {

        interactiveButton.dispatchEvent(InteractiveButtonEvent.builder()
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
        return "InteractiveProcessor{interactiveButtons.size=" + interactiveButtons.size() + '}';
    }
}
