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
package com.ancevt.d2d2.backend;

import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapFontBuilder;

public interface D2D2Backend {

    void setAlwaysOnTop(boolean b);

    boolean isAlwaysOnTop();

    void setFrameRate(int value);

    int getFrameRate();

    int getFps();

    long getWindowId();

    void create();

    void start();

    void setWindowSize(int width, int height);

    int getWidth();

    int getHeight();

    void setTitle(String title);

    String getTitle();

    Stage getStage();

    void setVisible(boolean visible);

    boolean isVisible();

    IRenderer getRenderer();

    void stop();

    void setMouseVisible(boolean mouseVisible);

    boolean isMouseVisible();

    void putToClipboard(String string);

    String getStringFromClipboard();

    boolean isFullscreen();

    void setFullscreen(boolean value);

    default void setSmoothMode(boolean value) {}

    default boolean isSmoothMode() {return false;}

    void setWindowXY(int x, int y);

    int getWindowX();

    int getWindowY();

    BitmapFont generateBitmapFont(BitmapFontBuilder bitmapFontBuilder);

    void setBorderless(boolean borderless);

    boolean isBorderless();

    void setTimerCheckFrameFrequency(int v);

    int getTimerCheckFrameFrequency();
}
