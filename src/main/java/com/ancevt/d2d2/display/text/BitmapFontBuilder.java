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
package com.ancevt.d2d2.display.text;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Stage;

import java.io.InputStream;
import java.nio.file.Path;

public class BitmapFontBuilder {

    private static final int DEFAULT_ATLAS_WIDTH = 512;
    private static final int DEFAULT_ATLAS_HEIGHT = 512;
    private static final int DEFAULT_FONT_SIZE = 12;
    private static final int DEFAULT_SPACING_X = 5;
    private static final int DEFAULT_SPACING_Y = 5;
    private static final boolean DEFAULT_TEXT_ANTIALIAS_ON = true;
    private static final boolean DEFAULT_FRACTIONAL_METRICS_ON = true;

    private int atlasWidth = DEFAULT_ATLAS_WIDTH;
    private int atlasHeight = DEFAULT_ATLAS_HEIGHT;
    private InputStream ttfInputStream;
    private Path ttfPath;
    private int fontSize = DEFAULT_FONT_SIZE;
    private boolean textAntialiasOn = DEFAULT_TEXT_ANTIALIAS_ON;
    private boolean textAntialiasGasp;
    private boolean textAntialiasLcdHrgb;
    private boolean textAntialiasLcdHbgr;
    private boolean textAntialiasLcdVrgb;
    private boolean textAntialiasLcdVbgr;
    private boolean fractionalMetricsOn = DEFAULT_FRACTIONAL_METRICS_ON;
    private int spacingX = DEFAULT_SPACING_X;
    private int spacingY = DEFAULT_SPACING_Y;
    private boolean bold;
    private boolean italic;
    private int offsetY;
    private String name;

    private String charSourceString = " !\"#№$%&'()*+,-./\\0123456789:;<=>@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz[]_{}АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя?^~`ҐґЇїЎў";

    private byte[] pngBytes;
    private byte[] charsDataBytes;

    public int getAtlasWidth() {
        return atlasWidth;
    }

    public int getAtlasHeight() {
        return atlasHeight;
    }

    public String getName() {
        return name;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public InputStream getTtfInputStream() {
        return ttfInputStream;
    }

    public Path getTtfPath() {
        return ttfPath;
    }

    public int getFontSize() {
        return fontSize;
    }

    public boolean isTextAntialiasOn() {
        return textAntialiasOn;
    }

    public boolean isTextAntialiasGasp() {
        return textAntialiasGasp;
    }

    public boolean isTextAntialiasLcdHrgb() {
        return textAntialiasLcdHrgb;
    }

    public boolean isTextAntialiasLcdHbgr() {
        return textAntialiasLcdHbgr;
    }

    public boolean isTextAntialiasLcdVrgb() {
        return textAntialiasLcdVrgb;
    }

    public boolean isTextAntialiasLcdVbgr() {
        return textAntialiasLcdVbgr;
    }

    public boolean isFractionalMetricsOn() {
        return fractionalMetricsOn;
    }

    public int getSpacingX() {
        return spacingX;
    }

    public int getSpacingY() {
        return spacingY;
    }

    public String getCharSourceString() {
        return charSourceString;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public BitmapFontBuilder charSourceString(String charSourceString) {
        this.charSourceString = charSourceString;
        return this;
    }

    public BitmapFontBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BitmapFontBuilder bold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public BitmapFontBuilder offsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public BitmapFontBuilder ttfPath(String ttfPath) {
        return ttfPath(Path.of(ttfPath));
    }

    public BitmapFontBuilder ttfPath(Path ttfPath) {
        this.ttfPath = ttfPath;
        return this;
    }

    public BitmapFontBuilder atlasWidth(int atlasWidth) {
        this.atlasWidth = atlasWidth;
        return this;
    }

    public BitmapFontBuilder atlasHeight(int atlasHeight) {
        this.atlasHeight = atlasHeight;
        return this;
    }

    public BitmapFontBuilder ttfInputStream(InputStream ttfInputStream) {
        this.ttfInputStream = ttfInputStream;
        return this;
    }

    public BitmapFontBuilder fontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public BitmapFontBuilder textAntialiasOn(boolean textAntialiasOn) {
        this.textAntialiasOn = textAntialiasOn;
        return this;
    }

    public BitmapFontBuilder textAntialiasGasp(boolean textAntialiasGasp) {
        this.textAntialiasGasp = textAntialiasGasp;
        return this;
    }

    public BitmapFontBuilder textAntialiasLcdHrgb(boolean textAntialiasLcdHrgb) {
        this.textAntialiasLcdHrgb = textAntialiasLcdHrgb;
        return this;
    }

    public BitmapFontBuilder textAntialiasLcdHbgr(boolean textAntialiasLcdHbgr) {
        this.textAntialiasLcdHbgr = textAntialiasLcdHbgr;
        return this;
    }

    public BitmapFontBuilder textAntialiasLcdVrgb(boolean textAntialiasLcdVrgb) {
        this.textAntialiasLcdVrgb = textAntialiasLcdVrgb;
        return this;
    }

    public BitmapFontBuilder textAntialiasLcdVbgr(boolean textAntialiasLcdVbgr) {
        this.textAntialiasLcdVbgr = textAntialiasLcdVbgr;
        return this;
    }

    public BitmapFontBuilder fractionalMetricsOn(boolean fractionalMetricsOn) {
        this.fractionalMetricsOn = fractionalMetricsOn;
        return this;
    }

    public BitmapFontBuilder spacingX(int spacingX) {
        this.spacingX = spacingX;
        return this;
    }

    public BitmapFontBuilder spacingY(int spacingY) {
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

        return D2D2.getBackend().generateBitmapFont(this);
    }

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        InputStream inputStream = Assets.getAssetAsStream("d2d2ttf/NotoSansMono-Bold.ttf");

        BitmapFont bitmapFont = new BitmapFontBuilder()
            .ttfInputStream(inputStream)
            .fontSize(12)
            .spacingY(10)
            .offsetY(3)
            .build();


        BitmapText bitmapText = new BitmapText(bitmapFont);
        //bitmapText.setAutosize(true);
        bitmapText.setSize(200, 200);

        bitmapText.setText("""
                Copyright (C) 2022 the original author or authors.
                See the notice.md file distributed with this work for additional
                information regarding copyright ownership.""");


        PlainRect plainRect = new PlainRect(Color.BLACK);
        plainRect.setSize(bitmapText.getWidth(), bitmapText.getHeight());

        stage.add(plainRect, 100, 250);
        stage.add(bitmapText, 100, 250);



        D2D2.loop();
    }
}
























