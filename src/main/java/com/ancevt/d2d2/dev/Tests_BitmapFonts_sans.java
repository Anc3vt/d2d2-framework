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
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;

public class Tests_BitmapFonts_sans {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        stage.add(createBitmapFont("open-sans/OpenSans-24-Regular"), 100, 250);
        stage.add(createBitmapFont("fira-code/FiraCode-24-Regular"), 100, 400);


        DebugPanel.setEnabled(true);
        DebugPanel.show("test", """
                #An inspired calligrapher can create pages of 
                beauty using stick ink, quill, brush, pick-axe,
                buzz saw, or even <FF0000>strawberry<FFFFFF> jam.""");



        D2D2.loop();
    }

    private static BitmapText createBitmapFont(String name) {
        BitmapText bitmapText = new BitmapText();
        bitmapText.setBitmapFont(BitmapFont.loadBitmapFont(name));

        bitmapText.setText("""
                #An inspired calligrapher can create pages of 
                beauty using stick ink, quill, brush, pick-axe,
                buzz saw, or even <FF0000>strawberry<FFFFFF> jam.
                """ + bitmapText.getBitmapFont().isMonospaced() + """
                """);
        
        bitmapText.setSize(800, 200);
        bitmapText.setAutosize(true);
        bitmapText.setMulticolorEnabled(true);
        return bitmapText;
    }
}
