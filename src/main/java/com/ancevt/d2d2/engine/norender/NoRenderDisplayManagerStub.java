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
package com.ancevt.d2d2.engine.norender;

import com.ancevt.d2d2.engine.DisplayManager;
import com.ancevt.d2d2.engine.Monitor;
import com.ancevt.d2d2.engine.WindowState;

import java.util.List;

public class NoRenderDisplayManagerStub implements DisplayManager {
    @Override
    public List<Monitor> getMonitors() {
        return null;
    }

    @Override
    public Monitor getPrimaryMonitor() {
        return null;
    }

    @Override
    public void restoreWindowedMode() {

    }

    @Override
    public void setWindowSize(int width, int height) {

    }

    @Override
    public void setWindowXY(int x, int y) {

    }

    @Override
    public WindowState getWindowState() {
        return null;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setBorderless(boolean borderless) {

    }

    @Override
    public boolean isBorderless() {
        return false;
    }

    @Override
    public void focusWindow() {

    }

    @Override
    public long getWindowId() {
        return 0;
    }

    @Override
    public void setMouseVisible(boolean mouseVisible) {

    }

    @Override
    public boolean isMouseVisible() {
        return false;
    }
}
