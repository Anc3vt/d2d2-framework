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


final class DisplayObjectAbsoluteComputer {

    private DisplayObjectAbsoluteComputer() {}

    static float getAbsoluteX(DisplayObject displayObject) {
        float result = displayObject.getX();

        IContainer parent = displayObject.getParent();

        while (parent != null && !(parent instanceof Stage)) {
            result *= parent.getScaleX();
            result += parent.getX();
            parent = parent.getParent();
        }

        return result;
    }

    static float getAbsoluteY(final DisplayObject displayObject) {
        float result = displayObject.getY();

        IContainer parent = displayObject.getParent();

        while (parent != null && !(parent instanceof Stage)) {
            result *= parent.getScaleY();
            result += parent.getY();
            parent = parent.getParent();
        }

        return result;
    }

    static float getAbsoluteScaleX(final DisplayObject displayObject) {
        float result = displayObject.getScaleX();

        IContainer parent = displayObject.getParent();

        while (parent != null && !(parent instanceof Stage)) {
            result *= parent.getScaleX();
            parent = parent.getParent();
        }

        return result;
    }

    static float getAbsoluteScaleY(final DisplayObject displayObject) {
        float result = displayObject.getScaleY();

        IContainer parent = displayObject.getParent();

        while (parent != null && !(parent instanceof Stage)) {
            result *= parent.getScaleY();
            parent = parent.getParent();
        }

        return result;
    }

    static float getAbsoluteAlpha(final DisplayObject displayObject) {
        float result = displayObject.getAlpha();

        IContainer parent = displayObject.getParent();

        while (parent != null && !(parent instanceof Stage)) {
            result *= parent.getAlpha();
            parent = parent.getParent();
        }

        return result;
    }

    static float getAbsoluteRotation(final DisplayObject displayObject) {
        float result = displayObject.getRotation();

        IContainer parent = displayObject.getParent();

        while (parent != null && !(parent instanceof Stage)) {
            result += parent.getRotation();
            parent = parent.getParent();
        }

        return result;
    }

    public static boolean isAbsoluteVisible(DisplayObject displayObject) {
        IContainer parent = displayObject.getParent();

        if (!displayObject.isVisible()) return false;

        while (parent != null && !(parent instanceof Stage)) {
            if (!parent.isVisible()) return false;
            parent = parent.getParent();
        }

        return true;
    }
}
