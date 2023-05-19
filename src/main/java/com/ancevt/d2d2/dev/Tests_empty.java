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
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapText;

public class Tests_empty {


    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));


        stage.add(new FpsMeter(), 100, 100);


        BitmapText bitmapText = new BitmapText();
        bitmapText.setText("TEst");
        bitmapText.setColor(Color.WHITE);

        stage.add(bitmapText, 150, 150);

        D2D2.loop();
    }
}
