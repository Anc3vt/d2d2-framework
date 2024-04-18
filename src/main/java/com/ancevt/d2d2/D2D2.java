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

import com.ancevt.d2d2.engine.Engine;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFontManager;
import com.ancevt.d2d2.display.texture.TextureManager;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.input.Mouse;
import com.ancevt.d2d2.util.D2D2Initializer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class D2D2 {

    private static final TextureManager textureManager = new TextureManager();

    private static BitmapFontManager bitmapFontManager;

    @Getter
    private static IDisplayObject cursor;

    private static Engine backend;

    public static Stage directInit(Engine backend) {
        bitmapFontManager = new BitmapFontManager();
        D2D2.backend = backend;
        backend.create();
        return backend.getStage();
    }

    public static void init(Class<? extends D2D2Main> d2d2EntryPointClass) {
        D2D2Initializer.init(d2d2EntryPointClass);
    }

    public static void init(Class<? extends D2D2Main> d2d2EntryPointClass, Map<String, String> propertyMap) {
        Properties properties = new Properties();
        properties.putAll(propertyMap);
        D2D2Initializer.init(d2d2EntryPointClass, properties);
    }

    public static void init(Class<? extends D2D2Main> d2d2EntryPointClass, Properties properties) {
        D2D2Initializer.init(d2d2EntryPointClass, properties);
    }

    public static void init(Class<? extends D2D2Main> d2d2EntryPointClass, InputStream propertiesInputStream) {
        D2D2Initializer.init(d2d2EntryPointClass, propertiesInputStream);
    }

    public static void setCursor(IDisplayObject cursor) {
        if (cursor == D2D2.cursor) return;

        if (cursor != null) {
            Mouse.setVisible(false);
            cursor.removeEventListener(Mouse.class, Event.LOOP_UPDATE);
            cursor.addEventListener(Mouse.class, Event.LOOP_UPDATE, event -> cursor.setXY(Mouse.getX(), Mouse.getY()));
        } else {
            Mouse.setVisible(true);
            D2D2.cursor.removeEventListener(Mouse.class, Event.LOOP_UPDATE);
        }
        D2D2.cursor = cursor;
    }

    public static Stage stage() {
        return backend.getStage();
    }

    public static void loop() {
        backend.start();
    }

    public static void exit() {
        backend.stop();
    }

    public static Engine backend() {
        return backend;
    }

    public static TextureManager textureManager() {
        return textureManager;
    }

    public static BitmapFontManager bitmapFontManager() {
        return bitmapFontManager;
    }
}
