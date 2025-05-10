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


final class GlobalMetrics {

    private GlobalMetrics() {
    }

    private static float computeGlobalValue(AbstractNode node, java.util.function.BiFunction<Group, Float, Float> accumulator) {
        float result = accumulator.apply(null, 0f);
        Group parent = node.getParent();

        while (parent != null && !(parent instanceof Root)) {
            result = accumulator.apply(parent, result);
            parent = parent.getParent();
        }

        return result;
    }

    static float getGlobalX(AbstractNode node) {
        return computeGlobalValue(node, (parent, acc) -> {
            if (parent == null) return node.getX();
            return acc * parent.getScaleX() + parent.getX();
        });
    }

    static float getGlobalY(AbstractNode node) {
        return computeGlobalValue(node, (parent, acc) -> {
            if (parent == null) return node.getY();
            return acc * parent.getScaleY() + parent.getY();
        });
    }

    static float getGlobalScaleX(AbstractNode node) {
        return computeGlobalValue(node, (parent, acc) -> {
            if (parent == null) return node.getScaleX();
            return acc * parent.getScaleX();
        });
    }

    static float getGlobalScaleY(AbstractNode node) {
        return computeGlobalValue(node, (parent, acc) -> {
            if (parent == null) return node.getScaleY();
            return acc * parent.getScaleY();
        });
    }

    static float getGlobalAlpha(AbstractNode node) {
        return computeGlobalValue(node, (parent, acc) -> {
            if (parent == null) return node.getAlpha();
            return acc * parent.getAlpha();
        });
    }

    static float getGlobalRotation(AbstractNode node) {
        return computeGlobalValue(node, (parent, acc) -> {
            if (parent == null) return node.getRotation();
            return acc + parent.getRotation();
        });
    }

    public static boolean isGloballyVisible(AbstractNode node) {
        Group parent = node.getParent();

        if (!node.isVisible()) return false;

        while (parent != null && !(parent instanceof Root)) {
            if (!parent.isVisible()) return false;
            parent = parent.getParent();
        }

        return true;
    }
}
