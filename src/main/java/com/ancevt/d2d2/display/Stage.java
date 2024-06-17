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

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;
import lombok.Getter;


public class Stage extends SimpleContainer {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

    private int width;
    private int height;


    @Getter
    private Color backgroundColor;

    public Stage() {
        setName("_" + getClass().getSimpleName() + getDisplayObjectId());
        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundColor(int rgb) {
        setBackgroundColor(Color.of(rgb));
    }

    public void setSize(float width, float height) {
        D2D2.displayManager().setWindowSize((int) width, (int) height);
    }

    public void onResize(int width, int height) {
        this.width = width;
        this.height = height;
        dispatchEvent(EventPool.simpleEventSingleton(Event.RESIZE, this));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "name=" + getName() +
            ", width=" + width +
            ", height=" + height +
            '}';
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    static void dispatchAddToStage(DisplayObject displayObject) {
        if (displayObject.isOnScreen()) {
            displayObject.dispatchEvent(EventPool.createEvent(Event.ADD_TO_STAGE));
            if (displayObject instanceof Container container) {
                for (int i = 0; i < container.getNumChildren(); i++) {
                    dispatchAddToStage(container.getChild(i));
                }
            }
        }
    }

    static void dispatchRemoveFromStage(DisplayObject displayObject) {
        if (displayObject.isOnScreen()) {
            displayObject.dispatchEvent(EventPool.createEvent(Event.REMOVE_FROM_STAGE));

            if (displayObject instanceof Container container) {
                for (int i = 0; i < container.getNumChildren(); i++) {
                    dispatchRemoveFromStage(container.getChild(i));
                }
            }
        }
    }
}
