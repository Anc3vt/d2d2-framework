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
package com.ancevt.d2d2.display;

import java.util.Objects;
import java.util.Random;

public class Color {
    public static final Color NO_COLOR              = null;
    public static final Color ALICE_BLUE            = new   Color(0xF0F8FF, true);
    public static final Color ANTIQUE_WHITE         = new   Color(0xFAEBD7, true);
    public static final Color AQUA                  = new   Color(0x00FFFF, true);
    public static final Color AQUAMARINE            = new   Color(0x7FFFD4, true);
    public static final Color AZURE                 = new   Color(0xF0FFFF, true);
    public static final Color BEIGE                 = new   Color(0xF5F5DC, true);
    public static final Color BISQUE                = new   Color(0xFFE4C4, true);
    public static final Color BLACK                 = new   Color(0x000000, true);
    public static final Color BLANCHED_ALMOND       = new   Color(0xFFEBCD, true);
    public static final Color BLUE                  = new   Color(0x0000FF, true);
    public static final Color BLUE_VIOLET           = new   Color(0x8A2BE2, true);
    public static final Color BROWN                 = new   Color(0xA52A2A, true);
    public static final Color BURLY_WOOD            = new   Color(0xDEB887, true);
    public static final Color CADET_BLUE            = new   Color(0x5F9EA0, true);
    public static final Color CHARTREUSE            = new   Color(0x7FFF00, true);
    public static final Color CHOCOLATE             = new   Color(0xD2691E, true);
    public static final Color CORAL                 = new   Color(0xFF7F50, true);
    public static final Color CORNFLOWER_BLUE       = new   Color(0x6495ED, true);
    public static final Color CORNSILK              = new   Color(0xFFF8DC, true);
    public static final Color CRIMSON               = new   Color(0xDC143C, true);
    public static final Color CYAN                  = new   Color(0x00FFFF, true);
    public static final Color DARK_BLUE             = new   Color(0x00008B / 2, true);
    public static final Color DARK_CYAN             = new   Color(0x008B8B / 2, true);
    public static final Color DARKGOLDEN_ROD        = new   Color(0xB8860B / 2, true);
    public static final Color DARK_GRAY             = new   Color(0x222222 / 2, true);
    public static final Color DARK_GREY             = new   Color(0x222222 / 2, true);
    public static final Color DARK_GREEN            = new   Color(0x006400 / 2, true);
    public static final Color DARK_KHAKI            = new   Color(0xBDB76B / 2, true);
    public static final Color DARK_MAGENTA          = new   Color(0x8B008B / 2, true);
    public static final Color DARK_OLIVEGREEN       = new   Color(0x556B2F / 2, true);
    public static final Color DARK_ORANGE           = new   Color(0xFF8C00 / 2, true);
    public static final Color DARK_ORCHID           = new   Color(0x9932CC / 2, true);
    public static final Color DARK_RED              = new   Color(0x8B0000 / 2, true);
    public static final Color DARK_SALMON           = new   Color(0xE9967A / 2, true);
    public static final Color DARK_SEAGREEN         = new   Color(0x8FBC8F / 2, true);
    public static final Color DARK_SLATEBLUE        = new   Color(0x483D8B / 2, true);
    public static final Color DARK_SLATEGRAY        = new   Color(0x2F4F4F / 2, true);
    public static final Color DARK_SLATEGREY        = new   Color(0x2F4F4F / 2, true);
    public static final Color DARK_TURQUOISE        = new   Color(0x00CED1 / 2, true);
    public static final Color DARK_VIOLET           = new   Color(0x9400D3, true);
    public static final Color DEEP_PINK             = new   Color(0xFF1493, true);
    public static final Color DEEP_SKY_BLUE         = new   Color(0x00BFFF, true);
    public static final Color DIM_GRAY              = new   Color(0x696969, true);
    public static final Color DIM_GREY              = new   Color(0x696969, true);
    public static final Color DODGER_BLUE           = new   Color(0x1E90FF, true);
    public static final Color FIRE_BRICK            = new   Color(0xB22222, true);
    public static final Color FLORAL_WHITE          = new   Color(0xFFFAF0, true);
    public static final Color FOREST_GREEN          = new   Color(0x228B22, true);
    public static final Color FUCHSIA               = new   Color(0xFF00FF, true);
    public static final Color GAINSBORO             = new   Color(0xDCDCDC, true);
    public static final Color GHOST_WHITE           = new   Color(0xF8F8FF, true);
    public static final Color GOLD                  = new   Color(0xFFD700, true);
    public static final Color GOLDEN_ROD            = new   Color(0xDAA520, true);
    public static final Color GRAY                  = new   Color(0x808080, true);
    public static final Color GREY                  = new   Color(0x808080, true);
    public static final Color GREEN                 = new   Color(0x008000, true);
    public static final Color GREEN_YELLOW          = new   Color(0xADFF2F, true);
    public static final Color HONEY_DEW             = new   Color(0xF0FFF0, true);
    public static final Color HOT_PINK              = new   Color(0xFF69B4, true);
    public static final Color INDIAN_RED            = new   Color(0xCD5C5C, true);
    public static final Color INDIGO                = new   Color(0x4B0082, true);
    public static final Color IVORY                 = new   Color(0xFFFFF0, true);
    public static final Color KHAKI                 = new   Color(0xF0E68C, true);
    public static final Color LAVENDER              = new   Color(0xE6E6FA, true);
    public static final Color LAVENDERBLUSH         = new   Color(0xFFF0F5, true);
    public static final Color LAWNGREEN             = new   Color(0x7CFC00, true);
    public static final Color LEMON_CHIFFON         = new   Color(0xFFFACD, true);
    public static final Color LIGHT_BLUE            = new   Color(0xADD8E6, true);
    public static final Color LIGHT_CORAL           = new   Color(0xF08080, true);
    public static final Color LIGHT_CYAN            = new   Color(0xE0FFFF, true);
    public static final Color LIGHT_GOLDENRODYELLOW = new   Color(0xFAFAD2, true);
    public static final Color LIGHT_GRAY            = new   Color(0xD3D3D3, true);
    public static final Color LIGHT_GREY            = new   Color(0xD3D3D3, true);
    public static final Color LIGHT_GREEN           = new   Color(0x90EE90, true);
    public static final Color LIGHT_PINK            = new   Color(0xFFB6C1, true);
    public static final Color LIGHT_SALMON          = new   Color(0xFFA07A, true);
    public static final Color LIGHT_SEAGREEN        = new   Color(0x20B2AA, true);
    public static final Color LIGHT_SKYBLUE         = new   Color(0x87CEFA, true);
    public static final Color LIGHT_SLATEGRAY       = new   Color(0x778899, true);
    public static final Color LIGHT_SLATEGREY       = new   Color(0x778899, true);
    public static final Color LIGHT_STEELBLUE       = new   Color(0xB0C4DE, true);
    public static final Color LIGHT_YELLOW          = new   Color(0xFFFFE0, true);
    public static final Color LIME                  = new   Color(0x00FF00, true);
    public static final Color LIMEGREEN             = new   Color(0x32CD32, true);
    public static final Color LINEN                 = new   Color(0xFAF0E6, true);
    public static final Color MAGENTA               = new   Color(0xFF00FF, true);
    public static final Color MAROON                = new   Color(0x800000, true);
    public static final Color MEDIUM_AQUAMARINE     = new   Color(0x66CDAA, true);
    public static final Color MEDIUM_BLUE           = new   Color(0x0000CD, true);
    public static final Color MEDIUM_ORCHID         = new   Color(0xBA55D3, true);
    public static final Color MEDIUM_PURPLE         = new   Color(0x9370DB, true);
    public static final Color MEDIUM_SEAGREEN       = new   Color(0x3CB371, true);
    public static final Color MEDIUM_SLATEBLUE      = new   Color(0x7B68EE, true);
    public static final Color MEDIUM_SPRINGGREEN    = new   Color(0x00FA9A, true);
    public static final Color MEDIUM_TURQUOISE      = new   Color(0x48D1CC, true);
    public static final Color MEDIUM_VIOLETRED      = new   Color(0xC71585, true);
    public static final Color MIDNIGHT_BLUE         = new   Color(0x191970, true);
    public static final Color MINT_CREAM            = new   Color(0xF5FFFA, true);
    public static final Color MISTY_ROSE            = new   Color(0xFFE4E1, true);
    public static final Color MOCCASIN              = new   Color(0xFFE4B5, true);
    public static final Color NAVAJO_WHITE          = new   Color(0xFFDEAD, true);
    public static final Color NAVY                  = new   Color(0x000080, true);
    public static final Color OLD_LACE              = new   Color(0xFDF5E6, true);
    public static final Color OLIVE                 = new   Color(0x808000, true);
    public static final Color OLIVE_DRAB            = new   Color(0x6B8E23, true);
    public static final Color ORANGE                = new   Color(0xFFA500, true);
    public static final Color ORANGE_RED            = new   Color(0xFF4500, true);
    public static final Color ORCHID                = new   Color(0xDA70D6, true);
    public static final Color PALE_GOLDEN_ROD       = new   Color(0xEEE8AA, true);
    public static final Color PALE_GREEN            = new   Color(0x98FB98, true);
    public static final Color PALE_TURQUOISE        = new   Color(0xAFEEEE, true);
    public static final Color PALE_VIOLET_RED       = new   Color(0xDB7093, true);
    public static final Color PAPAYA_WHIP           = new   Color(0xFFEFD5, true);
    public static final Color PEACH_PUFF            = new   Color(0xFFDAB9, true);
    public static final Color PERU                  = new   Color(0xCD853F, true);
    public static final Color PINK                  = new   Color(0xFFC0CB, true);
    public static final Color PLUM                  = new   Color(0xDDA0DD, true);
    public static final Color POWDER_BLUE           = new   Color(0xB0E0E6, true);
    public static final Color PURPLE                = new   Color(0x800080, true);
    public static final Color REBECCA_PURPLE        = new   Color(0x663399, true);
    public static final Color RED                   = new   Color(0xFF0000, true);
    public static final Color ROSY_BROWN            = new   Color(0xBC8F8F, true);
    public static final Color ROYAL_BLUE            = new   Color(0x4169E1, true);
    public static final Color SADDLE_BROWN          = new   Color(0x8B4513, true);
    public static final Color SALMON                = new   Color(0xFA8072, true);
    public static final Color SANDY_BROWN           = new   Color(0xF4A460, true);
    public static final Color SEA_GREEN             = new   Color(0x2E8B57, true);
    public static final Color SEA_SHELL             = new   Color(0xFFF5EE, true);
    public static final Color SIENNA                = new   Color(0xA0522D, true);
    public static final Color SILVER                = new   Color(0xC0C0C0, true);
    public static final Color SKY_BLUE              = new   Color(0x87CEEB, true);
    public static final Color SLATE_BLUE            = new   Color(0x6A5ACD, true);
    public static final Color SLATE_GRAY            = new   Color(0x708090, true);
    public static final Color SLATE_GREY            = new   Color(0x708090, true);
    public static final Color SNOW                  = new   Color(0xFFFAFA, true);
    public static final Color SPRING_GREEN          = new   Color(0x00FF7F, true);
    public static final Color STEEL_BLUE            = new   Color(0x4682B4, true);
    public static final Color TAN                   = new   Color(0xD2B48C, true);
    public static final Color TEAL                  = new   Color(0x008080, true);
    public static final Color THISTLE               = new   Color(0xD8BFD8, true);
    public static final Color TOMATO                = new   Color(0xFF6347, true);
    public static final Color TURQUOISE             = new   Color(0x40E0D0, true);
    public static final Color VIOLET                = new   Color(0xEE82EE, true);
    public static final Color WHEAT                 = new   Color(0xF5DEB3, true);
    public static final Color WHITE                 = new   Color(0xFFFFFF, true);
    public static final Color WHITE_SMOKE           = new   Color(0xF5F5F5, true);
    public static final Color YELLOW                = new   Color(0xFFFF00, true);
    public static final Color YELLOW_GREEN          = new   Color(0x9ACD32, true);

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
