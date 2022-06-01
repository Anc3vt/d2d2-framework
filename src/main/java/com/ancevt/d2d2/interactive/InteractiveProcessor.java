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
import com.ancevt.d2d2.exception.InteractiveContainerException;
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

public class InteractiveProcessor {

    private static InteractiveProcessor instance;

    public static InteractiveProcessor getInstance() {
        return instance == null ? instance = new InteractiveProcessor() : instance;
    }

    private final List<InteractiveContainer> interactiveContainers;
    private static final int KEY_HOLD_TIME = 30;

    private boolean leftMouseButton;
    private boolean rightMouseButton;
    private boolean middleMouseButton;

    private InteractiveContainer hoveredInteractiveContainer;

    private InteractiveContainer focusedInteractiveContainer;
    private int focusedInteractiveContainerIndex;
    private boolean tabbingEnabled;
    private int keyHoldTime = KEY_HOLD_TIME;
    private int keyHoldTabDirection;

    private InteractiveProcessor() {
        interactiveContainers = new CopyOnWriteArrayList<>();
        focusedInteractiveContainerIndex = -1;

        stage().addEventListener(InputEvent.KEY_DOWN, event -> {
            var e = (InputEvent) event;
            InteractiveContainer focused = getFocused();
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
            InteractiveContainer focused = getFocused();
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
            InteractiveContainer focused = getFocused();
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

    public void registerInteractiveContainer(final InteractiveContainer interactiveContainer) {
        if (!interactiveContainers.contains(interactiveContainer)) {
            interactiveContainer.addEventListener(this, Event.REMOVE_FROM_STAGE, event -> {
                if (interactiveContainer.isFocused()) {
                    resetFocus();
                }
            });
            interactiveContainers.add(interactiveContainer);
        }
    }

    public final void unregisterInteractiveContainer(final InteractiveContainer interactiveContainer) {
        interactiveContainers.remove(interactiveContainer);
        interactiveContainer.removeEventListener(this, Event.REMOVE_FROM_STAGE);
    }

    public final void clear() {
        while (!interactiveContainers.isEmpty()) {
            interactiveContainers.remove(0);
        }
    }

    public final void screenTouch(final int x, final int y, final int pointer, int mouseButton, final boolean down) {
        switch (mouseButton) {
            case MouseButton.LEFT -> leftMouseButton = down;
            case MouseButton.RIGHT -> rightMouseButton = down;
            case MouseButton.MIDDLE -> middleMouseButton = down;
        }

        InteractiveContainer pressedInteractiveContainer = null;

        if (down) {

            int maxIndex = 0;
            float _tcX = 0.0f, _tcY = 0.0f;

            for (InteractiveContainer interactiveContainer : interactiveContainers) {
                final float tcX = interactiveContainer.getAbsoluteX();
                final float tcY = interactiveContainer.getAbsoluteY();
                final float tcW = interactiveContainer.getInteractiveArea().getWidth() * interactiveContainer.getAbsoluteScaleX();
                final float tcH = interactiveContainer.getInteractiveArea().getHeight() * interactiveContainer.getAbsoluteScaleY();

                if (interactiveContainer.isOnScreen() && x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH) {
                    int index = interactiveContainer.getAbsoluteZOrderIndex();
                    if (index >= maxIndex) {
                        pressedInteractiveContainer = interactiveContainer;
                        maxIndex = index;
                        _tcX = tcX;
                        _tcY = tcY;
                    }
                }
            }

            if (pressedInteractiveContainer != null) {
                setFocused(pressedInteractiveContainer);

                dispatch(pressedInteractiveContainer,
                        DOWN,
                        (int) (x - _tcX),
                        (int) (y - _tcY),
                        true,
                        leftMouseButton,
                        rightMouseButton,
                        middleMouseButton,
                        mouseButton
                );

                pressedInteractiveContainer.setDragging(true);
            }

        } else {
            for (InteractiveContainer interactiveContainer : interactiveContainers) {
                if (interactiveContainer != null) {

                    if (interactiveContainer.isOnScreen()) {
                        final float tcX = interactiveContainer.getAbsoluteX();
                        final float tcY = interactiveContainer.getAbsoluteY();
                        final float tcW = interactiveContainer.getInteractiveArea().getWidth() * interactiveContainer.getAbsoluteScaleX();
                        final float tcH = interactiveContainer.getInteractiveArea().getHeight() * interactiveContainer.getAbsoluteScaleY();

                        final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

                        if (interactiveContainer.isDragging()) {
                            dispatch(interactiveContainer,
                                    UP,
                                    (int) (x - tcX),
                                    (int) (y - tcY),
                                    onArea,
                                    leftMouseButton,
                                    rightMouseButton,
                                    middleMouseButton,
                                    mouseButton
                            );

                            interactiveContainer.setDragging(false);
                        }
                    }
                }
            }
        }
    }

    public final void screenDrag(int pointer, final int x, final int y) {
        float _tcX = 0.0f, _tcY = 0.0f;
        int maxIndex = 0;
        InteractiveContainer upperIntaractiveContainer = null;

        for (final InteractiveContainer interactiveContainer : interactiveContainers) {
            final float tcX = interactiveContainer.getAbsoluteX();
            final float tcY = interactiveContainer.getAbsoluteY();
            final float tcW = interactiveContainer.getInteractiveArea().getWidth() * interactiveContainer.getAbsoluteScaleX();
            final float tcH = interactiveContainer.getInteractiveArea().getHeight() * interactiveContainer.getAbsoluteScaleY();

            final boolean onScreen = interactiveContainer.isOnScreen();
            final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

            if (onScreen) {


                if (onArea) {
                    int index = interactiveContainer.getAbsoluteZOrderIndex();
                    if (index >= maxIndex) {
                        maxIndex = index;
                        _tcX = tcX;
                        _tcY = tcY;
                        upperIntaractiveContainer = interactiveContainer;
                    }
                }

                if (interactiveContainer.isDragging()) {
                    dispatch(interactiveContainer,
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

                if (interactiveContainer.isHovering() && !onArea) {
                    interactiveContainer.setHovering(false);
                    dispatch(interactiveContainer,
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
        if (upperIntaractiveContainer != null) {
            if (!upperIntaractiveContainer.isHovering()) {
                if (hoveredInteractiveContainer != null) {
                    dispatch(hoveredInteractiveContainer,
                            OUT,
                            (int) (x - _tcX),
                            (int) (y - _tcY),
                            false,
                            leftMouseButton,
                            rightMouseButton,
                            middleMouseButton,
                            0
                    );
                    hoveredInteractiveContainer.setHovering(false);
                }

                hoveredInteractiveContainer = upperIntaractiveContainer;

                upperIntaractiveContainer.setHovering(true);
                dispatch(upperIntaractiveContainer,
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

    public void setFocused(InteractiveContainer interactiveContainer) {
        if (focusedInteractiveContainer == interactiveContainer) return;


        if (focusedInteractiveContainer != null) {
            focusedInteractiveContainer.dispatchEvent(InteractiveEvent.builder()
                    .type(InteractiveEvent.FOCUS_OUT)
                    .build());
        }

        int index = interactiveContainers.indexOf(interactiveContainer);
        if (index == -1)
            throw new InteractiveContainerException("Unable to focus unregistered InteractiveContainer " + interactiveContainer);

        focusedInteractiveContainer = interactiveContainer;
        focusedInteractiveContainerIndex = index;

        focusedInteractiveContainer.dispatchEvent(InteractiveEvent.builder()
                .type(InteractiveEvent.FOCUS_IN)
                .build());
    }

    public void setFocused(int index) {
        if (interactiveContainers.size() == 0) return;

        focusedInteractiveContainerIndex = index;

        if (focusedInteractiveContainerIndex < 0)
            focusedInteractiveContainerIndex = 0;
        else if (focusedInteractiveContainerIndex >= interactiveContainers.size())
            focusedInteractiveContainerIndex = interactiveContainers.size() - 1;

        if (focusedInteractiveContainer == interactiveContainers.get(focusedInteractiveContainerIndex)) return;

        if (focusedInteractiveContainer != null) {
            focusedInteractiveContainer.dispatchEvent(InteractiveEvent.builder()
                    .type(InteractiveEvent.FOCUS_OUT)
                    .build());
        }

        focusedInteractiveContainer = interactiveContainers.get(focusedInteractiveContainerIndex);

        focusedInteractiveContainer.dispatchEvent(InteractiveEvent.builder()
                .type(InteractiveEvent.FOCUS_IN)
                .build());
    }

    public InteractiveContainer getFocused() {
        return focusedInteractiveContainer;
    }

    public void focusNext() {
        System.out.println("focusNext " + interactiveContainers.size() + " " + getTabbingEnabledAndOnScreenAndVisibleCount());

        if (interactiveContainers.size() == 0 || getTabbingEnabledAndOnScreenAndVisibleCount() <= 1) return;
        focusedInteractiveContainerIndex++;
        if (focusedInteractiveContainerIndex >= interactiveContainers.size()) focusedInteractiveContainerIndex = 0;

        if (focusedInteractiveContainer != null) {
            dispatch(focusedInteractiveContainer, OUT, 0, 0, false, false, false, false, 0);
        }

        setFocused(focusedInteractiveContainerIndex);

        if (!getFocused().isTabbingEnabled() || !getFocused().isOnScreen() || !getFocused().isVisible()) focusNext();
    }

    public void focusPrevious() {
        if (interactiveContainers.size() == 0 || getTabbingEnabledAndOnScreenAndVisibleCount() <= 1) return;
        focusedInteractiveContainerIndex--;
        if (focusedInteractiveContainerIndex < 0) focusedInteractiveContainerIndex = interactiveContainers.size() - 1;

        if (focusedInteractiveContainer != null) {
            dispatch(focusedInteractiveContainer, OUT, 0, 0, false, false, false, false, 0);
        }

        setFocused(focusedInteractiveContainerIndex);

        if (!getFocused().isTabbingEnabled() || !getFocused().isOnScreen() || !getFocused().isVisible())
            focusPrevious();
    }

    private int getTabbingEnabledAndOnScreenAndVisibleCount() {
        int count = 0;
        for (InteractiveContainer interactiveContainer : interactiveContainers) {
            if (interactiveContainer.isTabbingEnabled() && interactiveContainer.isOnScreen() && interactiveContainer.isVisible())
                count++;
        }
        return count;
    }

    void setTabbingEnabled(boolean tabbingEnabled) {
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
                        if (focusedInteractiveContainer != null) {
                            dispatch(focusedInteractiveContainer, DOWN, 0, 0, true, false, false, false, 0);
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
                        if (focusedInteractiveContainer != null) {
                            dispatch(focusedInteractiveContainer, UP, 0, 0, true, false, false, false, 0);
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

    boolean isTabbingEnabled() {
        return tabbingEnabled;
    }

    public void resetFocus() {
        if (focusedInteractiveContainer != null) {
            focusedInteractiveContainer.dispatchEvent(InteractiveEvent.builder()
                    .type(InteractiveEvent.FOCUS_OUT)
                    .build());

            dispatch(focusedInteractiveContainer, OUT, 0, 0, false, false, false, false, 0);
        }

        focusedInteractiveContainerIndex = -1;
        focusedInteractiveContainer = null;
    }

    private static void dispatch(InteractiveContainer interactiveContainer,
                                 String type,
                                 int x,
                                 int y,
                                 boolean onArea,
                                 boolean leftMouseButton,
                                 boolean rightMouseButton,
                                 boolean middleMouseButton,
                                 int mouseButton) {

        interactiveContainer.dispatchEvent(InteractiveEvent.builder()
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
        return "InteractiveProcessor{interactiveContainers.size=" + interactiveContainers.size() + '}';
    }
}
