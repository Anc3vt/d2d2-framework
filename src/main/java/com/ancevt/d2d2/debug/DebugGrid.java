/**
 * Copyright (C) 2023 the original author or authors.
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
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.IColored;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.event.Event;

import java.util.ArrayList;
import java.util.List;

public class DebugGrid extends Container implements IColored {

    public static final int SIZE = 16;

    private static final float ALPHA = 0.075f;
    public static final int DEFAULT_COLOR = 0xFFFFFF;

    private final List<Line> lines;

    public DebugGrid() {
        lines = new ArrayList<>();
        setColor(DEFAULT_COLOR);

        for (int i = 0; i < D2D2.stage().getWidth(); i += SIZE) {
            BitmapText bitmapText = new BitmapText(String.valueOf(i));
            add(bitmapText, i, i);
        }
    }

    private void recreate(Color color) {
        removeAllChildren();

        final int w = (int) D2D2.stage().getWidth();
        final int h = (int) D2D2.stage().getHeight();

        for (float x = 0; x < w; x += SIZE) {
            Line line = new Line(Line.VERTICAL);
            line.setColor(color);
            line.setX(x);
            line.setAlpha(ALPHA);
            add(line);
            lines.add(line);
        }

        for (float y = 0; y < h; y += SIZE) {
            Line line = new Line(Line.HORIZONTAL);
            line.setColor(color);
            line.setY(y);
            line.setAlpha(ALPHA);
            add(line);
            lines.add(line);
        }
    }

    @Override
    public void setColor(Color color) {
        recreate(color);
    }

    @Override
    public Color getColor() {
        return lines.get(0).getColor();
    }

    @Override
    public void setColor(int rgb) {
        setColor(new Color(rgb));
    }

    private static class Line extends PlainRect {

        public static final byte HORIZONTAL = 0x00;
        public static final byte VERTICAL = 0x01;

        private int orientation;

        public Line(byte orientation) {
            super(1.0f, 1.0f);
            setOrientation(orientation);

            addEventListener(Event.EACH_FRAME, this::eachFrame);
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

        public int getOrientation() {
            return orientation;
        }

        private void setOrientation(int orientation) {
            this.orientation = orientation;

            final int w = (int) D2D2.stage().getWidth();
            final int h = (int) D2D2.stage().getHeight();

            switch (orientation) {
                case HORIZONTAL:
                    setScale(w, 1.0f);
                    break;
                case VERTICAL:
                    setScale(1.0f, h);
                    break;
            }
        }

    }
}


