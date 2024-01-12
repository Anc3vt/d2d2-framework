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
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.common.BorderedRect;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapFontBuilder;
import com.ancevt.d2d2.display.text.BitmapText;

import java.io.InputStream;

public class Tests_BitmapText_cacheAsSprite2 {

    private static final float DEFAULT_WIDTH = 590.0f;
    private static final float DEFAULT_HEIGHT = 600.0f;
    private static final float PADDING = 5.0f;

    public static final Color DEFAULT_BG_COLOR = Color.of(0x002200);
    private static final Color DEFAULT_BORDER_COLOR = Color.YELLOW;
    private static BorderedRect bg1, bg2;

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(1500, 600, "(floating)"));

        String text = """
            #<CCCCCC>position: 0 of 3902; HEXADECIMAL <00FF00>UNSIGNED <CCCCCC>
            <FFFFFF>---------------------------------------------------------------------------------
            <000000> 00 <999999> 88 <999999> 88 <999999> 88 <FFFFFF> 88 <999999> 33 <999999> 36 <999999> 36 <FFFFFF> 37 <999999> 37 <999999> 30 <999999> 32 <FFFFFF> 38 <999999> 31 <999999> 38 <999999> 37 <FFFFFF> 33 <999999> 33 <999999> 34\s
            <FFFFFF> 37 <999999> 34 <999999> 68 <999999> 65 <FFFFFF> 6C <999999> 6C <999999> 6F <999999> 20 <FFFFFF> 77 <999999> 6F <999999> 72 <999999> 6C <FFFFFF> 64 <999999> 20 <999999> D0 <999999> AF <FFFFFF> D0 <999999> AF <999999> D0\s
            <FFFFFF> AF <999999> D0 <999999> AF <999999> 30 <FFFFFF> 2E <999999> 31 <999999> 33 <999999> 36 <FFFFFF> 36 <999999> 37 <999999> 37 <999999> 30 <FFFFFF> 32 <999999> 38 <999999> 31 <999999> 38 <FFFFFF> 37 <999999> 33 <999999> 33\s
            <FFFFFF> 34 <999999> 37 <999999> 34 <999999> 68 <FFFFFF> 65 <999999> 6C <999999> 6C <999999> 6F <FFFFFF> 20 <999999> 77 <999999> 6F <999999> 72 <FFFFFF> 6C <999999> 64 <999999> 20 <999999> D0 <FFFFFF> AF <999999> D0 <999999> AF\s
            <FFFFFF> D0 <999999> AF <999999> D0 <999999> AF <FFFFFF> 30 <999999> 2E <999999> 31 <999999> 33 <FFFFFF> 36 <999999> 36 <999999> 37 <999999> 37 <FFFFFF> 30 <999999> 32 <999999> 38 <999999> 31 <FFFFFF> 38 <999999> 37 <999999> 33\s
            <FFFFFF> 33 <999999> 34 <999999> 37 <999999> 34 <FFFFFF> 68 <999999> 65 <999999> 6C <999999> 6C <FFFFFF> 6F <999999> 20 <999999> 77 <999999> 6F <FFFFFF> 72 <999999> 6C <999999> 64 <999999> 20 <FFFFFF> D0 <999999> AF <999999> D0\s
            <FFFFFF> AF <999999> D0 <999999> AF <999999> D0 <FFFFFF> AF <999999> 30 <999999> 2E <999999> 31 <FFFFFF> 33 <999999> 36 <999999> 36 <999999> 37 <FFFFFF> 37 <999999> 30 <999999> 32 <999999> 38 <FFFFFF> 31 <999999> 38 <999999> 37\s
            <FFFFFF> 33 <999999> 33 <999999> 34 <999999> 37 <FFFFFF> 34 <999999> 68 <999999> 65 <999999> 6C <FFFFFF> 6C <999999> 6F <999999> 20 <999999> 77 <FFFFFF> 6F <999999> 72 <999999> 6C <999999> 64 <FFFFFF> 20 <999999> D0 <999999> AF\s
            <FFFFFF> D0 <999999> AF <999999> D0 <999999> AF <FFFFFF> D0 <999999> AF <999999> 30 <999999> 2E <FFFFFF> 31 <999999> 33 <999999> 36 <999999> 36 <FFFFFF> 37 <999999> 37 <999999> 30 <999999> 32 <FFFFFF> 38 <999999> 31 <999999> 38\s
            <FFFFFF> 37 <999999> 33 <999999> 33 <999999> 34 <FFFFFF> 37 <999999> 34 <999999> 68 <999999> 65 <FFFFFF> 6C <999999> 6C <999999> 6F <999999> 20 <FFFFFF> 77 <999999> 6F <999999> 72 <999999> 6C <FFFFFF> 64 <999999> 20 <999999> D0\s
            <FFFFFF> AF <999999> D0 <999999> AF <999999> D0 <FFFFFF> AF <999999> D0 <999999> AF <999999> 30 <FFFFFF> 2E <999999> 31 <999999> 33 <999999> 36 <FFFFFF> 36 <999999> 37 <999999> 37 <999999> 30 <FFFFFF> 32 <999999> 38 <999999> 31\s
            <FFFFFF> 38 <999999> 37 <999999> 33 <999999> 33 <FFFFFF> 34 <999999> 37 <999999> 34 <999999> 68 <FFFFFF> 65 <999999> 6C <999999> 6C <999999> 6F <FFFFFF> 20 <999999> 77 <999999> 6F <999999> 72 <FFFFFF> 6C <999999> 64 <999999> 20\s
            <FFFFFF> D0 <999999> AF <999999> D0 <999999> AF <FFFFFF> D0 <999999> AF <999999> D0 <999999> AF <FFFFFF> 30 <999999> 2E <999999> 31 <999999> 33 <FFFFFF> 36 <999999> 36 <999999> 37 <999999> 37 <FFFFFF> 30 <999999> 32 <999999> 38\s
            <FFFFFF> 31 <999999> 38 <999999> 37 <999999> 33 <FFFFFF> 33 <999999> 34 <999999> 37 <999999> 34 <FFFFFF> 68 <999999> 65 <999999> 6C <999999> 6C <FFFFFF> 6F <999999> 20 <999999> 77 <999999> 6F <FFFFFF> 72 <999999> 6C <999999> 64\s
            <FFFFFF> 20 <999999> D0 <999999> AF <999999> D0 <FFFFFF> AF <999999> D0 <999999> AF <999999> D0 <FFFFFF> AF <999999> 30 <999999> 2E <999999> 31 <FFFFFF> 33 <999999> 36 <999999> 36 <999999> 37 <FFFFFF> 37 <999999> 30 <999999> 32\s
            <FFFFFF> 38 <999999> 31 <999999> 38 <999999> 37 <FFFFFF> 33 <999999> 33 <999999> 34 <999999> 37 <FFFFFF> 34 <999999> 68 <999999> 65 <999999> 6C <FFFFFF> 6C <999999> 6F <999999> 20 <999999> 77 <FFFFFF> 6F <999999> 72 <999999> 6C\s
            <FFFFFF> 64 <999999> 20 <999999> D0 <999999> AF <FFFFFF> D0 <999999> AF <999999> D0 <999999> AF <FFFFFF> D0 <999999> AF <999999> 30 <999999> 2E <FFFFFF> 31 <999999> 33 <999999> 36 <999999> 36 <FFFFFF> 37 <999999> 37 <999999> 30\s
            <FFFFFF>---------------------------------------------------------------------------------
             ����3667702818733474hello world ЯЯЯЯ0.13667702818733474hello world ЯЯЯЯ0.13667702818733474hello world ЯЯЯЯ0.13667702818733474hello world ЯЯЯЯ0.13667702818733474hello world ЯЯЯЯ0.13667702818733474hello world ЯЯЯЯ0.13667702818733474hello world ЯЯЯЯ0.13667702818733474hello world ЯЯЯЯ0.1366770""";

        String text2 = "#<FFFF00> HELLO<FFFF00> wordl!\n\n line <CCCCCC>line2\n<FFFFFF>HHHHEEEEELLLLooooo 123www";


        bg1 = new BorderedRect(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_BG_COLOR, DEFAULT_BORDER_COLOR);
        stage.add(bg1, 100, 100);

        bg2 = new BorderedRect(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_BG_COLOR, DEFAULT_BORDER_COLOR);
        stage.add(bg2, 700, 100);

        InputStream inputStream = Assets.getAssetAsStream("d2d2ttf/NotoSansMono-SemiCondensedBold.ttf");
        BitmapFont bitmapFont = new BitmapFontBuilder()
            .ttfInputStream(inputStream)
            .fontSize(12)
            .spacingY(3)
            .offsetY(2)
            .fractionalMetricsOn(false)
            .build();

        BitmapText bitmapText = new BitmapText();
        bitmapText.setBitmapFont(bitmapFont);
        bitmapText.setText(text);
        bitmapText.setMulticolorEnabled(true);
        bitmapText.setSize(DEFAULT_WIDTH - PADDING * 2, DEFAULT_HEIGHT - PADDING * 2);

        stage.add(bitmapText, PADDING, PADDING);
        stage.add(bitmapText, bg1.getX() + PADDING, bg1.getY() + PADDING);

        Sprite sprite = bitmapText.toSprite();
        stage.add(sprite, bg2.getX() + PADDING, bg2.getY() + PADDING);


        stage.add(new FpsMeter());
        D2D2.loop();
    }

}
