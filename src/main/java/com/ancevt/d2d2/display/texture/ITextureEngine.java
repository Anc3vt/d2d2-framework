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

import java.io.InputStream;

// TODO: refactor and delete this interface
public interface ITextureEngine {

    boolean bind(TextureAtlas textureAtlas);

    void enable(TextureAtlas textureAtlas);

    void disable(TextureAtlas textureAtlas);

    TextureAtlas createTextureAtlas(InputStream pngInputStream);

    TextureAtlas createTextureAtlas(String assetPath);

    TextureAtlas createTextureAtlas(int width, int height, TextureCell[] cells);

    void unloadTextureAtlas(TextureAtlas textureAtlas);

    TextureAtlas bitmapTextToTextureAtlas(BitmapText bitmapText);

}
