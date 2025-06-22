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
import com.ancevt.d2d2.engine.NodeFactory;
import com.ancevt.d2d2.engine.SoundManager;
import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.log.Log;
import com.ancevt.d2d2.scene.Stage;
import com.ancevt.d2d2.scene.text.BitmapFontManager;
import com.ancevt.d2d2.scene.texture.TextureManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class D2D2 {

    @Getter
    private static BitmapFontManager bitmapFontManager;

    @Getter
    private static Engine engine;

    @Getter
    private static D2D2Application application;

    @Getter
    private static D2D2Config config;

    @Getter
    private static boolean noScaleMode;

    public static Log log;

    public static void init(D2D2Application application, D2D2Config config) {
        D2D2.application = application;
        D2D2.config = config;
        addPropertiesToSystemProperties(config.asMap());

        int width = config.getOrDefault(D2D2Config.WIDTH, 800);
        int height = config.getOrDefault(D2D2Config.HEIGHT, 600);
        String title = config.getOrDefault(D2D2Config.TITLE, "D2D2 Application");
        boolean noScaleMode = config.getOrDefault(D2D2Config.NO_SCALE_MODE, false);
        String engineClassName = config.getOrDefault(D2D2Config.ENGINE, "com.ancevt.d2d2.engine.lwjgl.LwjglEngine");

        Engine engine = createEngine(engineClassName, width, height, title);

        log = engine.logger();

        log.info(D2D2.class, "D2D2 initialized with engine: <b>%s<>".formatted(engine.getClass().getName()));

        Stage stage = createStage(engine);

        setNoScaleMode(noScaleMode);
        application.start(stage);
        startMainLoop();
        application.stop();
    }

    public static void init(D2D2Application application) {
        init(application, new D2D2Config());
    }

    private static void addPropertiesToSystemProperties(Map<String, String> properties) {
        properties.forEach((key, value) -> {
            System.setProperty(key, value);
            System.err.printf("D2D2: %s=%s%n", key, value);
        });
    }

    private static Stage createStage(Engine engine) {
        D2D2.engine = engine;
        engine.init();
        bitmapFontManager = new BitmapFontManager();
        return engine.getStage();
    }

    private static Engine createEngine(String engineClassName, int width, int height, String titleText) {
        try {
            return (Engine) Class.forName(engineClassName)
                    .getConstructor(int.class, int.class, String.class)
                    .newInstance(width, height, titleText);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Stage getStage() {
        return engine.getStage();
    }

    private static void startMainLoop() {
        engine.start();
    }

    public static void exit() {
        engine.stop();
    }

    public static void setNoScaleMode(boolean noScaleMode) {
        D2D2.noScaleMode = noScaleMode;

        getEngine().removeEventListener(D2D2.class, CommonEvent.Resize.class);
        if (noScaleMode) {
            getEngine().addEventListener(D2D2.class, CommonEvent.Resize.class, e ->
                    getStage().setSize(getEngine().getCanvasWidth(), getEngine().getCanvasHeight())
            );
        }
    }

    public static DisplayManager getDisplayManager() {
        return engine.getDisplayManager();
    }

    public static SoundManager getSoundManager() {
        return engine.getSoundManager();
    }

    public static NodeFactory getNodeFactory() {
        return engine.getNodeFactory();
    }

    public static TextureManager getTextureManager() {
        return getEngine().getTextureManager();
    }

}
