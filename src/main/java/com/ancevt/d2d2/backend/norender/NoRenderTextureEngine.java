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
package com.ancevt.d2d2.backend.norender;

import org.jetbrains.annotations.NotNull;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.display.texture.ITextureEngine;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.display.texture.TextureCell;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NoRenderTextureEngine implements ITextureEngine {

    private int textureAtlasIdCounter;
    private final Map<Integer, Image> images;

    public NoRenderTextureEngine() {
        images = new HashMap<>();
    }

    @Override
    public boolean bind(TextureAtlas textureAtlas) {
        return false;
    }

    @Override
    public void enable(TextureAtlas textureAtlas) {

    }

    @Override
    public void disable(TextureAtlas textureAtlas) {

    }

    @Override
    public TextureAtlas createTextureAtlas(InputStream pngInputStream) {
        try {
            BufferedImage image = ImageIO.read(pngInputStream);
            textureAtlasIdCounter++;
            images.put(textureAtlasIdCounter, image);
            return new TextureAtlas(
                    textureAtlasIdCounter,
                    image.getWidth(),
                    image.getHeight()
            );
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public TextureAtlas createTextureAtlas(String assetPath) {
        return createTextureAtlas(Assets.getAssetAsStream(assetPath));
    }

    @Override
    public TextureAtlas createTextureAtlas(int width, int height, TextureCell[] cells) {
        textureAtlasIdCounter++;
        images.put(textureAtlasIdCounter, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        return new TextureAtlas(textureAtlasIdCounter, width, height);
    }

    @Override
    public void unloadTextureAtlas(@NotNull TextureAtlas textureAtlas) {
        images.remove(textureAtlas.getId());
    }

    @Override
    public TextureAtlas bitmapTextToTextureAtlas(BitmapText bitmapText) {
        int width = (int) bitmapText.getWidth();
        int height = (int) bitmapText.getHeight();
        textureAtlasIdCounter++;
        TextureAtlas textureAtlas = new TextureAtlas(textureAtlasIdCounter, width, height);
        D2D2.getTextureManager().addTexture("_textureAtlas_text_" + textureAtlas.getId(), textureAtlas.createTexture());
        return textureAtlas;
    }
}
