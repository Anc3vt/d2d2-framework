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
package com.ancevt.d2d2;

import com.ancevt.d2d2.backend.D2D2Backend;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.texture.TextureManager;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.input.Mouse;
import org.jetbrains.annotations.NotNull;

public class D2D2 {

    private static final TextureManager textureManager = new TextureManager();
    private static D2D2Backend backend;
    private static IDisplayObject cursor;

    private D2D2() {
    }

    public static void setFullscreen(boolean value) {
        backend.setFullscreen(value);
    }

    public static void setCursor(IDisplayObject cursor) {
        if (cursor == D2D2.cursor) return;

        if (cursor != null) {
            Mouse.setVisible(false);
            cursor.addEventListener(Mouse.class, Event.EACH_FRAME, event -> cursor.setXY(Mouse.getX(), Mouse.getY()));
        } else {
            Mouse.setVisible(true);
            D2D2.cursor.removeEventListener(Mouse.class, Event.EACH_FRAME);
        }
        D2D2.cursor = cursor;
        stage().add(D2D2.cursor);
    }

    public static IDisplayObject getCursor() {
        return cursor;
    }

    public static boolean isFullscreen() {
        return backend.isFullscreen();
    }

    public static void setSmoothMode(boolean value) {
        backend.setSmoothMode(value);
    }

    public static boolean isSmoothMode() {
        return backend.isSmoothMode();
    }

    public static D2D2Backend getBackend() {
        return backend;
    }

    public static @NotNull Stage init(@NotNull D2D2Backend backend) {
        D2D2.backend = backend;
        backend.create();
        return backend.getStage();
    }

    public static Stage stage() {
        return backend.getStage();
    }

    public static void loop() {
        backend.start();
    }

    public static TextureManager getTextureManager() {
        return textureManager;
    }

    public static void exit() {
        backend.stop();
    }

}
