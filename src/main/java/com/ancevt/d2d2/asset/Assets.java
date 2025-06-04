/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.asset;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.exception.AssetException;
import com.ancevt.d2d2.scene.text.BitmapFont;
import com.ancevt.d2d2.scene.texture.Texture;
import com.ancevt.d2d2.sound.Sound;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Assets {

    private static final String ASSETS_DIR = "assets/";

    public static Asset getAsset(String assetPath) {
        final ClassLoader classLoader = Assets.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(ASSETS_DIR + assetPath);
        if (inputStream == null) throw new AssetException("resource " + assetPath + " not found");
        return new Asset(inputStream);
    }

    public static Texture loadTexture(String assetPath) {
        return D2D2.getTextureManager().loadTexture(assetPath);
    }

    public static Texture loadTexture(InputStream inputStream) {
        return D2D2.getTextureManager().loadTexture(inputStream);
    }

    public static BitmapFont loadBitmapFont(String pngAssetPath, String bmfAssetPath) {
        return D2D2.getBitmapFontManager().loadBitmapFont(pngAssetPath, bmfAssetPath);
    }

    public static BitmapFont loadBitmapFont(InputStream pngInputStream, InputStream bmfInputStream, String name) {
        return D2D2.getBitmapFontManager().loadBitmapFont(pngInputStream, bmfInputStream, name);
    }

    public static Sound loadSound(String assetPath) {
        return D2D2.getSoundManager().loadSound(assetPath);
    }

    public static Sound loadSound(InputStream inputStream) {
        return D2D2.getSoundManager().loadSound(inputStream);
    }


}
