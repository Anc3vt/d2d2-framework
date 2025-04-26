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

package com.ancevt.d2d2.scene;

import java.util.Objects;
import java.util.Random;

public class Color {
    public static final Color NO_COLOR = null;
    public static final Color BLACK = Color.of(0x000000);
    public static final Color GRAY = Color.of(0x808080);
    public static final Color LIGHT_GRAY = Color.of(0xC8C8C8);
    public static final Color SILVER = Color.of(0xC0C0C0);
    public static final Color WHITE = Color.of(0xFFFFFF);
    public static final Color MAGENTA = Color.of(0xFF00FF);
    public static final Color PURPLE = Color.of(0x800080);
    public static final Color RED = Color.of(0xFF0000);
    public static final Color MAROON = Color.of(0x800000);
    public static final Color YELLOW = Color.of(0xFFFF00);
    public static final Color OLIVE = Color.of(0x808000);
    public static final Color LIME = Color.of(0x00FF00);
    public static final Color GREEN = Color.of(0x008000);
    public static final Color AQUA = Color.of(0x00FFFF);
    public static final Color TEAL = Color.of(0x008080);
    public static final Color BLUE = Color.of(0x0000FF);
    public static final Color NAVY = Color.of(0x000080);
    public static final Color WHITE_SMOKE = Color.of(0xF5F5F5);
    public static final Color GAINSBORO = Color.of(0xDCDCDC);
    public static final Color LIGHTGRAY = Color.of(0xD3D3D3);
    public static final Color DARK_GRAY = Color.of(0xA9A9A9);
    public static final Color DIMGRAY = Color.of(0x696969);
    public static final Color LIGHT_SLATE_GRAY = Color.of(0x778899);
    public static final Color SLATE_GRAY = Color.of(0x708090);
    public static final Color DARK_SLATE_GRAY = Color.of(0x2F4F4F);
    public static final Color LIGHT_CORAL = Color.of(0xF08080);
    public static final Color SALMON = Color.of(0xFA8072);
    public static final Color DARK_SALMON = Color.of(0xE9967A);
    public static final Color LIGHT_SALMON = Color.of(0xFFA07A);
    public static final Color CRIMSON = Color.of(0xDC143C);
    public static final Color INDIAN_RED = Color.of(0xCD5C5C);
    public static final Color FIRE_BRICK = Color.of(0xB22222);
    public static final Color BROWN = Color.of(0xA52A2A);
    public static final Color DARK_RED = Color.of(0x8B0000);
    public static final Color SEASHELL = Color.of(0xFFF5EE);
    public static final Color BEIGE = Color.of(0xF5F5DC);
    public static final Color OLD_LACE = Color.of(0xFDF5E6);
    public static final Color FLORAL_WHITE = Color.of(0xFFFAF0);
    public static final Color ANTIQUE_WHITE = Color.of(0xFAEBD7);
    public static final Color LINEN = Color.of(0xFAF0E6);
    public static final Color BLANCHED_ALMOND = Color.of(0xFFEBCD);
    public static final Color BISQUE = Color.of(0xFFE4C4);
    public static final Color NAVAJO_WHITE = Color.of(0xFFDEAD);
    public static final Color WHEAT = Color.of(0xF5DEB3);
    public static final Color BURLY_WOOD = Color.of(0xDEB887);
    public static final Color TAN = Color.of(0xD2B48C);
    public static final Color SANDY_BROWN = Color.of(0xF4A460);
    public static final Color GOLDENROD = Color.of(0xDAA520);
    public static final Color DARK_GOLDENROD = Color.of(0xB8860B);
    public static final Color PERU = Color.of(0xCD853F);
    public static final Color CHOCOLATE = Color.of(0xD2691E);
    public static final Color SADDLE_BROWN = Color.of(0x8B4513);
    public static final Color SIENNA = Color.of(0xA0522D);
    public static final Color CORAL = Color.of(0xFF7F50);
    public static final Color TOMATO = Color.of(0xFF6347);
    public static final Color ORANGE_RED = Color.of(0xFF4500);
    public static final Color DARK_ORANGE = Color.of(0xFF8C00);
    public static final Color ORANGE = Color.of(0xFFA500);
    public static final Color CORNSILK = Color.of(0xFFF8DC);
    public static final Color IVORY = Color.of(0xFFFFF0);
    public static final Color LIGHT_YELLOW = Color.of(0xFFFFE0);
    public static final Color LEMON_CHIFFON = Color.of(0xFFFACD);
    public static final Color LIGHT_GOLDENRODYELLOW = Color.of(0xFAFAD2);
    public static final Color PAPAYA_WHIP = Color.of(0xFFEFD5);
    public static final Color MOCCASIN = Color.of(0xFFE4B5);
    public static final Color PEACH_PUFF = Color.of(0xFFDAB9);
    public static final Color PALE_GOLDENROD = Color.of(0xEEE8AA);
    public static final Color KHAKI = Color.of(0xF0E68C);
    public static final Color DARK_KHAKI = Color.of(0xBDB76B);
    public static final Color GOLD = Color.of(0xFFD700);
    public static final Color HONEYDEW = Color.of(0xF0FFF0);
    public static final Color MINT_CREAM = Color.of(0xF5FFFA);
    public static final Color GREEN_YELLOW = Color.of(0xADFF2F);
    public static final Color CHARTREUSE = Color.of(0x7FFF00);
    public static final Color LAWN_GREEN = Color.of(0x7CFC00);
    public static final Color LIME_GREEN = Color.of(0x32CD32);
    public static final Color PALE_GREEN = Color.of(0x98FB98);
    public static final Color LIGHT_GREEN = Color.of(0x90EE90);
    public static final Color MEDIUM_SPRING_GREEN = Color.of(0x00FA9A);
    public static final Color SPRING_GREEN = Color.of(0x00FF7F);
    public static final Color MEDIUM_SEA_GREEN = Color.of(0x3CB371);
    public static final Color SEA_GREEN = Color.of(0x2E8B57);
    public static final Color FOREST_GREEN = Color.of(0x228B22);
    public static final Color DARK_GREEN = Color.of(0x006400);
    public static final Color YELLOW_GREEN = Color.of(0x9ACD32);
    public static final Color OLIVE_DRAB = Color.of(0x6B8E23);
    public static final Color DARK_OLIVE_GREEN = Color.of(0x556B2F);
    public static final Color MEDIUM_AQUAMARINE = Color.of(0x66CDAA);
    public static final Color DARK_SEA_GREEN = Color.of(0x8FBC8F);
    public static final Color LIGHT_SEA_GREEN = Color.of(0x20B2AA);
    public static final Color DARK_CYAN = Color.of(0x008B8B);
    public static final Color AZURE = Color.of(0xF0FFFF);
    public static final Color ALICE_BLUE = Color.of(0xF0F8FF);
    public static final Color LIGHT_CYAN = Color.of(0xE0FFFF);
    public static final Color CYAN = Color.of(0x00FFFF);
    public static final Color AQUAMARINE = Color.of(0x7FFFD4);
    public static final Color TURQUOISE = Color.of(0x40E0D0);
    public static final Color MEDIUM_TURQUOISE = Color.of(0x48D1CC);
    public static final Color DARK_TURQUOISE = Color.of(0x00CED1);
    public static final Color PALE_TURQUOISE = Color.of(0xAFEEEE);
    public static final Color POWDER_BLUE = Color.of(0xB0E0E6);
    public static final Color LIGHT_BLUE = Color.of(0xADD8E6);
    public static final Color LIGHT_STEEL_BLUE = Color.of(0xB0C4DE);
    public static final Color SKY_BLUE = Color.of(0x87CEEB);
    public static final Color LIGHT_SKY_BLUE = Color.of(0x87CEFA);
    public static final Color DEEP_SKY_BLUE = Color.of(0x00BFFF);
    public static final Color DODGER_BLUE = Color.of(0x1E90FF);
    public static final Color CORNFLOWER_BLUE = Color.of(0x6495ED);
    public static final Color MEDIUM_SLATE_BLUE = Color.of(0x7B68EE);
    public static final Color CADET_BLUE = Color.of(0x5F9EA0);
    public static final Color STEEL_BLUE = Color.of(0x4682B4);
    public static final Color ROYAL_BLUE = Color.of(0x4169E1);
    public static final Color MEDIUM_BLUE = Color.of(0x0000CD);
    public static final Color DARK_BLUE = Color.of(0x00008B);
    public static final Color MIDNIGHT_BLUE = Color.of(0x191970);
    public static final Color SLATE_BLUE = Color.of(0x6A5ACD);
    public static final Color DARK_SLATE_BLUE = Color.of(0x483D8B);
    public static final Color GHOST_WHITE = Color.of(0xF8F8FF);
    public static final Color LAVENDER = Color.of(0xE6E6FA);
    public static final Color THISTLE = Color.of(0xD8BFD8);
    public static final Color PLUM = Color.of(0xDDA0DD);
    public static final Color VIOLET = Color.of(0xEE82EE);
    public static final Color ORCHID = Color.of(0xDA70D6);
    public static final Color FUCHSIA = Color.of(0xFF00FF);
    public static final Color MEDIUM_VIOLET_RED = Color.of(0xC71585);
    public static final Color MEDIUM_ORCHID = Color.of(0xBA55D3);
    public static final Color MEDIUM_PURPLE = Color.of(0x9370DB);
    public static final Color BLUE_VIOLET = Color.of(0x8A2BE2);
    public static final Color DARK_VIOLET = Color.of(0x9400D3);
    public static final Color DARK_ORCHID = Color.of(0x9932CC);
    public static final Color DARK_MAGENTA = Color.of(0x8B008B);
    public static final Color INDIGO = Color.of(0x4B0082);
    public static final Color SNOW = Color.of(0xFFFAFA);
    public static final Color LAVENDER_BLUSH = Color.of(0xFFF0F5);
    public static final Color MISTY_ROSE = Color.of(0xFFE4E1);
    public static final Color PINK = Color.of(0xFFC0CB);
    public static final Color LIGHT_PINK = Color.of(0xFFB6C1);
    public static final Color HOT_PINK = Color.of(0xFF69B4);
    public static final Color DEEP_PINK = Color.of(0xFF1493);
    public static final Color PALE_VIOLET_RED = Color.of(0xDB7093);
    public static final Color ROSY_BROWN = Color.of(0xBC8F8F);

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

    public String toHexStringPretty() {
        String result = toHexString().toUpperCase();

        int l = result.length();

        if (l < 6) {
            result = "0".repeat(6 - l) + result;
        }

        return result;
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
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "[0x");

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

    public static Color of(String hex) {
        return new Color(hex, true);
    }


}
