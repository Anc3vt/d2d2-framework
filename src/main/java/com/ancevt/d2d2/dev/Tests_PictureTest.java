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


import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.input.Mouse;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;

public class Tests_PictureTest {

    private static float _x;
    private static float _y;

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));

        Sprite sprite = new Sprite(
                D2D2.getTextureManager()
                        .loadTextureAtlas("d2d2-picture-test.png")
                        .createTexture()
        );

        int repeat = 300;

        sprite.setRepeat(repeat, repeat);

        sprite.addEventListener(Event.EXIT_FRAME, event -> {
            float x = Mouse.getX();
            float y = Mouse.getY();
            float cx = D2D2.stage().getWidth() / 2;
            float cy = D2D2.stage().getHeight() / 2;

            float mx = (cx - x) / 100f;
            float my = (cy - y) / 100f;

            _x += mx;
            _y += my;

            sprite.setXY(_x, _y);

            if (sprite.getX() < -sprite.getWidth() * repeat) sprite.setX(0);
            if (sprite.getY() < -sprite.getHeight() * repeat) sprite.setY(0);
        });

        stage.add(sprite);
        stage.add(new FpsMeter(), 10, 10);
        stage.add(new Sprite("satellite"), 100, 100);
        D2D2.loop();
    }
}
