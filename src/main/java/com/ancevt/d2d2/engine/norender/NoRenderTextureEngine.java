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
package com.ancevt.d2d2.engine.norender;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.display.text.Text;
import com.ancevt.d2d2.display.texture.ITextureEngine;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureClipCombinerCell;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NoRenderTextureEngine implements ITextureEngine {
    private int textureIdCounter;
    private final Map<Integer, Image> images;

    public NoRenderTextureEngine() {
        images = new HashMap<>();
    }

    @Override
    public boolean bind(Texture texture) {
        return false;
    }

    @Override
    public void enable(Texture texture) {

    }

    @Override
    public void disable(Texture texture) {

    }

    @Override
    public Texture createTexture(InputStream pngInputStream) {
        try {
            BufferedImage image = ImageIO.read(pngInputStream);
            textureIdCounter++;
            images.put(textureIdCounter, image);
            return new Texture(
                textureIdCounter,
                    image.getWidth(),
                    image.getHeight()
            );
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Texture createTexture(String assetPath) {
        return createTexture(Assets.getAsset(assetPath));
    }

    @Override
    public Texture createTexture(int width, int height, TextureClipCombinerCell[] cells) {
        textureIdCounter++;
        images.put(textureIdCounter, new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        return new Texture(textureIdCounter, width, height);
    }

    @Override
    public void unloadTexture(Texture texture) {
        images.remove(texture.getId());
    }

    @Override
    public Texture bitmapTextToTexture(Text text) {
        int width = (int) text.getWidth();
        int height = (int) text.getHeight();
        textureIdCounter++;
        Texture texture = new Texture(textureIdCounter, width, height);
        D2D2.textureManager().addTextureClip("_texture_text_" + texture.getId(), texture.createTextureClip());
        return texture;
    }
}
