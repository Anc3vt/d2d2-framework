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
package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.shape.RectangleShape;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.IColored;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.event.Event;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Grid extends Container implements IColored {

    public static final int DEFAULT_COLOR = 0xFFFFFF;

    private final List<Line> lines;

    @Getter
    private float width;

    @Getter
    private float height;

    @Getter
    private float cellSize;

    @Getter
    private Color color;

    public Grid(float width, float height, float cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        lines = new ArrayList<>();
        setColor(DEFAULT_COLOR);
    }


    public Grid() {
        this(D2D2.stage().getWidth(), D2D2.stage().getHeight(), 16);
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
        update();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        update();
    }

    public void setWidth(float width) {
        this.width = width;
        update();
    }

    public void setHeight(float height) {
        this.height = height;
        update();
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        update();
    }

    private void update() {
        removeAllChildren();

        for (float x = 0; x < width; x += cellSize) {
            Line line = new Line(Line.VERTICAL, this);
            line.setColor(color);
            line.setX(x);
            add(line);
            lines.add(line);
        }

        for (float y = 0; y < height; y += cellSize) {
            Line line = new Line(Line.HORIZONTAL, this);
            line.setColor(color);
            line.setY(y);
            add(line);
            lines.add(line);
        }
    }

    @Getter
    private static class Line extends RectangleShape {

        public static final byte HORIZONTAL = 0x00;
        public static final byte VERTICAL = 0x01;
        private final Grid grid;

        private int orientation;

        public Line(byte orientation, Grid grid) {
            super(1.0f, 1.0f);
            this.grid = grid;
            setOrientation(orientation);

            addEventListener(Event.EXIT_FRAME, this::eachFrame);
        }

        private void eachFrame(Event event) {
            IContainer parent = getParent();
            switch (orientation) {
                case HORIZONTAL:
                    setScaleY(1.0f / parent.getAbsoluteScaleY());
                    break;
                case VERTICAL:
                    setScaleX(1.0f / parent.getAbsoluteScaleX());
                    break;
            }
        }

        private void setOrientation(int orientation) {
            this.orientation = orientation;

            switch (orientation) {
                case HORIZONTAL:
                    setScale(grid.getWidth(), 1.0f);
                    break;
                case VERTICAL:
                    setScale(1.0f, grid.getHeight());
                    break;
            }
        }

    }
}


