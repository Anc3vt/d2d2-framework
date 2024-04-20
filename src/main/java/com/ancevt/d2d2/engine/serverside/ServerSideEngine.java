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
package com.ancevt.d2d2.engine.serverside;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.TtfBitmapFontBuilder;
import com.ancevt.d2d2.engine.Engine;
import com.ancevt.d2d2.engine.VideoModeControl;
import com.ancevt.d2d2.engine.lwjgl.LwjglTextureEngine;
import com.ancevt.d2d2.event.LifecycleEvent;
import com.ancevt.d2d2.time.Timer;
import lombok.Getter;
import lombok.Setter;

public class ServerSideEngine implements Engine {

    private int width;
    private int height;
    private Stage stage;
    private String title;
    private IRenderer renderer;
    private boolean alive;
    private int frameRate = 60;
    private int frameCounter;
    private int fps = frameRate;
    private long time;
    private long tick;

    @Getter
    @Setter
    private int timerCheckFrameFrequency = 100;

    public ServerSideEngine(int width, int height) {
        D2D2.textureManager().setTextureEngine(new LwjglTextureEngine());
        setWindowSize(width, height);
    }

    @Override
    public VideoModeControl getVideoModeControl() {
        throw new RuntimeException("No video mode control in NoRenderBackend");
    }

    @Override
    public void focusWindow() {

    }

    @Override
    public void setAlwaysOnTop(boolean b) {

    }

    @Override
    public boolean isAlwaysOnTop() {
        return false;
    }

    @Override
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    @Override
    public int getFrameRate() {
        return frameRate;
    }

    @Override
    public int getActualFps() {
        return fps;
    }

    @Override
    public long getWindowId() {
        return 0;
    }

    @Override
    public void create() {
        stage = new Stage();
        stage.onResize(width, height);
        renderer = new RendererStub(stage);
    }

    @Override
    public void start() {
        alive = true;
        stage.dispatchEvent(
            LifecycleEvent.builder()
                .type(LifecycleEvent.START_MAIN_LOOP)
                .build()
        );
        startNoRenderLoop();
        stage.dispatchEvent(
            LifecycleEvent.builder()
                .type(LifecycleEvent.EXIT_MAIN_LOOP)
                .build()
        );
    }

    @Override
    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public IRenderer getRenderer() {
        return renderer;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void stop() {
        alive = false;
    }

    @Override
    public void setMouseVisible(boolean mouseVisible) {

    }

    @Override
    public boolean isMouseVisible() {
        return false;
    }

    @Override
    public void putToClipboard(String string) {

    }

    @Override
    public String getStringFromClipboard() {
        return null;
    }

    private void startNoRenderLoop() {
        while (alive) {
            try {
                renderer.renderFrame();
                if (fps > frameRate) {
                    Thread.sleep(1000 / (frameRate + 10));
                } else {
                    Thread.sleep((long) (1000 / (frameRate * 1.5f)));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frameCounter++;
            final long time2 = System.currentTimeMillis();

            if (time2 - time >= 1000) {
                time = System.currentTimeMillis();
                fps = frameCounter;
                frameCounter = 0;
            }

            tick++;

            if (tick % timerCheckFrameFrequency == 0) Timer.processTimers();
        }
    }

    @Override
    public void setWindowXY(int x, int y) {

    }

    @Override
    public int getWindowX() {
        return 0;
    }

    @Override
    public int getWindowY() {
        return 0;
    }

    @Override
    public BitmapFont generateBitmapFont(TtfBitmapFontBuilder ttfBitmapFontBuilder) {
        return D2D2.bitmapFontManager().getDefaultBitmapFont();
    }

    @Override
    public void setBorderless(boolean borderless) {

    }

    @Override
    public boolean isBorderless() {
        return false;
    }
}
