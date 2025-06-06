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

package com.ancevt.d2d2.scene.texture;

import com.ancevt.d2d2.scene.Group;
import com.ancevt.d2d2.scene.text.BitmapText;

import java.io.InputStream;
import java.util.Map;

public interface TextureManager {

    Map<Integer, Texture> getLoadedTextures();

    Texture loadTexture(InputStream pngInputStream);

    Texture loadTexture(String assetPath);

    void unloadTexture(Texture texture);

    Texture renderBitmapTextToTexture(BitmapText bitmapText);

    Texture renderGroupToTexture(Group group, int width, int height);

    boolean isTextureActive(Texture texture);

    void registerTextureRegion(String key, TextureRegion textureRegion);

    void removeTextureRegion(String key);

    TextureRegion getTextureRegion(String key);

    Map<String, TextureRegion> getTextureRegionMap();

    void loadTextureDataInfo(String assetMetaFile);
}
