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


package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.input.MouseButton;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Container;
import com.ancevt.d2d2.scene.interactive.InteractiveContainer;
import com.ancevt.d2d2.scene.shape.RectangleShape;
import lombok.Getter;

public class DraggingContainer extends InteractiveContainer {

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;

    private static final int MIN_WIDTH = 20;
    private static final int MIN_HEIGHT = 20;

    private int mouseButton;

    private int oldX;
    private int oldY;

    @Getter
    private final RectangleShape background;

    public DraggingContainer() {
        background = new RectangleShape(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.BLACK);
        addChild(background);

        addEventListener(this, InputEvent.MouseDown.class, this::mouseDown);
        addEventListener(this, InputEvent.MouseDrag.class, this::mouseDrag);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void mouseDown(InputEvent.MouseDown e) {
        mouseButton = e.button();

        oldX = (int) (e.x() + getX());
        oldY = (int) (e.y() + getY());

        Container parent = getParent();
        parent.removeChild(this);
        parent.addChild(this);

        focus();
    }

    public void setBackgroundColor(Color color) {
        background.setColor(color);
    }

    public Color getBackgroundColor() {
        return background.getColor();
    }

    public void setBackgroundVisible(boolean b) {
        if (b == isBackgroundVisible()) return;

        if (b) {
            addChild(background);
        } else {
            removeChild(background);
        }
    }

    public boolean isBackgroundVisible() {
        return contains(background);
    }

    private void mouseDrag(InputEvent.MouseDrag e) {
        if (mouseButton == MouseButton.RIGHT) {
            setSize(e.x() / getScaleX() + 1, e.y() / getScaleY() + 1);

            if (getWidth() < MIN_WIDTH) {
                setWidth(MIN_WIDTH);
            }
            if (getHeight() < MIN_HEIGHT) {
                setHeight(MIN_HEIGHT);
            }
            return;
        }

        final int tx = (int) (e.x() + getX());
        final int ty = (int) (e.y() + getY());

        move(tx - oldX, ty - oldY);

        oldX = tx;
        oldY = ty;
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        background.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        background.setHeight(height);
    }
}
