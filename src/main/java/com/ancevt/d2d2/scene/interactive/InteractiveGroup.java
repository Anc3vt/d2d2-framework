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

import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.exception.InteractiveException;
import com.ancevt.d2d2.scene.BasicGroup;
import com.ancevt.d2d2.scene.Resizable;
import com.ancevt.d2d2.scene.shape.FreeShape;

public class InteractiveGroup extends BasicGroup implements Interactive, Resizable {

    private static final float DEFAULT_WIDTH = 1f;
    private static final float DEFAULT_HEIGHT = 1f;
    protected final InteractiveArea interactiveArea;
    private boolean enabled;
    private boolean dragging;
    private boolean hovering;
    private boolean tabbingEnabled;
    private boolean disposed;
    private boolean pushEventsUp;

    private FreeShape interactiveFreeShape;

    public InteractiveGroup() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public InteractiveGroup(float width, float height) {
        interactiveArea = new InteractiveArea(0, 0, width, height);
        setName("_" + getClass().getSimpleName() + getNodeId());
        enabled = true;
        pushEventsUp = true;
        InteractiveManager.getInstance().registerInteractive(this);
    }

    @Override
    public void setInteractiveFreeShape(FreeShape interactiveFreeShape) {
        this.interactiveFreeShape = interactiveFreeShape;
    }

    @Override
    public FreeShape getInteractiveFreeShape() {
        return interactiveFreeShape;
    }

    @Override
    public void setPushEventsUp(boolean pushEventUp) {
        this.pushEventsUp = pushEventUp;
    }

    @Override
    public boolean isPushEventsUp() {
        return pushEventsUp;
    }

    @Override
    public void setTabbingEnabled(boolean tabbingEnabled) {
        this.tabbingEnabled = tabbingEnabled;
    }

    @Override
    public boolean isTabbingEnabled() {
        return tabbingEnabled;
    }

    private void dispatchResizeEvent() {
        dispatchEvent(CommonEvent.Resize.create(getWidth(), getHeight()));
    }

    public void setSize(float width, float height) {
        setWidth(width);
        setHeight(height);
    }

    public void setWidth(float width) {
        interactiveArea.setWidth(width);
        dispatchResizeEvent();
    }

    public void setHeight(float height) {
        interactiveArea.setHeight(height);
        dispatchResizeEvent();
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
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
        super.setPosition(x, y);
    }

    @Override
    public boolean isInteractionEnabled() {
        return enabled;
    }

    @Override
    public void setInteractionEnabled(boolean enabled) {
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
        removeAllEventListeners();
        removeFromParent();
        addEventListener(NodeEvent.Add.class, e -> {
            throw new InteractiveException("Unable to add disposed interactive display object %s".formatted(this.toString()));
        });
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }


}
