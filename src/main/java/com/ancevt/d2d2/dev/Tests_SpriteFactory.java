/**
 * Copyright (C) 2023 the original author or authors.
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
import com.ancevt.d2d2.display.FramedSprite;
import com.ancevt.d2d2.display.ISprite;
import com.ancevt.d2d2.display.SpriteFactory;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventListener;

import java.util.Arrays;

public class Tests_SpriteFactory {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));

        for (int i = 0; i < 10; i++) {
            ISprite sprite = SpriteFactory.createSprite("d2d2-core-demo-tileset.png", 144, 96, 64, 48);
            stage.add(sprite, i * sprite.getWidth(), 100);
        }

        TextureAtlas textureAtlas = D2D2.getTextureManager().loadTextureAtlas("d2d2-core-demo-tileset.png");

        {
            FramedSprite framedSprite = new FramedSprite(
                concatArrays(
                    textureAtlas.createTexturesVert(160, 0, 48, 16, 6),
                    textureAtlas.createTexturesVert(208, 0, 48, 16, 6)
                )
            );

            framedSprite.setLoop(true);
            framedSprite.setSlowing(10);
            framedSprite.play();
            stage.add(framedSprite);
        }

        {
            FramedSprite framedSprite = new FramedSprite(
                textureAtlas.createTextures("160,0,48,16v2")
            );

            framedSprite.setLoop(true);
            framedSprite.setSlowing(10);
            framedSprite.play();
            stage.add(framedSprite,0,200);


            framedSprite.addEventListener(framedSprite, Event.EXIT_FRAME, new EventListener() {

                int t = 0;

                @Override
                public void onEvent(Event event) {
                    t ++;
                    if(t == 100) {
                        System.out.println();
                    }
                }
            });
        }

        FpsMeter fpsMeter = new FpsMeter();
        fpsMeter.setScale(3.0f, 3.0f);

        stage.add(fpsMeter);

        D2D2.loop();
    }

    static <T> T[] concatArrays(T[] array1, T[] array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }
}
