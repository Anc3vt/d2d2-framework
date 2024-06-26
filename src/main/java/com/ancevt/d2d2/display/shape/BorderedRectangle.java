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
package com.ancevt.d2d2.display.shape;

import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.SimpleContainer;
import lombok.Getter;

public class BorderedRectangle extends SimpleContainer {
    private static final Color DEFAULT_FILL_COLOR = Color.WHITE;
    private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;

    private static final float DEFAULT_WIDTH = 16;
    private static final float DEFAULT_HEIGHT = 16;

    private final RectangleShape fillRect;

    @Getter
    private final LineBatch lineBatch = new LineBatch();

    public BorderedRectangle() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FILL_COLOR, DEFAULT_BORDER_COLOR);
    }

    public BorderedRectangle(float width, float height) {
        this(width, height, DEFAULT_FILL_COLOR, DEFAULT_BORDER_COLOR);
    }

    public BorderedRectangle(Color fillColor) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fillColor, DEFAULT_BORDER_COLOR);
    }

    public BorderedRectangle(Color fillColor, Color borderColor) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, fillColor, borderColor);
    }

    public BorderedRectangle(float width, float height, Color fillColor) {
        this(width, height, fillColor, DEFAULT_BORDER_COLOR);
    }

    public BorderedRectangle(float width, float height, Color fillColor, Color borderColor) {
        fillRect = new RectangleShape();

        fillRect.setSize(1, 1);

        addChild(fillRect);

        setFillColor(fillColor);
        setBorderColor(borderColor);

        setSize(width, height);
        addChild(lineBatch);
    }

    public void setWidth(float width) {
        fillRect.setWidth(width);
        rebuildBorders();
    }

    public void setHeight(float height) {
        fillRect.setHeight(height);
        rebuildBorders();
    }

    public void setSize(float width, float height) {
        fillRect.setWidth(width);
        fillRect.setHeight(height);
        rebuildBorders();
    }

    @Override
    public float getWidth() {
        return fillRect.getWidth();
    }

    @Override
    public float getHeight() {
        return fillRect.getHeight();
    }

    public void setFillColor(Color color) {
        if (color == null) {
            if (fillRect.getParent() != null)
                fillRect.removeFromParent();
        } else {
            if (fillRect.getParent() == null)
                addChild(fillRect, 0);
        }

        fillRect.setColor(color);
    }

    public Color getFillColor() {
        return fillRect.getColor();
    }

    public void setBorderColor(Color color) {
        lineBatch.setVisible(color != null);
        if (color != null) {
            lineBatch.setColor(color);
        }
    }

    public Color getBorderColor() {
        return lineBatch.getColor();
    }

    public void setBorderWidth(float borderWidth) {
        getLineBatch().setLineWidth(borderWidth);
    }

    public float getBorderWidth() {
        return lineBatch.getLineWidth();
    }

    private void rebuildBorders() {
        lineBatch.getLines().clear();
        lineBatch.moveTo(0, 0);
        lineBatch.lineTo(getWidth(), 0);
        lineBatch.lineTo(getWidth(), getHeight());
        lineBatch.lineTo(0, getHeight());
        lineBatch.closePath();
    }
}














