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


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Vertex {
    public float x;
    public float y;

    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static boolean isPointInsidePolygon(List<Vertex> vertices, float x, float y) {
        int intersections = 0;
        int vertexCount = vertices.size();

        for (int i = 0; i < vertexCount; i++) {
            Vertex v1 = vertices.get(i);
            Vertex v2 = vertices.get((i + 1) % vertexCount);

            if (isIntersecting(v1, v2, x, y)) {
                intersections++;
            }
        }

        return (intersections % 2) == 1;
    }

    private static boolean isIntersecting(Vertex v1, Vertex v2, float x, float y) {
        if (v1.y > v2.y) {
            Vertex temp = v1;
            v1 = v2;
            v2 = temp;
        }

        if (y == v1.y || y == v2.y) {
            y += 0.0001f;
        }

        if (y > v2.y || y < v1.y || x > Math.max(v1.x, v2.x)) {
            return false;
        }

        if (x < Math.min(v1.x, v2.x)) {
            return true;
        }

        double red = (double) (v2.x - v1.x) * (y - v1.y) / (v2.y - v1.y) + v1.x;
        return x < red;
    }

}
