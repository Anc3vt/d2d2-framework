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
package com.ancevt.d2d2;

import com.ancevt.d2d2.backend.D2D2Backend;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.input.Mouse;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFontManager;
import com.ancevt.d2d2.display.texture.TextureManager;
import lombok.Getter;


public class D2D2 {

    private static final TextureManager textureManager = new TextureManager();
    private static BitmapFontManager bitmapFontManager;

    @Getter
    private static IDisplayObject cursor;
    private static D2D2Backend backend;

    private D2D2() {
    }

    public static TextureManager textureManager() {
        return textureManager;
    }

    public static BitmapFontManager bitmapFontManager() {
        return bitmapFontManager;
    }

    public static Stage init( D2D2Backend backend) {
        bitmapFontManager = new BitmapFontManager();
        D2D2.backend = backend;
        backend.create();
        return backend.getStage();
    }

    public static void setFullscreen(boolean value) {
        backend.setFullscreen(value);
    }

    public static void setCursor(IDisplayObject cursor) {
        if (cursor == D2D2.cursor) return;

        if (cursor != null) {
            Mouse.setVisible(false);
            cursor.removeEventListener(Mouse.class, Event.EXIT_FRAME);
            cursor.addEventListener(Mouse.class, Event.EXIT_FRAME, event -> cursor.setXY(Mouse.getX(), Mouse.getY()));
        } else {
            Mouse.setVisible(true);
            D2D2.cursor.removeEventListener(Mouse.class, Event.EXIT_FRAME);
        }
        D2D2.cursor = cursor;
    }

    public static Stage stage() {
        return backend.getStage();
    }

    public static D2D2Backend backend() {
        return backend;
    }

    public static void loop() {
        backend.start();
    }

    public static void exit() {
        backend.stop();
    }

}
