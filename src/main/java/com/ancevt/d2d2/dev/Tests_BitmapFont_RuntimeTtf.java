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

import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapFontBuilder;
import com.ancevt.d2d2.display.text.BitmapText;

public class Tests_BitmapFont_RuntimeTtf {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        BitmapFont bitmapFont = new BitmapFontBuilder()
                .ttfPath("/home/ancevt/.fonts/Quicksand-Medium.ttf")
                .fontSize(14)
                .spacingY(10)
                .offsetY(3)
                .build();


        BitmapText bitmapText = new BitmapText(bitmapFont);
        //bitmapText.setAutosize(true);
        bitmapText.setSize(200, 200);

        bitmapText.setText("""
                Copyright (C) 2022 the original author or authors.
                See the notice.md file distributed with this work for additional
                information regarding copyright ownership.""");


        PlainRect plainRect = new PlainRect(Color.BLACK);
        plainRect.setSize(bitmapText.getWidth(), bitmapText.getHeight());

        stage.add(plainRect, 100, 250);
        stage.add(bitmapText, 100, 250);



        D2D2.loop();
    }
}
