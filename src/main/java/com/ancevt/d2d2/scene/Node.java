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

import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.event.core.EventDispatcher;
import com.ancevt.d2d2.event.core.EventLink;
import com.ancevt.d2d2.event.core.EventListener;

public interface Node extends EventDispatcher {

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
            setY(-getHeight() / 2);
        }
    }

    default void center() {
        centerX();
        centerY();
    }

    int getNodeId();

    String getName();

    void setName(String value);

    Group getParent();

    boolean hasParent();

    void setAlpha(float value);

    float getAlpha();

    void toAlpha(float value);

    void setPosition(float x, float y);

    void setX(float value);

    void setY(float value);

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

    float getGlobalX();

    float getGlobalY();

    float getGlobalScaleX();

    float getGlobalScaleY();

    float getGlobalAlpha();

    float getGlobalRotation();

    boolean isGloballyVisible();

    void setGlobalZOrderIndex(int zOrder);

    int getGlobalZOrderIndex();

    void removeFromParent();

    void setIntegerPixelAlignmentEnabled(boolean value);

    boolean isIntegerPixelAlignmentEnabled();

    default void preFrame() {
    }

    default void postFrame() {
    }

    default void tick() {
    }

    @SuppressWarnings("unchecked")
    default EventLink<SceneEvent.Tick> onTick(EventListener<SceneEvent.Tick> listener) {
        return (EventLink<SceneEvent.Tick>) on(SceneEvent.Tick.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<SceneEvent.PreFrame> onPreFrame(EventListener<SceneEvent.PreFrame> listener) {
        return (EventLink<SceneEvent.PreFrame>) on(SceneEvent.PreFrame.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<SceneEvent.PostFrame> onPostFrame(EventListener<SceneEvent.PostFrame> listener) {
        return (EventLink<SceneEvent.PostFrame>) on(SceneEvent.PostFrame.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<SceneEvent.Add> onAdd(EventListener<SceneEvent.Add> listener) {
        return on(SceneEvent.Add.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<SceneEvent.Remove> onRemove(EventListener<SceneEvent.Remove> listener) {
        return on(SceneEvent.Remove.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<SceneEvent.AddToScene> onAddToScene(EventListener<SceneEvent.AddToScene> listener) {
        return on(SceneEvent.AddToScene.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<SceneEvent.RemoveFromScene> onRemoveFromScene(EventListener<SceneEvent.RemoveFromScene> listener) {
        return on(SceneEvent.RemoveFromScene.class, listener);
    }


}


