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
package com.ancevt.d2d2.display.texture;

import com.ancevt.d2d2.display.text.BitmapText;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureManager {

    private final List<TextureAtlas> textureAtlases;

    private final Map<String, TextureAtlas> textureAtlasCache;

    private final Map<String, Texture> textures;

    @Getter
    @Setter
    private ITextureEngine textureEngine;

    public TextureManager() {
        textures = new HashMap<>();
        textureAtlases = new ArrayList<>();
        textureAtlasCache = new HashMap<>();
    }

    public TextureAtlas loadTextureAtlas(InputStream pngInputStream) {
        final TextureAtlas result = textureEngine.createTextureAtlas(pngInputStream);
        textureAtlases.add(result);
        return result;
    }

    public TextureAtlas loadTextureAtlas(String assetPath) {
        if (textureAtlasCache.containsKey(assetPath)) {
            return textureAtlasCache.get(assetPath);
        }

        final TextureAtlas result = textureEngine.createTextureAtlas(assetPath);
        textureAtlases.add(result);
        textureAtlasCache.put(assetPath, result);
        return result;
    }

    public void unloadTextureAtlas(TextureAtlas textureAtlas) {
        textureEngine.unloadTextureAtlas(textureAtlas);
        textureAtlases.remove(textureAtlas);

        for (Map.Entry<String, TextureAtlas> e : textureAtlasCache.entrySet()) {
            if (e.getValue() == textureAtlas) {
                textureAtlasCache.remove(e.getKey());
                break;
            }
        }

        textureAtlas.setDisposed(true);
    }

    public void clear() {
        while (!textureAtlases.isEmpty()) {
            unloadTextureAtlas(textureAtlases.get(0));
        }
    }

    public TextureAtlas bitmapTextToTextureAtlas(BitmapText bitmapText) {
        TextureAtlas textureAtlas = textureEngine.bitmapTextToTextureAtlas(bitmapText);
        textureAtlases.add(textureAtlas);
        return textureAtlas;
    }

    public int getTextureAtlasCount() {
        return textureAtlases.size();
    }

    public TextureAtlas getTextureAtlas(int index) {
        return textureAtlases.get(index);
    }

    public void addTexture(String key, Texture texture) {
        textures.put(key, texture);
    }

    public Texture getTexture(String key) {
        Texture result = textures.get(key);
        if (result == null) {
            throw new IllegalArgumentException("No such texture key: " + key);
        }
        return result;
    }

    public final void loadTextureDataInfo(String assetPath) {
        try {
            TextureDataInfoReadHelper.readTextureDataInfoFile(assetPath);
        } catch (IOException e) {
            throw new TextureException(e);
        }
    }
}
