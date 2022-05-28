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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InteractiveProcessor {

    public static final InteractiveProcessor instance = new InteractiveProcessor();

    private final List<InteractiveButton> interactiveButtons;

    private boolean leftMouseButton;
    private boolean rightMouseButton;
    private boolean middleMouseButton;

    private InteractiveProcessor() {
        interactiveButtons = new CopyOnWriteArrayList<>();
    }

    public void registerInteractiveButton(final InteractiveButton interactiveButton) {
        if (!interactiveButtons.contains(interactiveButton))
            interactiveButtons.add(interactiveButton);
    }

    public final void unregisterTouchableComponent(final InteractiveButton interactiveButton) {
        interactiveButtons.remove(interactiveButton);
    }

    public final void clear() {
        while (!interactiveButtons.isEmpty()) {
            interactiveButtons.remove(0);
        }
    }

    public final void screenTouch(final int x, final int y, final int pointer, int mouseButton, final boolean down) {
        final Touch t = Touch.touch(pointer);
        if (t == null) return;

        t.setUp(x, y, down);

        switch (mouseButton) {
            case MouseButton.LEFT -> leftMouseButton = down;
            case MouseButton.RIGHT -> rightMouseButton = down;
            case MouseButton.MIDDLE -> middleMouseButton = down;
        }

        if (!down && t.getInteractiveButton() != null) {

            final InteractiveButton interactiveButton = t.getInteractiveButton();

            if (interactiveButton.isOnScreen()) {

                final float tcX = interactiveButton.getAbsoluteX();
                final float tcY = interactiveButton.getAbsoluteY();
                final float tcW = interactiveButton.getInteractiveArea().getWidth() * interactiveButton.getAbsoluteScaleX();
                final float tcH = interactiveButton.getInteractiveArea().getHeight() * interactiveButton.getAbsoluteScaleY();

                final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

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
                t.setInteractiveButton(null);
                return;
            }
        }

        for (final InteractiveButton interactiveButton : interactiveButtons) {
            final float tcX = interactiveButton.getAbsoluteX();
            final float tcY = interactiveButton.getAbsoluteY();
            final float tcW = interactiveButton.getInteractiveArea().getWidth() * interactiveButton.getAbsoluteScaleX();
            final float tcH = interactiveButton.getInteractiveArea().getHeight() * interactiveButton.getAbsoluteScaleY();

            if (interactiveButton.getName().equals("___")) {
                System.out.println("[\n" +
                        interactiveButton.isOnScreen() + ", " +
                        x + " " + tcX + " " + tcW + " " + down
                );
            }

            if (interactiveButton.isOnScreen() && x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH && down) {

                t.setInteractiveButton(interactiveButton);
                t.getInteractiveButton().setDragging(true);
                interactiveButton.dispatchEvent(InteractiveButtonEvent.builder()
                        .type(InteractiveButtonEvent.DOWN)
                        .x((int) (x - tcX))
                        .y((int) (y - tcY))
                        .mouseButton(mouseButton)
                        .onArea(true)
                        .leftMouseButton(leftMouseButton)
                        .rightMouseButton(rightMouseButton)
                        .middleMouseButton(middleMouseButton)
                        .build()
                );
            }
        }
    }

    public final void screenDrag(int i, final int x, final int y) {

        for (final InteractiveButton interactiveButton : interactiveButtons) {
            final float tcX = interactiveButton.getAbsoluteX();
            final float tcY = interactiveButton.getAbsoluteY();
            final float tcW = interactiveButton.getInteractiveArea().getWidth() * interactiveButton.getAbsoluteScaleX();
            final float tcH = interactiveButton.getInteractiveArea().getHeight() * interactiveButton.getAbsoluteScaleY();

            final boolean onArea = x >= tcX && x <= tcX + tcW && y >= tcY && y <= tcY + tcH;

            if (interactiveButton.isOnScreen() && interactiveButton.isDragging()) {
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
            if (interactiveButton.isOnScreen()) {
                if (!interactiveButton.isHovering() && onArea) {
                    interactiveButton.setHovering(true);
                    interactiveButton.dispatchEvent(InteractiveButtonEvent.builder()
                            .type(InteractiveButtonEvent.HOVER)
                            .x((int) (x - tcX))
                            .y((int) (y - tcY))
                            .onArea(true)
                            .leftMouseButton(leftMouseButton)
                            .rightMouseButton(rightMouseButton)
                            .middleMouseButton(middleMouseButton)
                            .build()
                    );
                } else if (interactiveButton.isHovering() && !onArea) {
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
    }

    public final @NotNull String getActiveList() {
        final StringBuilder sb = new StringBuilder();

        for (InteractiveButton current : interactiveButtons) {
            final String s = current.toString();
            sb.append(s).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "InteractiveProcessor{interactiveButtons.size=" + interactiveButtons.size() + '}';
    }

    private static class Touch {

        private static final int MAX_TOUCHES = 4;

        private static final Touch[] touches = new Touch[MAX_TOUCHES];

        public static Touch touch(final int pointer) {
            for (Touch value : touches) {
                if (pointer >= MAX_TOUCHES) return null;
                if (value != null && value.getPointer() == pointer) return value;
            }

            final Touch touch = new Touch(pointer);
            touches[pointer] = touch;

            return touch;
        }

        private final int pointer;
        private int x;
        private int y;
        private boolean down;
        private InteractiveButton component;

        private Touch(final int pointer) {
            this.pointer = pointer;
        }

        public final int getPointer() {
            return pointer;
        }

        public final void setUp(final int x, final int y, boolean down) {
            setLocation(x, y);
            setDown(down);
        }

        public final void setLocation(final int x, final int y) {
            setX(x);
            setY(y);
        }

        public final int getY() {
            return y;
        }

        public final void setY(int y) {
            this.y = y;
        }

        public final int getX() {
            return x;
        }

        public final void setX(int x) {
            this.x = x;
        }

        public boolean isDown() {
            return down;
        }

        public void setDown(boolean down) {
            this.down = down;
        }

        public final void setInteractiveButton(InteractiveButton interactiveButton) {
            this.component = interactiveButton;
        }

        public final InteractiveButton getInteractiveButton() {
            return component;
        }
    }

}
