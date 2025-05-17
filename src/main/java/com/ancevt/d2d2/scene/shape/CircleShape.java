/**
 * Copyright (C) 2025 the original author or authors.
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

package com.ancevt.d2d2.scene.shape;

public class CircleShape extends FreeShape {

    public static final float DEFAULT_RADIUS = 100;
    public static final int DEFAULT_NUM_VERTICES = 50;

    public CircleShape() {
        this(DEFAULT_RADIUS, DEFAULT_NUM_VERTICES);
    }

    public CircleShape(int radius) {
        this(radius, DEFAULT_NUM_VERTICES);
    }

    public CircleShape(float radius, int numVertices) {
        for (int i = 0; i < numVertices; i++) {
            var angle = 2 * Math.PI * i / numVertices;
            var x = radius * Math.cos(angle);
            var y = radius * Math.sin(angle);
            addVertex((float) x, (float) y);
        }
        commit();
    }
}
