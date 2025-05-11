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

package com.ancevt.d2d2.scene;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.scene.texture.Texture;
import com.ancevt.d2d2.scene.texture.TextureClip;

public interface Sprite extends Node, Colored, Repeatable {

    Color DEFAULT_COLOR = Color.WHITE;

    static Sprite create() {
        Sprite sprite = new SpriteImpl();
        sprite.setColor(SpriteImpl.DEFAULT_COLOR);
        sprite.setRepeat(1, 1);
        sprite.setName("_" + sprite.getClass().getSimpleName() + sprite.getNodeId());
        return sprite;
    }

    static Sprite create(TextureClip textureClip) {
        Sprite sprite = new SpriteImpl();
        sprite.setTextureClip(textureClip);
        sprite.setColor(SpriteImpl.DEFAULT_COLOR);
        sprite.setRepeat(1, 1);
        return sprite;
    }

    static Sprite create(Texture texture) {
        return create(texture.createTextureClip());
    }

    static Sprite load(String assetPath) {
        TextureClip textureClip = D2D2.textureManager().loadTexture(assetPath).createTextureClip();
        return create(textureClip);
    }

    static Sprite load(String assetPath, int textureX, int textureY, int textureWidth, int textureHeight) {
        TextureClip textureClip = D2D2.textureManager()
                .loadTexture(assetPath)
                .createTextureClip(textureX, textureY, textureWidth, textureHeight);
        return create(textureClip);
    }

    TextureClip getTextureClip();

    void setTextureClip(TextureClip value);

    void setTextureClip(String textureClipKey);

    void setTextureBleedingFix(double v);

    double getTextureBleedingFix();

    void setVertexBleedingFix(double v);

    double getVertexBleedingFix();

    Sprite cloneSprite();
}
