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
package com.ancevt.d2d2.display.text;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.texture.TextureAtlas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static com.ancevt.d2d2.D2D2.init;
import static com.ancevt.d2d2.D2D2.loop;
import static java.lang.Integer.parseInt;

public class BitmapFont {

    private static final int MAX_CHARS = 65536;

    private static BitmapFont defaultBitmapFont;
    private static final String BITMAP_FONTS_DIR = "bitmapfonts/";

    private final BitmapCharInfo[] charInfos;
    private final TextureAtlas textureAtlas;

    private float paddingTop;


    private BitmapFont(TextureAtlas textureAtlas, BitmapCharInfo[] charInfos) {
        this.textureAtlas = textureAtlas;
        this.charInfos = charInfos;
    }

    public final boolean isCharSupported(char c) {
        return getCharInfo(c) != null;
    }

    public final BitmapCharInfo getCharInfo(char c) {
        return charInfos[c];
    }

    public final int getCharHeight() {
        return charInfos['0'].height();
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    @Override
    public String toString() {
        return "BitmapFont{" +
                ", textureAtlas=" + textureAtlas +
                '}';
    }

    // ------------------- Loading stuff: ---------------------------------------------

    private static final Map<String, BitmapFont> cache = new HashMap<>();

    public static void setDefaultBitmapFont(final BitmapFont bitmapFont) {
        defaultBitmapFont = bitmapFont;
    }

    public static BitmapFont getDefaultBitmapFont() {
        return defaultBitmapFont;
    }

    public static void loadDefaultBitmapFont(String assetPath) {
        BitmapFont.setDefaultBitmapFont(BitmapFont.loadBitmapFont(assetPath));
    }

    public static BitmapFont loadBitmapFont(InputStream dataInputStream, InputStream pngInputStream) {
        BitmapCharInfo[] charInfos = new BitmapCharInfo[MAX_CHARS];

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(line);
                char c = line.charAt(0) == ' ' ? ' ' : stringTokenizer.nextToken().charAt(0);
                charInfos[c] = new BitmapCharInfo(
                        c,
                        parseInt(stringTokenizer.nextToken()),
                        parseInt(stringTokenizer.nextToken()),
                        parseInt(stringTokenizer.nextToken()),
                        parseInt(stringTokenizer.nextToken())
                );
            }

            charInfos['\n'] = new BitmapCharInfo(
                    '\n',
                    charInfos[' '].x(),
                    charInfos[' '].y(),
                    0,
                    charInfos[' '].height()
            );

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        return new BitmapFont(D2D2.getTextureManager().loadTextureAtlas(pngInputStream), charInfos);
    }

    public static BitmapFont loadBitmapFont(String assetWithoutExtension) {
        BitmapFont fromCache = cache.get(assetWithoutExtension);

        if (fromCache != null) {
            return fromCache;
        }

        BitmapFont bitmapFont = loadBitmapFont(
                Assets.getAssetAsStream(BITMAP_FONTS_DIR + assetWithoutExtension + ".bmf"),
                Assets.getAssetAsStream(BITMAP_FONTS_DIR + assetWithoutExtension + ".png")
        );

        cache.put(assetWithoutExtension, bitmapFont);

        return bitmapFont;
    }

    public static void main(String[] args) {
        Stage stage = init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        FpsMeter fpsMeter = new FpsMeter();

        stage.add(fpsMeter);

        BitmapText bitmapText = new BitmapText(BitmapFont.loadBitmapFont("terminus/Terminus-16-Bold"));
        bitmapText.setMulticolorEnabled(true);
        bitmapText.setText("#000Hello\n<FFFF00>world");
        stage.add(bitmapText, 100, 250);

        loop();
    }
}
