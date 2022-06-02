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

import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.exception.InteractiveException;

public class InteractiveContainer extends Container implements Interactive {

    private static final float DEFAULT_WIDTH = 1;
    private static final float DEFAULT_HEIGHT = 1;

    private final InteractiveArea interactiveArea;
    private boolean enabled;
    private boolean dragging;
    private boolean hovering;
    private boolean tabbingEnabled;
    private boolean disposed;

    public InteractiveContainer(float width, float height) {
        interactiveArea = new InteractiveArea(0, 0, width, height);
        setName("_" + getClass().getSimpleName() + displayObjectId());
        enabled = true;
        InteractiveManager.getInstance().registerInteractive(this);
    }

    public InteractiveContainer() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public void setTabbingEnabled(boolean tabbingEnabled) {
        this.tabbingEnabled = tabbingEnabled;
    }

    @Override
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

    @Override
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

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;

        this.enabled = enabled;

        if (enabled)
            InteractiveManager.getInstance().registerInteractive(this);
        else
            InteractiveManager.getInstance().unregisterInteractive(this);
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    @Override
    public boolean isHovering() {
        return hovering;
    }

    @Override
    public void focus() {
        InteractiveManager.getInstance().setFocused(this, false);
    }

    @Override
    public boolean isFocused() {
        return InteractiveManager.getInstance().getFocused() == this;
    }

    @Override
    public void dispose() {
        this.disposed = true;
        InteractiveManager.getInstance().unregisterInteractive(this);
        removeFromParent();
        removeAllEventListeners();
        addEventListener(Event.ADD, event -> {
            throw new InteractiveException("Unable to add disposed interactive display object %s".formatted(this.toString()));
        });
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }
}
