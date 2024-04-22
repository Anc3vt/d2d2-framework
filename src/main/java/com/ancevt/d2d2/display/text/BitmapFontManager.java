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
import com.ancevt.util.args.Args;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

public class BitmapFontManager {

    private static final int MAX_CHARS = 65536;

    private static final String BITMAP_FONTS_ASSET_DIR = "bitmapfonts/";
    private static final String DEFAULT_BITMAP_FONT = "terminus/Terminus-12";

    private final Map<String, BitmapFont> bitmapFontMap;

    @Getter
    private final BitmapFont defaultBitmapFont;

    public BitmapFontManager() {
        bitmapFontMap = new HashMap<>();

        defaultBitmapFont = loadBitmapFont(DEFAULT_BITMAP_FONT);
    }

    private static String convertToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public BitmapFont loadBitmapFont(InputStream charsDataInputStream, InputStream pngInputStream, String name) {
        BitmapCharInfo[] charInfos = new BitmapCharInfo[MAX_CHARS];
        int spacingX = 0;
        int spacingY = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(charsDataInputStream))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                if (line.startsWith("#meta")) {
                    Args meta = Args.of(line);
                    spacingX = meta.get(int.class, "spacingX", 0);
                    spacingY = meta.get(int.class, "spacingY", 0);
                    continue;
                }

                StringTokenizer stringTokenizer = new StringTokenizer(line);
                char c = line.charAt(0) == ' ' ? ' ' : stringTokenizer.nextToken().charAt(0);
                charInfos[c] = new BitmapCharInfo(
                    c,
                    parseInt(stringTokenizer.nextToken()),
                    parseInt(stringTokenizer.nextToken()),
                    parseInt(stringTokenizer.nextToken()) + spacingX,
                    parseInt(stringTokenizer.nextToken()) + spacingY
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

        BitmapFont bitmapFont = new BitmapFont(name, D2D2.textureManager().loadTextureAtlas(pngInputStream), charInfos);

        bitmapFontMap.put(name, bitmapFont);

        return bitmapFont;
    }

    public BitmapFont loadBitmapFont(String assetWithoutExtension) {
        return loadBitmapFont(assetWithoutExtension, false);
    }

    public BitmapFont loadBitmapFont(String assetWithoutExtension, boolean forceUpdate) {
        if (!forceUpdate) {
            BitmapFont fromCache = bitmapFontMap.get(assetWithoutExtension);

            if (fromCache != null) {
                return fromCache;
            }
        }

        BitmapFont bitmapFont = loadBitmapFont(
            Assets.getAssetAsStream(BITMAP_FONTS_ASSET_DIR + assetWithoutExtension + ".bmf"),
            Assets.getAssetAsStream(BITMAP_FONTS_ASSET_DIR + assetWithoutExtension + ".png"),
            assetWithoutExtension
        );

        bitmapFontMap.put(assetWithoutExtension, bitmapFont);

        return bitmapFont;
    }

    public void remove(String name) {
        BitmapFont bitmapFont = bitmapFontMap.remove(name);
        if (bitmapFont != null) {
            bitmapFont.dispose();
        }
    }

    public Map<String, BitmapFont> getBitmapFonts() {
        return Map.copyOf(bitmapFontMap);
    }


}
