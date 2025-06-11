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
import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.event.StageEvent;
import com.ancevt.d2d2.event.core.EventDispatcher;
import com.ancevt.d2d2.event.core.EventLink;
import com.ancevt.d2d2.event.core.EventListener;
import com.ancevt.d2d2.scene.shader.ShaderProgram;
import com.ancevt.d2d2.util.Disposable;

public interface Node extends EventDispatcher, Disposable {

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

    default void setPositionAs(Node node) {
        setPosition(node.getX(), node.getY());
    }

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

    default void moveX(float value) {
        setX(getX() + value);
    }

    default void moveY(float value) {
        setY(getY() + value);
    }

    default void move(float toX, float toY) {
        moveX(toX);
        moveY(toY);
    }

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

    void setShaderProgram(ShaderProgram shaderProgram);

    ShaderProgram getShaderProgram();

    @SuppressWarnings("unchecked")
    default EventLink<NodeEvent.Add> onAdd(EventListener<NodeEvent.Add> listener) {
        return on(NodeEvent.Add.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<NodeEvent.Remove> onRemove(EventListener<NodeEvent.Remove> listener) {
        return on(NodeEvent.Remove.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<NodeEvent.AddToScene> onAddToScene(EventListener<NodeEvent.AddToScene> listener) {
        return on(NodeEvent.AddToScene.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<NodeEvent.RemoveFromScene> onRemoveFromScene(EventListener<NodeEvent.RemoveFromScene> listener) {
        return on(NodeEvent.RemoveFromScene.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<NodeEvent.Dispose> onDispose(EventListener<NodeEvent.Dispose> listener) {
        return on(NodeEvent.Dispose.class, listener);
    }

    default EventLink<StageEvent.Tick> onStageTick(EventListener<StageEvent.Tick> listener) {
        EventLink<StageEvent.Tick> link = D2D2.getStage().onTick(e -> {
            if (isOnScreen()) listener.onEvent(e);
        });

        EventLink<NodeEvent.RemoveFromScene> removeFromSceneEventLink = onRemoveFromScene(e -> link.setPaused(true));
        EventLink<NodeEvent.AddToScene> addToSceneEventLink = onAddToScene(e -> link.setPaused(false));

        onDispose(e -> {
            link.unregister();
            removeFromSceneEventLink.unregister();
            addToSceneEventLink.unregister();
        });
        return link;
    }


}


