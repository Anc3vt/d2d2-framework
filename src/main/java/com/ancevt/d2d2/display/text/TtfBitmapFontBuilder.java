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
package com.ancevt.d2d2.display.text;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.backend.lwjgl.LwjglBackend;
import lombok.Getter;

import java.io.InputStream;
import java.nio.file.Path;

public class TtfBitmapFontBuilder {

    private static final int DEFAULT_ATLAS_WIDTH = 512;
    private static final int DEFAULT_ATLAS_HEIGHT = 512;
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final int DEFAULT_SPACING_X = 5;
    private static final int DEFAULT_SPACING_Y = 5;

    @Getter
    private int atlasWidth = DEFAULT_ATLAS_WIDTH;
    @Getter
    private int atlasHeight = DEFAULT_ATLAS_HEIGHT;
    @Getter
    private InputStream ttfInputStream;
    @Getter
    private Path ttfPath;
    @Getter
    private int fontSize = DEFAULT_FONT_SIZE;
    @Getter
    private boolean textAntialiasOn = true;
    @Getter
    private boolean textAntialiasGasp;
    @Getter
    private boolean textAntialiasLcdHrgb;
    @Getter
    private boolean textAntialiasLcdHbgr;
    @Getter
    private boolean textAntialiasLcdVrgb;
    @Getter
    private boolean textAntialiasLcdVbgr;
    @Getter
    private FractionalMetrics fractionalMetrics;
    @Getter
    private int spacingX = DEFAULT_SPACING_X;
    @Getter
    private int spacingY = DEFAULT_SPACING_Y;
    @Getter
    private boolean bold;
    @Getter
    private boolean italic;
    @Getter
    private int offsetY;
    @Getter
    private String name;

    @Getter
        //private String charSourceString = " !\"#№$%&'()*+,-./\\0123456789:;<=>@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[]_{}АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя?^~`ҐґЇїЎў";
        private String charSourceString = " !\"#$%&'()*+,-./\\0123456789:;<=>@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[]_{}АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя?^~`ҐґЇїЎў";

    public TtfBitmapFontBuilder charSourceString(String charSourceString) {
        this.charSourceString = charSourceString;
        return this;
    }

    public TtfBitmapFontBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TtfBitmapFontBuilder bold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public TtfBitmapFontBuilder offsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public TtfBitmapFontBuilder ttfPath(String ttfPath) {
        return ttfPath(Path.of(ttfPath));
    }

    public TtfBitmapFontBuilder ttfPath(Path ttfPath) {
        this.ttfPath = ttfPath;
        return this;
    }

    public TtfBitmapFontBuilder atlasWidth(int atlasWidth) {
        this.atlasWidth = atlasWidth;
        return this;
    }

    public TtfBitmapFontBuilder atlasHeight(int atlasHeight) {
        this.atlasHeight = atlasHeight;
        return this;
    }

    public TtfBitmapFontBuilder ttfAssetPath(String assetPath) {
        return ttfInputStream(Assets.getAssetAsStream(assetPath));
    }

    public TtfBitmapFontBuilder ttfInputStream(InputStream ttfInputStream) {
        this.ttfInputStream = ttfInputStream;
        return this;
    }

    public TtfBitmapFontBuilder fontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public TtfBitmapFontBuilder textAntialias(boolean textAntialiasOn) {
        this.textAntialiasOn = textAntialiasOn;
        return this;
    }

    public TtfBitmapFontBuilder withHintTextAntialiasGasp() {
        this.textAntialiasGasp = true;
        return this;
    }

    public TtfBitmapFontBuilder withHintTextAntialiasLcdHrgb() {
        this.textAntialiasLcdHrgb = true;
        return this;
    }

    public TtfBitmapFontBuilder withHintTextAntialiasLcdHbgr() {
        this.textAntialiasLcdHbgr = true;
        return this;
    }

    public TtfBitmapFontBuilder withHintTextAntialiasLcdVrgb() {
        this.textAntialiasLcdVrgb = true;
        return this;
    }

    public TtfBitmapFontBuilder withHintTextAntialiasLcdVbgr() {
        this.textAntialiasLcdVbgr = true;
        return this;
    }

    public TtfBitmapFontBuilder fractionalMetrics(FractionalMetrics fractionalMetrics) {
        this.fractionalMetrics = fractionalMetrics;
        return this;
    }

    public FractionalMetrics fractionalMetrics() {
        return fractionalMetrics;
    }

    public TtfBitmapFontBuilder spacingX(int spacingX) {
        this.spacingX = spacingX;
        return this;
    }

    public TtfBitmapFontBuilder spacingY(int spacingY) {
        this.spacingY = spacingY;
        return this;
    }

    public BitmapFont build() {
        if (ttfPath == null && ttfInputStream == null) {
            throw new IllegalStateException("ttfPath == null && ttfInputStream == null");
        }

        if (name == null) {
            if (ttfPath != null) {
                name = ttfPath.toString();
            }
            if (ttfInputStream != null) {
                name = ttfInputStream.toString();
            }
        }

        return D2D2.backend().generateBitmapFont(this);
    }

    public static void main(String[] args) {
        D2D2.init(new LwjglBackend(1000, 600, "(floating)"));

        //InputStream inputStream = Assets.getAssetAsStream("d2d2ttf/PressStart2P-Regular.ttf");

        String fontPath = "d2d2ttf/terminus/TerminusTTF-Bold-4.49.3.ttf";

        int size = 16;

        createBitmapText("The quick brown fox jumps over the lazy dog абвгдежз АБВГДЕЖЗ",
            new TtfBitmapFontBuilder()
                .ttfAssetPath(fontPath)
                .fontSize(16)
                .spacingY(10)
                .textAntialias(true)
                .fractionalMetrics(FractionalMetrics.OFF)
                .offsetY(5)
                .build());

        createBitmapText("The quick brown fox jumps over the lazy dog абвгдежз АБВГДЕЖЗ",
            new TtfBitmapFontBuilder()
                .ttfAssetPath(fontPath)
                .fontSize(16)
                .spacingY(10)
                .textAntialias(false)
                .fractionalMetrics(FractionalMetrics.OFF)
                .offsetY(5)
                .build());

        D2D2.loop();
    }

    private static float _y = 0;

    private static BitmapText createBitmapText(String text, BitmapFont bitmapFont) {
        BitmapText bitmapText = new BitmapText(bitmapFont);
        bitmapText.setAutosize(true);
        bitmapText.setText(text);
        D2D2.stage().add(bitmapText, 50, _y + 10);
        _y += bitmapText.getHeight() + 5;
        return bitmapText;
    }
}
