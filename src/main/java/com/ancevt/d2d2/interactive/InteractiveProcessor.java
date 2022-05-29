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

import com.ancevt.d2d2.event.InteractiveButtonEvent;
import com.ancevt.d2d2.input.MouseButton;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InteractiveProcessor {

    public static final InteractiveProcessor instance = new InteractiveProcessor();

    private final List<InteractiveButton> interactiveButtons;

    private boolean leftMouseButton;
    private boolean rightMouseButton;
    private boolean middleMouseButton;

    private InteractiveButton pressedInteractiveButton;
    private InteractiveButton hoveredInteractiveButton;


    private InteractiveProcessor() {
        interactiveButtons = new CopyOnWriteArrayList<>();
    }

    public void registerInteractiveButton(final InteractiveButton interactiveButton) {
        if (!interactiveButtons.contains(interactiveButton))
            interactiveButtons.add(interactiveButton);
    }

    public final void unregisterInteractiveButton(final InteractiveButton interactiveButton) {
        interactiveButtons.remove(interactiveButton);
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
                pressedInteractiveButton.dispatchEvent(InteractiveButtonEvent.builder()
                        .type(InteractiveButtonEvent.DOWN)
                        .x((int) (x - _tcX))
                        .y((int) (y - _tcY))
                        .mouseButton(mouseButton)
                        .onArea(true)
                        .leftMouseButton(leftMouseButton)
                        .rightMouseButton(rightMouseButton)
                        .middleMouseButton(middleMouseButton)
                        .build()
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
                            interactiveButton.dispatchEvent(InteractiveButtonEvent.builder()
                                    .type(InteractiveButtonEvent.UP)
                                    .x((int) (x - tcX))
                                    .y((int) (y - tcY))
                                    .mouseButton(mouseButton)
                                    .onArea(onArea)
                                    .leftMouseButton(leftMouseButton)
                                    .rightMouseButton(rightMouseButton)
                                    .middleMouseButton(middleMouseButton)
                                    .build()
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
                    interactiveButton.dispatchEvent(InteractiveButtonEvent.builder()
                            .type(InteractiveButtonEvent.DRAG)
                            .x((int) (x - tcX))
                            .y((int) (y - tcY))
                            .onArea(onArea)
                            .leftMouseButton(leftMouseButton)
                            .rightMouseButton(rightMouseButton)
                            .middleMouseButton(middleMouseButton)
                            .build()
                    );
                }

                if (interactiveButton.isHovering() && !onArea) {
                    interactiveButton.setHovering(false);
                    interactiveButton.dispatchEvent(InteractiveButtonEvent.builder()
                            .type(InteractiveButtonEvent.OUT)
                            .x((int) (x - tcX))
                            .y((int) (y - tcY))
                            .onArea(false)
                            .leftMouseButton(leftMouseButton)
                            .rightMouseButton(rightMouseButton)
                            .middleMouseButton(middleMouseButton)
                            .build()
                    );
                }

            }

        }
        if (upperButton != null) {
            if (!upperButton.isHovering()) {
                if (hoveredInteractiveButton != null) {
                    hoveredInteractiveButton.dispatchEvent(InteractiveButtonEvent.builder()
                            .type(InteractiveButtonEvent.OUT)
                            .x((int) (x - _tcX))
                            .y((int) (y - _tcY))
                            .onArea(false)
                            .leftMouseButton(leftMouseButton)
                            .rightMouseButton(rightMouseButton)
                            .middleMouseButton(middleMouseButton)
                            .build()
                    );
                    hoveredInteractiveButton.setHovering(false);
                }

                hoveredInteractiveButton = upperButton;

                upperButton.setHovering(true);
                upperButton.dispatchEvent(InteractiveButtonEvent.builder()
                        .type(InteractiveButtonEvent.HOVER)
                        .x((int) (x - _tcX))
                        .y((int) (y - _tcY))
                        .onArea(true)
                        .leftMouseButton(leftMouseButton)
                        .rightMouseButton(rightMouseButton)
                        .middleMouseButton(middleMouseButton)
                        .build()
                );
            }
        }
    }

    @Override
    public String toString() {
        return "InteractiveProcessor{interactiveButtons.size=" + interactiveButtons.size() + '}';
    }
}
