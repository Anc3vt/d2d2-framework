/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2;

import com.ancevt.d2d2.engine.DisplayManager;
import com.ancevt.d2d2.engine.Engine;
import com.ancevt.d2d2.engine.SoundManager;
import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.input.Mouse;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Node;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.text.BitmapFontManager;
import com.ancevt.d2d2.scene.texture.TextureManager;
import com.ancevt.util.args.Args;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class D2D2 {
    private static final String DEFAULT_PROPERTIES_FILE_NAME = "application.properties";

    private static final TextureManager textureManager = new TextureManager();

    private static BitmapFontManager bitmapFontManager;

    @Getter
    private static Node cursor;
    private static Engine engine;

    @Getter
    private static D2D2Application application;
    @Getter
    private static Args args;

    private static boolean noScaleMode;

    /*
    public static void init(D2D2Application application, String[] args) {
        init(application, args, DEFAULT_PROPERTIES_FILE_NAME, Map.of());
    }

    public static void init(D2D2Application application,
                            String[] args,
                            String propertiesResourceFileName) {
        init(application, args, propertiesResourceFileName, Map.of());
    }

     */

    public static void init(D2D2Application application, D2D2Config config) {
        D2D2.application = application;
        addPropertiesToSystemProperties(config.asMap());

        int width = config.getOrDefault(D2D2Config.WIDTH, 800);
        int height = config.getOrDefault(D2D2Config.HEIGHT, 600);
        String title = config.getOrDefault(D2D2Config.TITLE, "D2D2 Application");
        boolean noScaleMode = config.getOrDefault(D2D2Config.NO_SCALE_MODE, false);
        String engineClassName = config.getOrDefault(D2D2Config.ENGINE, "com.ancevt.d2d2.engine.lwjgl.LwjglEngine");

        Engine engine = createEngine(engineClassName, width, height, title);
        Root root = createRoot(engine);

        setNoScaleMode(noScaleMode);
        application.start(root);
        startMainLoop();
        application.shutdown();
    }

    /*
    public static void init(D2D2Application application,
                            String[] args,
                            String propertiesResourceFileName,
                            Map<String, String> properties
    ) {
        D2D2.application = application;
        D2D2.args = Args.of(args);

        readPropertyFileIfExist(propertiesResourceFileName);
        addCliArgsToSystemProperties(args);
        addPropertiesToSystemProperties(properties);

        if (Arrays.asList(args).contains("--lwjgl")) {
            System.setProperty("d2d2.engine", "com.ancevt.d2d2.engine.lwjgl.LwjglEngine");
            System.setProperty("d2d2.window.width", "1168");
            System.setProperty("d2d2.window.height", "1000");
            System.err.println("D2D2: `--lwjgl` default engine and window properties preset is set");
        }

        Properties p = System.getProperties();
        String engineClassName = p.getProperty(D2D2PropertyConstants.D2D2_ENGINE, NoRenderEngine.class.getName());
        String titleText = p.getProperty(D2D2PropertyConstants.D2D2_TITLE, "D2D2 Application");
        int width = convert(p.getProperty(D2D2PropertyConstants.D2D2_WIDTH, "800")).toInt();
        int height = convert(p.getProperty(D2D2PropertyConstants.D2D2_HEIGHT, "600")).toInt();
        Engine engine = createEngine(engineClassName, width, height, titleText);

        Root root = D2D2.createRoot(engine);
        application.start(root);
        D2D2.startMainLoop();
        application.shutdown();
    }
     */

    private static void addPropertiesToSystemProperties(Map<String, String> properties) {
        properties.forEach((key, value) -> {
            System.setProperty(key, value);
            System.err.printf("D2D2: %s=%s%n", key, value);
        });
    }

    private static Root createRoot(Engine engine) {
        bitmapFontManager = new BitmapFontManager();
        D2D2.engine = engine;
        engine.create();
        return engine.root();
    }

    private static Engine createEngine(String engineClassName, int width, int height, String titleText) {
        try {
            Engine engine = (Engine) Class.forName(engineClassName)
                    .getConstructor(int.class, int.class, String.class)
                    .newInstance(width, height, titleText);

            return engine;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void readPropertyFileIfExist(String propertiesResourceFileName) {
        if (propertiesResourceFileName == null) return;

        InputStream inputStream = D2D2.class
                .getClassLoader()
                .getResourceAsStream(propertiesResourceFileName);

        if (inputStream != null) {
            try {
                Properties properties = new Properties();
                properties.load(inputStream);
                System.getProperties().putAll(properties);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            System.err.printf("Property file %s not found%n", propertiesResourceFileName);
        }
    }

    private static void addCliArgsToSystemProperties(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("-D")) {
                arg = arg.substring(2);
                String[] split = arg.split("=");
                String key = split[0];
                String value = split[1];
                System.setProperty(key, value);
            }
        }
    }

    public static void setCursor(Node cursor) {
        if (cursor == D2D2.cursor) return;

        if (cursor != null) {
            Mouse.setVisible(false);
            cursor.removeEventListener(Mouse.class, SceneEvent.Tick.class);
            cursor.addEventListener(Mouse.class, SceneEvent.Tick.class, event -> cursor.setPosition(Mouse.getX(), Mouse.getY()));
        } else {
            Mouse.setVisible(true);
            D2D2.cursor.removeEventListener(Mouse.class, SceneEvent.Tick.class);
        }
        D2D2.cursor = cursor;
    }

    public static Root root() {
        return engine.root();
    }

    private static void startMainLoop() {
        engine.start();
    }

    public static void exit() {
        engine.stop();
    }

    public static TextureManager textureManager() {
        return textureManager;
    }

    public static BitmapFontManager bitmapFontManager() {
        return bitmapFontManager;
    }

    public static Engine engine() {
        return engine;
    }

    public static DisplayManager displayManager() {
        return engine.displayManager();
    }

    public static SoundManager soundManager() {
        return engine().soundManager();
    }

    public static void setNoScaleMode(boolean noScaleMode) {
        D2D2.noScaleMode = noScaleMode;

        engine().removeEventListener(D2D2.class, CommonEvent.Resize.class);
        if (noScaleMode) {
            engine().addEventListener(D2D2.class, CommonEvent.Resize.class, e ->
                    root().setSize(engine().getCanvasWidth(), engine().getCanvasHeight())
            );
        }
    }

    public static boolean isNoScaleMode() {
        return noScaleMode;
    }


}
