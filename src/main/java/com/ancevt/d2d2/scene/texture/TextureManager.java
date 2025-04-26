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
package com.ancevt.d2d2.scene.texture;

import com.ancevt.d2d2.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureManager {

    private final List<Texture> textures;

    private final Map<String, Texture> textureCache;

    private final Map<String, TextureClip> textureClips;

    @Getter
    @Setter
    private ITextureEngine textureEngine;

    public TextureManager() {
        textureClips = new HashMap<>();
        textures = new ArrayList<>();
        textureCache = new HashMap<>();
    }

    public Texture loadTexture(InputStream pngInputStream) {
        final Texture result = textureEngine.createTexture(pngInputStream);
        textures.add(result);
        return result;
    }

    public Texture loadTexture(String assetPath) {
        if (textureCache.containsKey(assetPath)) {
            return textureCache.get(assetPath);
        }

        final Texture result = textureEngine.createTexture(assetPath);
        textures.add(result);
        textureCache.put(assetPath, result);
        return result;
    }

    public void unloadTexture(Texture texture) {
        textureEngine.unloadTexture(texture);
        textures.remove(texture);

        for (Map.Entry<String, Texture> e : textureCache.entrySet()) {
            if (e.getValue() == texture) {
                textureCache.remove(e.getKey());
                break;
            }
        }
    }

    public void clear() {
        while (!textures.isEmpty()) {
            unloadTexture(textures.get(0));
        }
    }

    public Texture bitmapTextToTexture(Text text) {
        Texture texture = textureEngine.bitmapTextToTexture(text);
        textures.add(texture);
        return texture;
    }

    public int getTextureCount() {
        return textures.size();
    }

    public Texture getTexture(int index) {
        return textures.get(index);
    }

    public void addTextureClip(String key, TextureClip textureClip) {
        textureClips.put(key, textureClip);
    }

    public TextureClip getTextureClip(String key) {
        TextureClip result = textureClips.get(key);
        if (result == null) {
            throw new IllegalArgumentException("No such texture clip key: " + key);
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

    public boolean containsTexture(Texture texture) {
        return textures.contains(texture);
    }

    public void addTexture(Texture texture) {
        textures.add(texture);
    }
}
