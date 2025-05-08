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

import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.event.core.EventDispatcher;
import com.ancevt.d2d2.event.core.EventHandleRegistration;
import com.ancevt.d2d2.event.core.EventListener;
import com.ancevt.d2d2.scene.shader.ShaderProgram;

public interface Node extends EventDispatcher {

    void setShaderProgram(ShaderProgram shaderProgram);

    ShaderProgram getShaderProgram();

    default void centerX() {
        if (hasParent()) {
            Group parent = getParent();
            setX((parent.getWidth() - getWidth()) / 2);
        } else {
            setX(-getWidth() / 2);
        }
    }

    default void centerY() {
        if (hasParent()) {
            Group parent = getParent();
            setY((parent.getHeight() - getHeight()) / 2);
        } else {
            setX(-getWidth() / 2);
        }
    }

    default void center() {
        centerX();
        centerY();
    }

    int getDisplayObjectId();

    String getName();

    void setName(String value);

    Group getParent();

    boolean hasParent();

    void setAlpha(float value);

    float getAlpha();

    void toAlpha(float value);

    void setXY(float x, float y);

    void setX(float value);

    void setY(float value);

    default void setXYAs(Node node) {
        setXY(node.getX(), node.getY());
    }

    float getX();

    float getY();

    void setScale(float scaleX, float scaleY);

    default void setScale(float xy) {
        setScale(xy, xy);
    }

    void setScaleX(float value);

    float getScaleX();

    void setScaleY(float value);

    float getScaleY();

    boolean isOnScreen();

    void setVisible(boolean value);

    boolean isVisible();

    void setRotation(float degrees);

    float getRotation();

    void rotate(float toRotation);

    void moveX(float value);

    void moveY(float value);

    void move(float toX, float toY);

    void scaleX(float value);

    void scaleY(float value);

    void scale(float toX, float toY);

    default void scale(float xy) {
        scale(xy, xy);
    }

    float getWidth();

    float getHeight();

    float getScaledWidth();

    float getScaledHeight();

    float getAbsoluteX();

    float getAbsoluteY();

    float getAbsoluteScaleX();

    float getAbsoluteScaleY();

    float getAbsoluteAlpha();

    float getAbsoluteRotation();

    boolean isAbsoluteVisible();

    void setAbsoluteZOrderIndex(int zOrder);

    int getAbsoluteZOrderIndex();

    void removeFromParent();

    void setIntegerPixelAlignmentEnabled(boolean value);

    boolean isIntegerPixelAlignmentEnabled();

    String toString();

    default void onExitFrame() {
    }

    default void onEnterFrame() {
    }

    default void onLoopUpdate() {
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<NodeEvent.LoopUpdate> onLoopUpdate(EventListener<NodeEvent.LoopUpdate> listener) {
        return (EventHandleRegistration<NodeEvent.LoopUpdate>) on(NodeEvent.LoopUpdate.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<NodeEvent.BeforeRenderFrame> onEnterFrame(EventListener<NodeEvent.BeforeRenderFrame> listener) {
        return (EventHandleRegistration<NodeEvent.BeforeRenderFrame>) on(NodeEvent.BeforeRenderFrame.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<NodeEvent.AfterRenderFrame> onExitFrame(EventListener<NodeEvent.AfterRenderFrame> listener) {
        return (EventHandleRegistration<NodeEvent.AfterRenderFrame>) on(NodeEvent.AfterRenderFrame.class, listener);
    }

}