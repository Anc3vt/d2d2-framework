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

package com.ancevt.d2d2.scene.shape;

import com.ancevt.d2d2.scene.AbstractNode;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Colored;
import com.ancevt.d2d2.scene.Textured;
import com.ancevt.d2d2.scene.texture.TextureRegion;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FreeShape extends AbstractNode implements Shape, Colored, Textured {

    @Getter
    private List<Vertex> vertices = new ArrayList<>();

    @Setter
    private Color color = Color.WHITE;

    @Getter
    private float currentX;
    @Getter
    private float currentY;

    @Getter
    @Setter
    private float textureURepeat = 1.0f;

    @Getter
    @Setter
    private float textureVRepeat = 1.0f;

    @Getter
    @Setter
    private float textureRotation = 0f; // в радианах
    @Getter
    @Setter
    private float textureScaleX = 1f;
    @Getter
    @Setter
    private float textureScaleY = 1f;

    @Getter
    @Setter
    private TextureRegion textureRegion;

    @Getter
    private List<TriangleInfo> triangleInfos = new ArrayList<>();

    @Override
    public void setTextureUVRepeat(float uRepeat, float vRepeat) {
        this.textureURepeat = uRepeat;
        this.textureVRepeat = vRepeat;
    }


    @Override
    public void setTextureScale(float scaleX, float scaleY) {
        textureScaleX = scaleX;
        textureScaleY = scaleY;
    }

    public boolean isPointInsideFreeShape(float x, float y) {
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


    public Vertex addVertex(float x, float y) {
        this.currentX = x;
        this.currentY = y;
        var result = new Vertex(x, y);
        vertices.add(result);
        return result;
    }

    public void curveTo(float controlX1, float controlY1, float controlX2, float controlY2, float endX, float endY, float step) {
        float t = 0.0f;
        while (t <= 1.0f) {
            float oneMinusT = 1.0f - t;
            float x = oneMinusT * oneMinusT * oneMinusT * currentX +
                    3 * oneMinusT * oneMinusT * t * controlX1 +
                    3 * oneMinusT * t * t * controlX2 +
                    t * t * t * endX;
            float y = oneMinusT * oneMinusT * oneMinusT * currentY +
                    3 * oneMinusT * oneMinusT * t * controlY1 +
                    3 * oneMinusT * t * t * controlY2 +
                    t * t * t * endY;
            addVertex(x, y);
            this.currentX = x;
            this.currentY = y;
            t += step;
        }
        // Обновляем currentX и currentY

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + vertices.size() + "}";
    }

    public void closePath() {
        var begin = vertices.get(0);
        addVertex(begin.getX(), begin.getY());
    }

    public FreeShape copy() {
        FreeShape copy = new FreeShape();

        List<Vertex> copyVertices = new ArrayList<>();
        for (Vertex v : vertices) {
            Vertex newVertex = new Vertex(v.getX(), v.getY());
            copyVertices.add(newVertex);
        }
        copy.vertices = copyVertices;

        List<TriangleInfo> copyTriangleInfos = new ArrayList<>();
        for (TriangleInfo t : triangleInfos) {
            TriangleInfo newTriangleInfo = new TriangleInfo(t.getX1(), t.getY1(), t.getX2(), t.getY2(), t.getX3(), t.getY3());
        }
        copy.triangleInfos = copyTriangleInfos;
        return copy;
    }

    public void commit() {
        triangleInfos.clear();

        List<Double> coords = new ArrayList<>();
        for (Vertex vertex : vertices) {
            coords.add((double) vertex.x);
            coords.add((double) vertex.y);
        }

        double[] array = coords.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        List<Integer> indices = Earcut.earcut(array);

        int[] indicesArray = indices.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        for (int i = 0; i < indicesArray.length; i += 3) {
            int idx1 = indicesArray[i] * 2;
            int idx2 = indicesArray[i + 1] * 2;
            int idx3 = indicesArray[i + 2] * 2;

            TriangleInfo t = new TriangleInfo(
                    (float) array[idx1], (float) array[idx1 + 1],
                    (float) array[idx2], (float) array[idx2 + 1],
                    (float) array[idx3], (float) array[idx3 + 1]
            );
            triangleInfos.add(t);
        }


    }

}
