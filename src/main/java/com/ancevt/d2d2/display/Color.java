/**
 * Copyright (C) 2022 the original author or authors.
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

import java.util.Objects;
import java.util.Random;

public class Color {

    public static final Color BLACK = new Color(0, true);
    public static final Color WHITE = new Color(0xFFFFFF, true);
    public static final Color GREEN = new Color(0x00FF00, true);
    public static final Color BLUE = new Color(0x0000FF, true);
    public static final Color DARK_BLUE = new Color(0x000080, true);
    public static final Color YELLOW = new Color(0xFFFF00, true);
    public static final Color DARK_YELLOW = new Color(0x808000, true);
    public static final Color RED = new Color(0xFF0000, true);
    public static final Color DARK_RED = new Color(0x800000, true);
    public static final Color PINK = new Color(0xFF8080, true);
    public static final Color ORANGE = new Color(0xFF8000, true);
    public static final Color MAGENTA = new Color(0xFF00FF, true);
    public static final Color DARK_GREEN = new Color(0x008000, true);
    public static final Color LIGHT_GREEN = new Color(0x80FF80, true);
    public static final Color GRAY = new Color(0x808080, true);
    public static final Color DARK_GRAY = new Color(0x3F3F3F, true);
    public static final Color LIGHT_GRAY = new Color(0xBBBBBB, true);

    private int r;
    private int g;
    private int b;

    private final boolean immutable;

    public Color(int r, int g, int b, boolean immutable) {
        setRGB(r, g, b);
        this.immutable = immutable;
    }

    public Color(int r, int g, int b) {
        this(r, g, b, false);
    }

    public Color(int rgb, boolean immutable) {
        setValue(rgb);
        this.immutable = immutable;
    }

    public Color(int rgb) {
        this(rgb, false);
    }

    public Color(String hex, boolean immutable) {
        setValue(Integer.parseInt(hex, 16));
        this.immutable = immutable;
    }

    public Color(String hex) {
        this(hex, false);
    }

    public void setRGB(int r, int g, int b) {
        setR(r);
        setG(g);
        setB(b);
    }

    public void setValue(int rgb) {
        setR((rgb >> 16) & 0xFF);
        setG((rgb >> 8) & 0xFF);
        setB(rgb & 255);
    }

    public int getValue() {
        return r << 16 | g << 8 | b;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        constantCheck();
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        constantCheck();
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        constantCheck();
        this.b = b;
    }

    public boolean isImmutable() {
        return immutable;
    }

    private void constantCheck() {
        if (immutable)
            throw new IllegalStateException("The color object is immutable");
    }

    public String toHexString() {
        return Integer.toHexString(getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return r == color.r && g == color.g && b == color.b && immutable == color.immutable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b, immutable);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Color[0x");

        final String hex = Integer.toHexString(getValue());

        if (hex.length() < 6) sb.append('0');

        sb.append(hex);

        if (immutable)
            sb.append(", const");

        sb.append(']');

        return sb.toString();
    }

    public Color cloneColor() {
        return new Color(getValue(), isImmutable());
    }

    public static Color createRandomColor() {
        return new Color((int) (Math.random() * 0xFFFFFF));
    }

    public static Color createVisibleRandomColor() {
        Random random = new Random();

        final int[] values = new int[]{0x00, 0x40, 0x80, 0xC0, 0xFF};

        int r, g, b;

        do {
            r = values[random.nextInt(values.length)];
            g = values[random.nextInt(values.length)];
            b = values[random.nextInt(values.length)];
        } while (r <= 0x40 && g <= 0x40 && b <= 0x00);

        return new Color(r, g, b);
    }

    public static Color of(int rgb) {
        return new Color(rgb, true);
    }
}
