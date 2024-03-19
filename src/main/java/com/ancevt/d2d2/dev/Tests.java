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
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;

public class Tests {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "Hello D2D2 (floating)"));

        TextureAtlas textureAtlas = D2D2.getTextureManager().loadTextureAtlas("d2d2-logo.png");
        Texture texture = textureAtlas.createTexture();

        for (int i = 0; i < 1; i++) {
            Container container = new Container();

            Sprite sprite = new Sprite(texture);
            sprite.setColor(Color.createRandomColor());
            container.addEventListener(Event.EXIT_FRAME, e -> {
                container.move(0.01f, 0.01f);
                container.rotate(1);
                //container.toScale(1.005f, 1.005f);
            });
            sprite.setRepeat(1, 1);
            container.add(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);

            stage.add(container, (float) (Math.random() * 300) + 100, (float) (Math.random() * 300) + 100);
        }

        BitmapText bitmapText = new BitmapText();
        bitmapText.setName("_test_");
        bitmapText.setColor(Color.WHITE);
        bitmapText.setText("Hello BitmapText\nSecond line");
        bitmapText.addEventListener(Event.EXIT_FRAME, e -> {
            //bitmapText.toScale(1.001f, 1.001f);
            //bitmapText.rotate(1f);
            bitmapText.moveX(0.01f);
        });

        bitmapText.setScale(5, 5);

        stage.add(bitmapText, 10, 200);
        stage.add(new FpsMeter(), 10, 10);

        D2D2.loop();
    }
}
