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

package com.ancevt.d2d2.scene;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.engine.Engine;
import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.event.core.EventDispatcherImpl;
import com.ancevt.d2d2.scene.shader.ShaderProgram;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractNode extends EventDispatcherImpl implements Node {

    private static int displayObjectIdCounter;
    private final int nodeId;
    private String name;
    private BasicGroup parent;
    private float x;
    private float y;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float alpha = 1.0f;
    private float rotation;
    private boolean visible = true;
    private int zOrderIndex;
    private boolean integerPixelAlignmentEnabled = true;

    protected final Stage stage = D2D2.stage();
    protected final Engine engine = D2D2.engine();
    private boolean disposed;

    @Getter
    @Setter
    private ShaderProgram shaderProgram;

    protected AbstractNode() {
        nodeId = displayObjectIdCounter++;
        name = "_" + getClass().getSimpleName() + getNodeId();
    }

    @Override
    public int getNodeId() {
        return nodeId;
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
    public void setGlobalZOrderIndex(int zOrder) {
        this.zOrderIndex = zOrder;
    }

    @Override
    public int getGlobalZOrderIndex() {
        return zOrderIndex;
    }

    final void setParent(final BasicGroup container) {
        this.parent = container;
    }

    @Override
    public Group getParent() {
        return parent;
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public void setPosition(float x, float y) {
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
        Group currentParent = getParent();

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
    public void scaleX(float value) {
        setScaleX(getScaleX() * value);
    }

    @Override
    public void scaleY(float value) {
        setScaleY(getScaleY() * value);
    }

    @Override
    public void scale(float toX, float toY) {
        scaleX(toX);
        scaleY(toY);
    }

    @Override
    public float getWidth() {
        return 0f;
    }

    @Override
    public float getHeight() {
        return 0f;
    }

    @Override
    public float getScaledWidth() {
        return getWidth() * getScaleX();
    }

    @Override
    public float getScaledHeight() {
        return getHeight() * getScaleY();
    }

    @Override
    public float getGlobalX() {
        return GlobalMetrics.getGlobalX(this);
    }

    @Override
    public float getGlobalY() {
        return GlobalMetrics.getGlobalY(this);
    }

    @Override
    public float getGlobalScaleX() {
        return GlobalMetrics.getGlobalScaleX(this);
    }

    @Override
    public float getGlobalScaleY() {
        return GlobalMetrics.getGlobalScaleY(this);
    }

    @Override
    public float getGlobalAlpha() {
        return GlobalMetrics.getGlobalAlpha(this);
    }

    @Override
    public float getGlobalRotation() {
        return GlobalMetrics.getGlobalRotation(this);
    }

    @Override
    public boolean isGloballyVisible() {
        return GlobalMetrics.isGloballyVisible(this);
    }

    @Override
    public final void removeFromParent() {
        if (hasParent()) {
            getParent().removeChild(this);
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

    @Override
    public void setIntegerPixelAlignmentEnabled(boolean value) {
        this.integerPixelAlignmentEnabled = value;
    }

    @Override
    public boolean isIntegerPixelAlignmentEnabled() {
        return integerPixelAlignmentEnabled;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "displayObjectId=" + nodeId + ", name='" + name + "'}";
    }

    @Override
    public void dispose() {
        removeFromParent();
        stage.removeAllEventListenersByKey(this);
        dispatchEvent(NodeEvent.Dispose.create());
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }
}
