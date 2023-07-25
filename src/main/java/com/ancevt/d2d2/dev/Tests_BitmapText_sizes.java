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

import com.ancevt.commons.concurrent.Async;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapFontManager;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.interactive.DragUtil;
import com.ancevt.d2d2.interactive.InteractiveContainer;
import com.ancevt.d2d2.interactive.InteractiveManager;
import com.ancevt.util.command.CommandRepl;
import com.ancevt.util.command.CommandSet;

import java.io.IOException;

public class Tests_BitmapText_sizes {

    public static void main(String[] args) throws IOException {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        CommandSet<Integer> commandSet = new CommandSet<>();
        CommandRepl<Integer> commandRepl = new CommandRepl<>(commandSet);

        Box box = new Box();
        stage.add(box, 50, 350);

        BitmapText bitmapText = createBitmapText("terminus/Terminus-24");
        bitmapText.setAutosize(true);
        bitmapText.setMulticolorEnabled(true);
        bitmapText.setCacheAsSprite(false);
        bitmapText.setText("#This is text text<FFFF00> is this 1 2 3 4 5 6 regular");
        stage.add(bitmapText, 100, 250);

        Async.run(() -> {
            try {
                commandRepl.start(System.in, System.out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        commandSet.registerCommand("f", "", a -> {
            System.out.println(InteractiveManager.getInstance().getFocused());
            return 1;
        });

        stage.add(new FpsMeter());

        D2D2.loop();
    }

    private static final BitmapText createBitmapText(String fontName) {
        BitmapFont bitmapFont = BitmapFontManager.getInstance().loadBitmapFont(fontName);

        BitmapText bitmapText = new BitmapText(bitmapFont);
        bitmapText.setMulticolorEnabled(true);
        bitmapText.setText("#An inspired calligrapher can create pages of beauty using stick ink, quill, brush, pick-axe, buzz saw, or even <FF0000>strawberry<FFFFFF> jam.");

        return bitmapText;
    }

    private static class Box extends InteractiveContainer {

        private final float DEFAULT_WIDTH = 200.0f;
        private final float DEFAULT_HEIGHT = 200.0f;

        private final PlainRect bg;

        private final BitmapText bitmapText;

        public Box() {
            setEnabled(true);
            focus();
            DragUtil.enableDrag(this);
            bg = new PlainRect(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.BLACK);
            bitmapText = createBitmapText("open-sans/OpenSans-12-Regular");
            bitmapText.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            add(bg);
            add(bitmapText);
            setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

            addEventListener(Box.class, InteractiveEvent.KEY_DOWN, event -> {
                var e = (InteractiveEvent) event;

                final float S = 10;

                switch (e.getKeyCode()) {
                    case KeyCode.W -> setSize(getWidth(), getHeight() - S);
                    case KeyCode.S -> setSize(getWidth(), getHeight() + S);
                    case KeyCode.A -> setSize(getWidth() - S, getHeight());
                    case KeyCode.D -> setSize(getWidth() + S, getHeight());
                }
            });
        }

        @Override
        public void setSize(float width, float height) {
            super.setSize(width, height);
            bg.setSize(width, height);
            bitmapText.setSize(width, height);
        }
    }
}
































