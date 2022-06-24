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
import com.ancevt.d2d2.common.IDisposable;
import com.ancevt.d2d2.display.texture.TextureAtlas;

public class BitmapFont implements IDisposable {

    private static final int MAX_CHARS = 65536;

    private static BitmapFont defaultBitmapFont;
    private static final String BITMAP_FONTS_DIR = "bitmapfonts/";

    private final BitmapCharInfo[] charInfos;
    private final TextureAtlas textureAtlas;
    private final String name;

    private final boolean monospaced;

    private float paddingTop;
    private boolean disposed;


    BitmapFont(String name, TextureAtlas textureAtlas, BitmapCharInfo[] charInfos) {
        this.name = name;
        this.textureAtlas = textureAtlas;
        this.charInfos = charInfos;

        BitmapCharInfo[] charInfosToCheck = {
                charInfos['|'],
                charInfos['.'],
                charInfos['I'],
                charInfos['_'],
                charInfos['Ж'],
                charInfos['Щ'],
                charInfos['\''],
                charInfos['W'],
        };

        int width = charInfos['0'].width();

        boolean foundDifferent = false;

        for (BitmapCharInfo bitmapCharInfo : charInfosToCheck) {
            if(bitmapCharInfo != null) {
                if(bitmapCharInfo.width() != width) {
                    foundDifferent = true;
                    break;
                }
            }
        }

        monospaced = !foundDifferent;
    }

    public boolean isMonospaced() {
        return monospaced;
    }

    public final boolean isCharSupported(char c) {
        return getCharInfo(c) != null;
    }

    public final BitmapCharInfo getCharInfo(char c) {
        return charInfos[c];
    }

    public final int getZeroCharWidth() {
        return charInfos['0'].width();
    }

    public final int getZeroCharHeight() {
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

    @Override
    public void dispose() {
        disposed = true;
        D2D2.getTextureManager().unloadTextureAtlas(getTextureAtlas());
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }
}
