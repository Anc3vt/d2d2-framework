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
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;

public class Tests_BitmapText_toSprite {
    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, "D2D2Demo2"));
        Root root = new Root();

        BitmapText bitmapText = new BitmapText();
        bitmapText.setText("JAVA: How to create png image from BufferedImage. Image ...");

        Sprite sprite = bitmapText.toSprite();

        sprite.setScale(2,2);
        root.add(sprite, 10, 20);

        D2D2.getTextureManager().bitmapTextToTextureAtlas(bitmapText);

        root.add(new FpsMeter());
        D2D2.getStage().setRoot(root);
        D2D2.loop();
    }
}
