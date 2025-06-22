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

package com.ancevt.d2d2.engine;

import com.ancevt.d2d2.log.Log;
import com.ancevt.d2d2.scene.Renderer;
import com.ancevt.d2d2.scene.Stage;
import com.ancevt.d2d2.scene.shader.ShaderProgram;
import com.ancevt.d2d2.scene.text.BitmapFont;
import com.ancevt.d2d2.scene.text.FontBuilder;
import com.ancevt.d2d2.event.core.EventDispatcher;
import com.ancevt.d2d2.scene.texture.TextureManager;

public interface Engine extends EventDispatcher {

    void init();

    Stage getStage();

    void setAlwaysOnTop(boolean b);

    boolean isAlwaysOnTop();

    void setFrameRate(int value);

    int getFrameRate();

    int getActualFps();

    void start();

    Renderer getRenderer();

    void stop();

    void putStringToClipboard(String string);

    String getStringFromClipboard();

    default void setSmoothMode(boolean value) {
    }

    default boolean isSmoothMode() {
        return false;
    }

    BitmapFont generateBitmapFont(FontBuilder fontBuilder);

    void setTimerCheckFrameFrequency(int v);

    int getTimerCheckFrameFrequency();

    void setCursorXY(int x, int y);

    void setCanvasSize(int width, int height);

    int getCanvasWidth();

    int getCanvasHeight();

    Log logger();

    ShaderProgram createShaderProgram(String vertexShaderSource, String fragmentShaderSource);

    TextureManager getTextureManager();

    DisplayManager getDisplayManager();

    SoundManager getSoundManager();

    NodeFactory getNodeFactory();
}
