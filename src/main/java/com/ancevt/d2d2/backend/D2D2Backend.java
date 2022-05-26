/*
 *     Copyright 2015-2022 Ancevt (me@ancevt.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.backend;

import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.ShaderProgram;
import com.ancevt.d2d2.display.Stage;

public interface D2D2Backend {

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

    default boolean isSmoothMode() { return false; }

    void setWindowXY(int x, int y);

    int getWindowX();

    int getWindowY();

    int prepareShaderProgram(ShaderProgram shaderProgram);

    void disposeShaderProgram(ShaderProgram shaderProgram);
}
