/*
 *     Copyright 2015-2022 Ancevt (me@ancevt.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.touch;

import com.ancevt.d2d2.display.DisplayObjectContainer;

public class TouchButton extends DisplayObjectContainer {

    private static final float DEFAULT_WIDTH = 1;
    private static final float DEFAULT_HEIGHT = 1;

    private final TouchArea touchArea;
    private boolean enabled;
    private boolean dragging;
    private boolean hovering;

    public TouchButton(float width, float height) {
        touchArea = new TouchArea(0, 0, width, height);
        setName("touchButton" + hashCode());
    }

    public TouchButton(float width, float height, boolean enabled) {
        this(width, height);
        setEnabled(enabled);
    }

    public TouchButton() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public TouchButton(boolean enabled) {
        this();
        setEnabled(enabled);
    }

    public void setSize(float width, float height) {
        touchArea.setWidth(width);
        touchArea.setHeight(height);
    }

    public void setWidth(float width) {
        touchArea.setWidth(width);
    }

    public void setHeight(float height) {
        touchArea.setHeight(height);
    }

    public TouchArea getTouchArea() {
        return touchArea;
    }

    @Override
    public float getWidth() {
        return touchArea.getWidth();
    }

    @Override
    public float getHeight() {
        return touchArea.getHeight();
    }

    @Override
    public void setX(float value) {
        touchArea.setUp((float) value, touchArea.getY(), touchArea.getWidth(), touchArea.getHeight());
        super.setX(value);
    }

    @Override
    public void setY(float value) {
        touchArea.setUp(touchArea.getX(), (float) value, touchArea.getWidth(), touchArea.getHeight());
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

        TouchProcessor touchProcessor = TouchProcessor.instance;

        if (enabled)
            touchProcessor.registerTouchableComponent(this);
        else
            touchProcessor.unregisterTouchableComponent(this);
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
}
























