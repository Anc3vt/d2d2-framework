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

import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFontManager;
import com.ancevt.d2d2.display.text.BitmapText;

public class Tests_BitmapText_toSprite {
    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "D2D2Demo2"));

        stage.setBackgroundColor(Color.DARK_GRAY);

        BitmapFontManager.getInstance().getDefaultBitmapFont().setPaddingTop(-5);

        BitmapText bitmapText = new BitmapText();
        bitmapText.setMulticolorEnabled(true);
        bitmapText.setSpacing(2f);
        bitmapText.setLineSpacing(5f);
        bitmapText.setText("#Copyright (C)\n<FFFF00>2022 <FFFFFF>the original author or authors.");
        bitmapText.setScale(2,2);
        PlainRect plainRect = new PlainRect(20, 20, Color.BLUE);
        stage.add(plainRect, 10, 20);
        stage.add(bitmapText, 10, 20);

        Sprite sprite = bitmapText.toSprite();
        sprite.setScale(2,2);
        PlainRect plainRect2 = new PlainRect(20, 20, Color.BLUE);
        stage.add(plainRect2, 10, 200);
        stage.add(sprite, 10, 200);

        stage.add(new FpsMeter());
        D2D2.loop();
    }
}
