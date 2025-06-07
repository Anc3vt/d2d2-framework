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

package com.ancevt.d2d2.scene.interactive;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.event.StageEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.MouseButton;
import com.ancevt.d2d2.scene.shape.FreeShape;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private boolean alt;
    private boolean control;
    private boolean shift;

    private InteractiveManager() {
        interactiveList = new CopyOnWriteArrayList<>();
        focusedInteractiveIndex = -1;

        D2D2.getStage().addEventListener(InputEvent.KeyDown.class, e -> {
            Interactive focused = getFocused();
            if (focused != null) {
                dispatch(focused, InputEvent.KeyDown.create(
                        e.getKeyCode(),
                        e.getCharacter(),
                        e.isAlt(),
                        e.isControl(),
                        e.isShift()
                ));
            }
            alt = e.isAlt();
            control = e.isControl();
            shift = e.isShift();
        });

        D2D2.getStage().addEventListener(InputEvent.KeyRepeat.class, e -> {
            Interactive focused = getFocused();
            if (focused != null) {
                dispatch(focused, InputEvent.KeyRepeat.create(
                        e.getKeyCode(),
                        e.isAlt(),
                        e.isControl(),
                        e.isShift()
                ));
            }
        });

        D2D2.getStage().addEventListener(InputEvent.KeyUp.class, e -> {
            Interactive focused = getFocused();
            if (focused != null) {
                dispatch(focused, InputEvent.KeyUp.create(
                        e.getKeyCode(),
                        e.getCharacter(),
                        e.isAlt(),
                        e.isControl(),
                        e.isShift()
                ));
            }
            alt = e.isAlt();
            control = e.isControl();
            shift = e.isShift();
        });

        D2D2.getStage().addEventListener(InputEvent.MouseWheel.class, e -> {
            Interactive interactive = (hoveredInteractive != null && hoveredInteractive.isOnScreen() && hoveredInteractive.isHovering())
                    ? hoveredInteractive
                    : getFocused();

            if (interactive != null) {
                dispatch(interactive, InputEvent.MouseWheel.create(
                        e.getDelta(),
                        e.getX(),
                        e.getY(),
                        e.isAlt(),
                        e.isControl(),
                        e.isShift()
                ));
            }
        });

        D2D2.getStage().addEventListener(InputEvent.KeyType.class, e -> {
            Interactive focused = getFocused();
            if (focused != null) {
                dispatch(focused, InputEvent.KeyType.create(
                        e.getKeyCode(),
                        e.isAlt(),
                        e.isControl(),
                        e.isShift(),
                        e.getCharacter(),
                        e.getCodepoint(),
                        e.getKeyType()
                ));
            }
        });
    }


    public void registerInteractive(final Interactive interactive) {
        if (!interactiveList.contains(interactive)) {
            interactive.addEventListener(this, NodeEvent.RemoveFromScene.class, e -> {
                if (interactive.isFocused()) {
                    resetFocus();
                }
            });
            interactiveList.add(interactive);
        }
    }

    public final void unregisterInteractive(final Interactive interactive) {
        interactiveList.remove(interactive);
        interactive.removeEventListener(this, NodeEvent.RemoveFromScene.class);
    }

    public final void clear() {
        while (!interactiveList.isEmpty()) {
            interactiveList.remove(0);
        }
    }

    public final void screenTouch(final int x,
                                  final int y,
                                  final int pointer,
                                  int mouseButton,
                                  final boolean down,
                                  boolean shift,
                                  boolean control,
                                  boolean alt) {

        leftMouseButton = mouseButton == MouseButton.LEFT;
        rightMouseButton = mouseButton == MouseButton.RIGHT;
        middleMouseButton = mouseButton == MouseButton.MIDDLE;

        Interactive pressedInteractive = null;

        if (down) {
            int maxIndex = 0;
            float _tcX = 0.0f, _tcY = 0.0f;

            for (Interactive interactive : interactiveList) {
                if (interactive.getInteractiveFreeShape() != null) {
                    FreeShape freeShape = interactive.getInteractiveFreeShape();
                    int index = interactive.getGlobalZOrderIndex();
                    if (interactive.isOnScreen() &&
                            freeShape.isPointInsideFreeShape(
                                    (x - interactive.getGlobalX()) / interactive.getGlobalScaleX(),
                                    (y - interactive.getGlobalY()) / interactive.getGlobalScaleY()
                            ) && index > maxIndex) {
                        maxIndex = index;
                        _tcX = interactive.getGlobalX();
                        _tcY = interactive.getGlobalY();
                        pressedInteractive = interactive;
                    }

                } else {
                    final float tcX = interactive.getGlobalX();
                    final float tcY = interactive.getGlobalY();
                    final float tcW = interactive.getInteractiveArea().getWidth() * interactive.getGlobalScaleX();
                    final float tcH = interactive.getInteractiveArea().getHeight() * interactive.getGlobalScaleY();

                    if (interactive.isOnScreen() && x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH) {
                        int index = interactive.getGlobalZOrderIndex();
                        if (index >= maxIndex) {
                            pressedInteractive = interactive;
                            maxIndex = index;
                            _tcX = tcX;
                            _tcY = tcY;
                        }
                    }
                }
            }

            if (pressedInteractive != null) {
                setFocused(pressedInteractive, true);
                dispatch(pressedInteractive, InputEvent.MouseDown.create(
                        (int) (x - _tcX),
                        (int) (y - _tcY),
                        mouseButton,
                        leftMouseButton,
                        rightMouseButton,
                        middleMouseButton,
                        alt, control, shift
                ));

                pressedInteractive.setDragging(true);
            }

        } else {
            for (Interactive interactive : interactiveList) {
                if (interactive != null && interactive.isOnScreen()) {
                    final float tcX = interactive.getGlobalX();
                    final float tcY = interactive.getGlobalY();
                    final float tcW = interactive.getInteractiveArea().getWidth() * interactive.getGlobalScaleX();
                    final float tcH = interactive.getInteractiveArea().getHeight() * interactive.getGlobalScaleY();

                    boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

                    if (interactive.getInteractiveFreeShape() != null) {
                        onArea = interactive.getInteractiveFreeShape().isPointInsideFreeShape(
                                (x - interactive.getGlobalX()) / interactive.getGlobalScaleX(),
                                (y - interactive.getGlobalY()) / interactive.getGlobalScaleY()
                        );
                    }

                    if (interactive.isDragging()) {
                        dispatch(interactive, InputEvent.MouseUp.create(
                                (int) (x - tcX),
                                (int) (y - tcY),
                                mouseButton,
                                leftMouseButton,
                                rightMouseButton,
                                middleMouseButton,
                                onArea,
                                alt,
                                shift,
                                control
                        ));
                        interactive.setDragging(false);
                    }
                }
            }
        }
    }


    public final void screenMove(int pointer, final int x, final int y, boolean shift, boolean control, boolean alt) {
        float _tcX = 0.0f, _tcY = 0.0f;
        int maxIndex = 0;
        Interactive upperInteractive = null;

        for (final Interactive interactive : interactiveList) {
            final float tcX = interactive.getGlobalX();
            final float tcY = interactive.getGlobalY();
            final float tcW = interactive.getInteractiveArea().getWidth() * interactive.getGlobalScaleX();
            final float tcH = interactive.getInteractiveArea().getHeight() * interactive.getGlobalScaleY();

            final boolean onScreen = interactive.isOnScreen();
            boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

            if (interactive.getInteractiveFreeShape() != null) {
                onArea = interactive.getInteractiveFreeShape().isPointInsideFreeShape(
                        (x - interactive.getGlobalX()) / interactive.getGlobalScaleX(),
                        (y - interactive.getGlobalY()) / interactive.getGlobalScaleY()
                );
            }

            if (onScreen) {
                if (onArea) {
                    int index = interactive.getGlobalZOrderIndex();
                    if (index >= maxIndex) {
                        maxIndex = index;
                        _tcX = tcX;
                        _tcY = tcY;
                        upperInteractive = interactive;

                        dispatch(interactive, InputEvent.MouseMove.create(
                                (int) (x - tcX),
                                (int) (y - tcY),
                                true,
                                alt,
                                control,
                                shift
                        ));
                    }
                }

                if (interactive.isDragging()) {
                    dispatch(interactive, InputEvent.MouseDrag.create(
                            (int) (x - tcX),
                            (int) (y - tcY),
                            leftMouseButton ? MouseButton.LEFT :
                                    rightMouseButton ? MouseButton.RIGHT :
                                            middleMouseButton ? MouseButton.MIDDLE : -1,
                            leftMouseButton,
                            rightMouseButton,
                            middleMouseButton,
                            alt,
                            control,
                            shift
                    ));
                }

                if (interactive.isHovering() && !onArea) {
                    interactive.setHovering(false);
                    dispatch(interactive, InputEvent.MouseOut.create(
                            (int) (x - tcX),
                            (int) (y - tcY),
                            alt,
                            control,
                            shift
                    ));
                }
            }
        }

        if (upperInteractive != null && !upperInteractive.isHovering()) {
            if (hoveredInteractive != null) {
                dispatch(hoveredInteractive, InputEvent.MouseOut.create(
                        (int) (x - _tcX),
                        (int) (y - _tcY),
                        alt,
                        control,
                        shift
                ));
                hoveredInteractive.setHovering(false);
            }

            hoveredInteractive = upperInteractive;
            upperInteractive.setHovering(true);

            dispatch(upperInteractive, InputEvent.MouseHover.create(
                    (int) (x - _tcX),
                    (int) (y - _tcY),
                    alt,
                    control,
                    shift
            ));
        }
    }


    public void setFocused(Interactive interactive, boolean byMouseDown) {

        if (focusedInteractive == interactive) return;

        if (focusedInteractive != null) {
            dispatch(focusedInteractive, InputEvent.FocusOut.create());
        }

        int index = interactiveList.indexOf(interactive);
        if (index == -1) {
            focusedInteractiveIndex = -1;
            focusedInteractive = null;
        } else {
            focusedInteractive = interactive;
            focusedInteractiveIndex = index;

            // Параноик-версия: если хочешь сохранить byMouseDown, нужно завести FocusIn с полем
            dispatch(focusedInteractive, InputEvent.FocusIn.create(byMouseDown));
        }
    }


    public void setFocused(int index) {
        if (interactiveList.isEmpty()) return;

        focusedInteractiveIndex = index;

        if (focusedInteractiveIndex < 0)
            focusedInteractiveIndex = 0;
        else if (focusedInteractiveIndex >= interactiveList.size())
            focusedInteractiveIndex = interactiveList.size() - 1;

        Interactive newFocus = interactiveList.get(focusedInteractiveIndex);

        if (focusedInteractive == newFocus) return;

        if (focusedInteractive != null) {
            dispatch(focusedInteractive, InputEvent.FocusOut.create());
        }

        focusedInteractive = newFocus;

        dispatch(focusedInteractive, InputEvent.FocusIn.create(false));
    }


    public Interactive getFocused() {
        return focusedInteractive;
    }

    public void focusNext() {
        if (interactiveList.isEmpty() || getTabbingEnabledAndOnScreenAndVisibleCount() == 0) return;

        focusedInteractiveIndex++;
        if (focusedInteractiveIndex >= interactiveList.size()) focusedInteractiveIndex = 0;

        if (focusedInteractive != null) {
            dispatch(focusedInteractive, InputEvent.MouseOut.create(0, 0, alt, control, shift));
        }

        setFocused(focusedInteractiveIndex);

        if (!getFocused().isTabbingEnabled() || !getFocused().isOnScreen() || !getFocused().isVisible()) {
            focusNext();
        }
    }

    public void focusPrevious() {
        if (interactiveList.isEmpty() || getTabbingEnabledAndOnScreenAndVisibleCount() == 0) return;

        focusedInteractiveIndex--;
        if (focusedInteractiveIndex < 0) focusedInteractiveIndex = interactiveList.size() - 1;

        if (focusedInteractive != null) {
            dispatch(focusedInteractive, InputEvent.MouseOut.create(0, 0, alt, control, shift));
        }

        setFocused(focusedInteractiveIndex);

        if (!getFocused().isTabbingEnabled() || !getFocused().isOnScreen() || !getFocused().isVisible()) {
            focusPrevious();
        }
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
            D2D2.getStage().addEventListener(this, InputEvent.KeyDown.class, e -> {
                switch (e.getKeyCode()) {
                    case KeyCode.TAB -> {
                        if (e.isShift()) {
                            focusPrevious();
                            keyHoldTabDirection = -1;
                        } else {
                            focusNext();
                            keyHoldTabDirection = 1;
                        }
                        D2D2.getStage().addEventListener(this, StageEvent.PostFrame.class, exite -> {
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
                            dispatch(focusedInteractive, InputEvent.MouseDown.create(
                                    0, 0, MouseButton.LEFT,
                                    true, rightMouseButton, middleMouseButton, alt, control, shift
                            ));
                        }
                    }
                    case KeyCode.ESCAPE -> resetFocus();
                }
            });

            D2D2.getStage().addEventListener(this, InputEvent.KeyUp.class, e -> {
                switch (e.getKeyCode()) {
                    case KeyCode.TAB -> {
                        keyHoldTime = KEY_HOLD_TIME;
                        keyHoldTabDirection = 0;
                        D2D2.getStage().removeEventListener(this, StageEvent.PostFrame.class);
                    }
                    case KeyCode.ENTER -> {
                        if (focusedInteractive != null) {
                            dispatch(focusedInteractive, InputEvent.MouseUp.create(
                                    0, 0, MouseButton.LEFT,
                                    true, rightMouseButton, middleMouseButton, true, alt, control, shift
                            ));
                        }
                    }
                }
            });
        } else {
            D2D2.getStage().removeEventListener(this, InputEvent.KeyDown.class);
            D2D2.getStage().removeEventListener(this, InputEvent.KeyUp.class);
            D2D2.getStage().removeEventListener(this, StageEvent.PostFrame.class);
        }
    }


    public boolean isTabbingEnabled() {
        return tabbingEnabled;
    }

    public void resetFocus() {
        if (focusedInteractive != null) {
            dispatch(focusedInteractive, InputEvent.FocusOut.create());
            dispatch(focusedInteractive, InputEvent.MouseOut.create(0, 0, alt, control, shift));
        }

        focusedInteractiveIndex = -1;
        focusedInteractive = null;
    }


    private static void dispatch(Interactive eventDispatcher, InputEvent event) {
        if (!eventDispatcher.isInteractionEnabled() || !eventDispatcher.isGloballyVisible()) return;

        eventDispatcher.dispatchEvent(event);

        if (eventDispatcher.getParent() instanceof Interactive parent) {
            dispatch(parent, event);
        }
    }


    @Override
    public String toString() {
        return "InteractiveManager{interactiveList.size=" + interactiveList.size() + '}';
    }
}
