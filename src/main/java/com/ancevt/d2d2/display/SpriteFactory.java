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
package com.ancevt.d2d2.display;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.texture.TextureClip;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpriteFactory {
    private static final Map<String, TextureClip> textureCacheFiles = new HashMap<>();

    public static Sprite createSprite(String assetPath) {
        return new SimpleSprite(assetPath);
    }

    public static Sprite createSprite(String assetPath, int textureX, int textureY, int textureWidth, int textureHeight) {
        return new SimpleSprite(
            textureCacheFiles.computeIfAbsent(
                "%s_%d".formatted(
                    assetPath,
                    Objects.hash(
                        assetPath,
                        textureX,
                        textureY,
                        textureWidth,
                        textureHeight
                    )),
                key -> D2D2.getTextureManager()
                    .loadTextureAtlas(assetPath)
                    .createTextureClip(textureX, textureY, textureWidth, textureHeight)
            )
        );
    }

    public static Sprite createSpriteByTextureKey(String textureKey) {
        return new SimpleSprite(D2D2.getTextureManager().getTextureClip(textureKey));
    }

    public static Sprite createEmptySprite() {
        return new SimpleSprite();
    }

    public static void clearCache() {
        textureCacheFiles.clear();
    }

}
