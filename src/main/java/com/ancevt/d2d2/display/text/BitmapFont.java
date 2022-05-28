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
import com.ancevt.d2d2.display.texture.TextureAtlas;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BitmapFont {

    private static final int MAX_CHARS = 65536;

    private static BitmapFont defaultBitmapFont;
    private static final String BITMAP_FONTS_DIR = "bitmapfonts/";

    private final BitmapCharInfo[] charInfos;
    private final TextureAtlas textureAtlas;


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

    public static BitmapFont loadBitmapFont(String bmfAssetPath) {
        BitmapFont fromCache = cache.get(bmfAssetPath);

        if(fromCache != null) {
            return fromCache;
        }

        try (DataInputStream dataInputStream = new DataInputStream(Assets.getAssetAsStream(BITMAP_FONTS_DIR + bmfAssetPath))) {

            BitmapCharInfo[] charInfos = new BitmapCharInfo[MAX_CHARS];

            int metaSize = dataInputStream.readUnsignedShort();

            while (metaSize > 0) {
                char character = dataInputStream.readChar();
                int x = dataInputStream.readUnsignedShort();
                int y = dataInputStream.readUnsignedShort();
                int width = dataInputStream.readUnsignedShort();
                int height = dataInputStream.readUnsignedShort();

                BitmapCharInfo bitmapCharInfo = new BitmapCharInfo(character, x, y, width, height);

                charInfos[character] = bitmapCharInfo;

                metaSize -= Character.BYTES;
                metaSize -= Short.BYTES;
                metaSize -= Short.BYTES;
                metaSize -= Short.BYTES;
                metaSize -= Short.BYTES;

                //Trace.trace("c,x,y,w,h", character, x, y, width, height);
            }

            // Passing rest of the data to loadTextureAtlas. PNG input stream expected
            BitmapFont result = new BitmapFont(D2D2.getTextureManager().loadTextureAtlas(dataInputStream), charInfos);

            cache.put(bmfAssetPath, result);

            return result;
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}

















