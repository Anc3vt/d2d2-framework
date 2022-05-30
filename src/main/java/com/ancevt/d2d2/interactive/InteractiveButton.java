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

import com.ancevt.d2d2.display.DisplayObjectContainer;

import java.util.List;

public class InteractiveButton extends DisplayObjectContainer {

    private static final float DEFAULT_WIDTH = 1;
    private static final float DEFAULT_HEIGHT = 1;

    private final InteractiveArea interactiveArea;
    private boolean enabled;
    private boolean dragging;
    private boolean hovering;
    private boolean tabbingEnabled;
    private List<InteractiveButton> tabbingGroup;

    public InteractiveButton(float width, float height) {
        interactiveArea = new InteractiveArea(0, 0, width, height);
        setName("_" + getClass().getSimpleName() + displayObjectId());
        tabbingGroup = InteractiveProcessor.INSTANCE.getDefaultTabbingGroup();
    }

    public InteractiveButton(float width, float height, boolean enabled) {
        this(width, height);
        setEnabled(enabled);
    }

    public InteractiveButton() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public InteractiveButton(boolean enabled) {
        this();
        setEnabled(enabled);
    }

    public void setDefaultTabbingGroup() {
        tabbingGroup = InteractiveProcessor.INSTANCE.getDefaultTabbingGroup();
    }

    public void setTabbingGroup(List<InteractiveButton> tabbingGroup) {
        if (tabbingGroup == null) {
            setDefaultTabbingGroup();
        } else {
            this.tabbingGroup = tabbingGroup;
        }
    }

    public List<InteractiveButton> getTabbingGroup() {
        return tabbingGroup;
    }

    public void setTabbingEnabled(boolean tabbingEnabled) {
        this.tabbingEnabled = tabbingEnabled;
    }

    public boolean isTabbingEnabled() {
        return tabbingEnabled;
    }

    public void setSize(float width, float height) {
        interactiveArea.setWidth(width);
        interactiveArea.setHeight(height);
    }

    public void setWidth(float width) {
        interactiveArea.setWidth(width);
    }

    public void setHeight(float height) {
        interactiveArea.setHeight(height);
    }

    public InteractiveArea getInteractiveArea() {
        return interactiveArea;
    }

    @Override
    public float getWidth() {
        return interactiveArea.getWidth();
    }

    @Override
    public float getHeight() {
        return interactiveArea.getHeight();
    }

    @Override
    public void setX(float value) {
        interactiveArea.setUp(value, interactiveArea.getY(), interactiveArea.getWidth(), interactiveArea.getHeight());
        super.setX(value);
    }

    @Override
    public void setY(float value) {
        interactiveArea.setUp(interactiveArea.getX(), value, interactiveArea.getWidth(), interactiveArea.getHeight());
        super.setY(value);
    }

    @Override
    public void setXY(float x, float y) {
        setX(x);
        setY(y);
        super.setXY(x, y);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;

        this.enabled = enabled;

        if (enabled)
            InteractiveProcessor.INSTANCE.registerInteractiveButton(this);
        else
            InteractiveProcessor.INSTANCE.unregisterInteractiveButton(this);
    }

    public boolean isDragging() {
        return dragging;
    }

    void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    public boolean isHovering() {
        return hovering;
    }

    public void focus() {
        InteractiveProcessor.INSTANCE.setFocused(this);
    }

    public boolean isFocused() {
        return InteractiveProcessor.INSTANCE.getFocused() == this;
    }

    public static void setGlobalTabbingEnabled(boolean tabbingEnabled) {
        InteractiveProcessor.INSTANCE.setTabbingEnabled(tabbingEnabled);
    }

    public static boolean isGlobalTabbingEnabled() {
        return InteractiveProcessor.INSTANCE.isTabbingEnabled();
    }

    public static void resetFocus() {
        InteractiveProcessor.INSTANCE.resetFocus();
    }
}
