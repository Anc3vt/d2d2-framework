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
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;

public class Tests_BitmapText_cacheAsSprite {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();
        stage.setBackgroundColor(Color.WHITE);

        String t = "Test test test test test test test test test test test test test";
        
        String text = """
                #<FF0000>Test test test<FFFF00> test test test <00FFFF>test test test<FFFFFF> test test test
                <FF0000>Test test test<FFFF00> test test test <00FFFF>test test test<FFFFFF> test test test
                """;

        //BitmapFont bitmapFont = BitmapFont.loadBitmapFont("fira-code/FiraCode-16-Regular");
        BitmapFont bitmapFont = BitmapFont.loadBitmapFont("terminus/Terminus-16-Bold");

        Color outlineColor = Color.BLACK;

        float alpha = 0.3f;
        float radius = 1f;
        float scale = 1f;

        BitmapText bitmapText1 = new BitmapText(bitmapFont);
        bitmapText1.setAutosize(true);
        bitmapText1.setText(t);
        bitmapText1.setColor(outlineColor);
        bitmapText1.setCacheAsSprite(true);
        bitmapText1.setAlpha(alpha);
        bitmapText1.setScale(scale, scale);
        stage.add(bitmapText1, 50 - radius, 250 - radius);

        BitmapText bitmapText2 = new BitmapText(bitmapFont);
        bitmapText2.setAutosize(true);
        bitmapText2.setText(t);
        bitmapText2.setColor(outlineColor);
        bitmapText2.setCacheAsSprite(true);
        bitmapText2.setAlpha(alpha);
        bitmapText2.setScale(scale, scale);
        stage.add(bitmapText2, 50, 250 - radius);

        BitmapText bitmapText3 = new BitmapText(bitmapFont);
        bitmapText3.setAutosize(true);
        bitmapText3.setText(t);
        bitmapText3.setColor(outlineColor);
        bitmapText3.setCacheAsSprite(true);
        bitmapText3.setAlpha(alpha);
        bitmapText3.setScale(scale, scale);
        stage.add(bitmapText3, 50 + radius, 250 - radius);

        BitmapText bitmapText4 = new BitmapText(bitmapFont);
        bitmapText4.setAutosize(true);
        bitmapText4.setText(t);
        bitmapText4.setColor(outlineColor);
        bitmapText4.setCacheAsSprite(true);
        bitmapText4.setAlpha(alpha);
        bitmapText4.setScale(scale, scale);
        stage.add(bitmapText4, 50 - radius, 250);

        BitmapText bitmapText5 = new BitmapText(bitmapFont);
        bitmapText5.setAutosize(true);
        bitmapText5.setText(t);
        bitmapText5.setColor(outlineColor);
        bitmapText5.setCacheAsSprite(true);
        bitmapText5.setAlpha(alpha);
        bitmapText5.setScale(scale, scale);
        stage.add(bitmapText5, 50 + radius, 250);

        BitmapText bitmapText6 = new BitmapText(bitmapFont);
        bitmapText6.setAutosize(true);
        bitmapText6.setText(t);
        bitmapText6.setColor(outlineColor);
        bitmapText6.setCacheAsSprite(true);
        bitmapText6.setAlpha(alpha);
        bitmapText6.setScale(scale, scale);
        stage.add(bitmapText6, 50 - radius, 250 + radius);

        BitmapText bitmapText7 = new BitmapText(bitmapFont);
        bitmapText7.setAutosize(true);
        bitmapText7.setText(t);
        bitmapText7.setColor(outlineColor);
        bitmapText7.setCacheAsSprite(true);
        bitmapText7.setAlpha(alpha);
        bitmapText7.setScale(scale, scale);
        stage.add(bitmapText7, 50, 250 + radius);

        BitmapText bitmapText8 = new BitmapText(bitmapFont);
        bitmapText8.setAutosize(true);
        bitmapText8.setText(t);
        bitmapText8.setColor(outlineColor);
        bitmapText8.setCacheAsSprite(true);
        bitmapText8.setAlpha(alpha);
        bitmapText8.setScale(scale, scale);
        stage.add(bitmapText8, 50+ 1, 250  + radius);

        BitmapText bitmapText9 = new BitmapText(bitmapFont);
        bitmapText9.setAutosize(true);
        bitmapText9.setText(text);
        bitmapText9.setMulticolorEnabled(true);
        bitmapText9.setCacheAsSprite(true);
        bitmapText9.setScale(scale,scale);
        stage.add(bitmapText9, 50, 250);



        stage.add(new FpsMeter());
        D2D2.loop();
    }

    private static class Text extends Container {




    }
}
