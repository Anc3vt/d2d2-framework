/**
 * Copyright (C) 2025 the original author or authors.
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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TextureClip {

    private final Texture texture;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public int getX() {return x;}

    public int getY() {return y;}

    public int getWidth() {return width;}

    public int getHeight() {return height;}


    public TextureClip createSubTextureClip(int x, int y, int width, int height) {
        return getTexture().createTextureClip(getX() + x, getY() + y, width, height);
    }

    public Texture getTexture() {
        return texture;
    }

    public String stringify() {
        return "%d,%d,%d,%d".formatted(x, y, width, height);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "texture=" + texture +
            ", x=" + x +
            ", y=" + y +
            ", width=" + width +
            ", height=" + height +
            '}';
    }
}
