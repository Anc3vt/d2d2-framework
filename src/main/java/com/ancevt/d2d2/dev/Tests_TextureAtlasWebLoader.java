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
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.display.texture.TextureUrlLoader;
import com.ancevt.d2d2.event.TextureUrlLoaderEvent;

public class Tests_TextureAtlasWebLoader {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));

        Preloader preloader = new Preloader();
        preloader.setScale(0.5f, 0.5f);

        TextureUrlLoader loader = new TextureUrlLoader("https://d2d2.world/test.png");
        loader.addEventListener(TextureUrlLoaderEvent.TEXTURE_LOAD_START, event -> stage.add(preloader, 252f / 2, 167f / 2));
        loader.addEventListener(TextureUrlLoaderEvent.TEXTURE_LOAD_COMPLETE, event -> {
            TextureAtlas atlas = loader.getLastLoadedTextureAtlas();
            Sprite sprite = new Sprite(atlas.createTexture());
            stage.add(sprite);
            stage.remove(preloader);
            stage.add(new FpsMeter());
        });
        loader.load();


        D2D2.loop();
    }

    private static class Preloader extends Container {
        static final int FRAMES = 10;
        int timer = FRAMES;

        public Preloader() {
            Sprite sprite = new Sprite(D2D2.getTextureManager().loadTextureAtlas("d2d2-loading.png").createTexture());
            add(sprite, -32, -32);
        }

        @Override
        public void onEachFrame() {
            if (timer-- <= 0) {
                timer = FRAMES;
                rotate(45);
            }
        }
    }
}
