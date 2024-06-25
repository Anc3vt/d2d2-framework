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
import com.ancevt.d2d2.common.Disposable;
import com.ancevt.d2d2.display.texture.Texture;

public class Font implements Disposable {

    private final BitmapCharInfo[] charInfos;
    private final Texture texture;
    private final String name;

    private final boolean monospaced;

    private float paddingTop;
    private boolean disposed;

    Font(String name, Texture texture, BitmapCharInfo[] charInfos) {
        this.name = name;
        this.texture = texture;
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
            if (bitmapCharInfo != null) {
                if (bitmapCharInfo.width() != width) {
                    foundDifferent = true;
                    break;
                }
            }
        }

        monospaced = !foundDifferent;
    }

    public float computeTextWidth(String text, float spacing) {
        float sum = 0.0f;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            BitmapCharInfo bitmapCharInfo = charInfos[c];
            if (bitmapCharInfo != null) {
                sum += bitmapCharInfo.width() + spacing;
            }

        }

        return sum;
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

    public Texture getTexture() {
        return texture;
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
                "name = " + name +
                ", texture=" + texture +
                '}';
    }

    @Override
    public void dispose() {
        disposed = true;
        D2D2.textureManager().unloadTexture(getTexture());
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }
}
