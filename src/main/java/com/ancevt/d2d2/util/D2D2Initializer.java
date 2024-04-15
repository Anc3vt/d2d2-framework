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
package com.ancevt.d2d2.util;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.D2D2Main;
import com.ancevt.d2d2.backend.D2D2Backend;
import com.ancevt.d2d2.backend.lwjgl.LwjglBackend;
import com.ancevt.d2d2.display.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.ancevt.commons.string.ConvertableString.convert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class D2D2Initializer {

    private static final String PROPERTIES_FILENAME = "d2d2.properties";

    @Getter
    private static Properties properties;

    public static void init(Class<? extends D2D2Main> d2d2EntryPointClass) {
        finalizeInitAndStartLoop(
            d2d2EntryPointClass,
            handlePropertiesAndCreateStage(
                D2D2Initializer.class
                    .getClassLoader()
                    .getResourceAsStream(PROPERTIES_FILENAME)
            )
        );
    }

    public static void init(Class<? extends D2D2Main> d2d2EntryPointClass, InputStream propertiesInputStream) {
        finalizeInitAndStartLoop(d2d2EntryPointClass, handlePropertiesAndCreateStage(propertiesInputStream));
    }

    public static void init(Class<? extends D2D2Main> d2d2EntryPointClass, Properties properties) {
        finalizeInitAndStartLoop(d2d2EntryPointClass, handlePropertiesAndCreateStage(properties));
    }

    private static Stage handlePropertiesAndCreateStage(Properties properties) {
        String backendClass = properties.getProperty("d2d2.backend", LwjglBackend.class.getName());
        String titleText = properties.getProperty("d2d2.window.title", "D2D2 Application");
        int width = convert(properties.getProperty("d2d2.window.width", "800")).toInt();
        int height = convert(properties.getProperty("d2d2.window.height", "600")).toInt();

        try {
            D2D2Backend backend = (D2D2Backend) Class.forName(backendClass)
                .getConstructor(int.class, int.class, String.class)
                .newInstance(width, height, titleText);

            return D2D2.directInit(backend);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Stage handlePropertiesAndCreateStage(InputStream propertiesInputStream) {
        properties = new Properties();

        if (propertiesInputStream != null) {
            try {
                properties.load(propertiesInputStream);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        return handlePropertiesAndCreateStage(properties);
    }

    private static void finalizeInitAndStartLoop(Class<? extends D2D2Main> d2d2EntryPointClass, Stage stage) {
        try {
            D2D2Main entryPoint = d2d2EntryPointClass.getConstructor().newInstance();
            entryPoint.onCreate(stage);
            D2D2.loop();
            entryPoint.onDispose();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
