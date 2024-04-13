/**
 * Copyright (C) 2024 the original author or authors.
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

import com.ancevt.d2d2.event.IEventDispatcher;

import java.util.HashMap;
import java.util.Map;

public interface IDisplayObject extends IEventDispatcher {

    Map<IDisplayObject, Map<String, Object>> extraMap = new HashMap<>();

    default void setExtra(Map<String, Object> extra) {
        extraMap.put(this, extra);
    }

    default Map<String, Object> getExtra() {
        return extraMap.computeIfAbsent(this, o -> new HashMap<>());
    }

    default <T> IDisplayObject putExtra(String key, T object) {
        getExtra().put(key, object);
        return this;
    }

    default <T> T getExtra(String key) {
        return (T) getExtra().get(key);
    }

    int displayObjectId();

    String getName();

    void setName(String value);

    IContainer getParent();

    boolean hasParent();

    void setAlpha(float value);

    float getAlpha();

    void toAlpha(float value);

    void setXY(float x, float y);

    void setX(float value);

    float getX();

    void setY(float value);

    float getY();

    void setScale(float scaleX, float scaleY);

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

    void toScaleX(float value);

    void toScaleY(float value);

    void toScale(float toX, float toY);

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

    void setAbsoluteZOrderIndex(int zOrder);

    int getAbsoluteZOrderIndex();

    void removeFromParent();

    String toString();

    default void onExitFrame() {}
    default void onEnterFrame() {}
    default void onLoopUpdate() {}
}
