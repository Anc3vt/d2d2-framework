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

import com.ancevt.d2d2.scene.AbstractNode;
import com.ancevt.d2d2.scene.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class LineBatch extends AbstractNode implements Shape {

    private Color color = Color.WHITE;

    @Getter
    private final List<Line> lines = new ArrayList<>();

    @Getter
    private float currentX = 0f;

    @Getter
    private float currentY = 0f;

    @Getter
    private boolean started = false;

    private float startX = 0f;
    private float startY = 0f;

    @Setter
    @Getter
    private float lineWidth = 1f;

    @Getter
    @Setter
    private int stippleFactor = 1;

    @Getter
    @Setter
    private short stipple = 0;

    private Line currentLine = null;

    public LineBatch() {
        setName("_" + getClass().getSimpleName() + getNodeId());
    }

    public void moveTo(float x, float y) {
        if (!started) {
            startX = x;
            startY = y;
            started = true;
        }
        if (currentLine != null) {
            currentLine.closing = true;
        }

        currentX = x;
        currentY = y;
    }

    public void moveTo(int x, int y) {
        moveTo((float) x, (float) y);
    }

    public Line lineTo(float x, float y) {
        Vertex vertexA = new Vertex(currentX, currentY);
        Vertex vertexB = new Vertex(x, y);

        currentX = x;
        currentY = y;

        currentLine = new Line(vertexA, vertexB);
        lines.add(currentLine);

        return currentLine;
    }

    public Line lintTo(int x, int y) {
        return lineTo((float) x, (float) y);
    }

    public void closePath() {
        if (!started) throw new IllegalStateException("Lint batch has not been started");
        lineTo(startX, startY);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Line {
        public final Vertex vertexA;
        public final Vertex vertexB;
        private boolean closing;
    }
}
