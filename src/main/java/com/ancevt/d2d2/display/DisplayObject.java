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
package com.ancevt.d2d2.display;

import com.ancevt.d2d2.event.EventDispatcher;

public abstract class DisplayObject extends EventDispatcher implements IDisplayObject {

    private static int displayObjectIdCounter;

    private final int displayObjectId;
    private String name;
    private DisplayObjectContainer parent;

    private float alpha;

    private float x;
    private float y;

    private float scaleX;
    private float scaleY;

    private float rotation;

    private boolean visible;
    private int zOrderIndex;

    protected DisplayObject() {
        displayObjectId = displayObjectIdCounter++;
        visible = true;
        scaleX = scaleY = 1.0f;
        alpha = 1.0f;
        name = "_" + getClass().getSimpleName() + displayObjectId();
    }

    @Override
    public int displayObjectId() {
        return displayObjectId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String value) {
        this.name = value;
    }

    @Override
    public void setAbsoluteZOrderIndex(int zOrder) {
        this.zOrderIndex = zOrder;
    }

    @Override
    public int getAbsoluteZOrderIndex() {
        return zOrderIndex;
    }

    final void setParent(final DisplayObjectContainer container) {
        this.parent = container;
    }

    @Override
    public IDisplayObjectContainer getParent() {
        return parent;
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public void setXY(float x, float y) {
        setX(x);
        setY(y);
    }

    @Override
    public void setX(float value) {
        this.x = value;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setY(float value) {
        this.y = value;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        setScaleX(scaleX);
        setScaleY(scaleY);
    }

    @Override
    public void setScaleX(float value) {
        this.scaleX = value;
    }

    @Override
    public float getScaleX() {
        return scaleX;
    }

    @Override
    public void setScaleY(float value) {
        this.scaleY = value;
    }

    @Override
    public float getScaleY() {
        return scaleY;
    }

    @Override
    public boolean isOnScreen() {
        IDisplayObjectContainer currentParent = getParent();

        while (currentParent != null) {
            if (currentParent instanceof Stage) return true;
            currentParent = currentParent.getParent();
        }

        return false;
    }

    @Override
    public void setVisible(boolean value) {
        this.visible = value;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setRotation(float degrees) {
        rotation = degrees;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public void rotate(float toRotation) {
        rotation += toRotation;
    }

    @Override
    public void moveX(float value) {
        setX(getX() + value);
    }

    @Override
    public void moveY(float value) {
        setY(getY() + value);
    }

    @Override
    public void move(float toX, float toY) {
        moveX(toX);
        moveY(toY);
    }

    @Override
    public void toScaleX(float value) {
        setScaleX(getScaleX() * value);
    }

    @Override
    public void toScaleY(float value) {
        setScaleY(getScaleY() * value);
    }

    @Override
    public void toScale(float toX, float toY) {
        toScaleX(toX);
        toScaleY(toY);
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float getAbsoluteX() {
        return DisplayObjectAbsoluteComputer.getAbsoluteX(this);
    }

    @Override
    public float getAbsoluteY() {
        return DisplayObjectAbsoluteComputer.getAbsoluteY(this);
    }

    @Override
    public float getAbsoluteScaleX() {
        return DisplayObjectAbsoluteComputer.getAbsoluteScaleX(this);
    }

    @Override
    public float getAbsoluteScaleY() {
        return DisplayObjectAbsoluteComputer.getAbsoluteScaleY(this);
    }

    @Override
    public float getAbsoluteAlpha() {
        return DisplayObjectAbsoluteComputer.getAbsoluteAlpha(this);
    }

    @Override
    public float getAbsoluteRotation() {
        return DisplayObjectAbsoluteComputer.getAbsoluteRotation(this);
    }

    @Override
    public final void removeFromParent() {
        if (getParent() != null) {
            getParent().remove(this);
        }
    }

    @Override
    public void setAlpha(float value) {
        this.alpha = value;
    }

    @Override
    public float getAlpha() {
        return alpha;
    }

    @Override
    public void toAlpha(float value) {
        alpha *= value;
    }
}
