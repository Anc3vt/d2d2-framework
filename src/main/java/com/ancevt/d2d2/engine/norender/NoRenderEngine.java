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

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Renderer;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.TrueTypeBitmapFontBuilder;
import com.ancevt.d2d2.engine.DisplayManager;
import com.ancevt.d2d2.engine.Engine;
import com.ancevt.d2d2.event.LifecycleEvent;
import com.ancevt.d2d2.exception.NotImplementedException;
import com.ancevt.d2d2.time.Timer;
import lombok.Getter;
import lombok.Setter;

public class NoRenderEngine implements Engine {

    private final int initialWidth;
    private final int initialHeight;
    private Stage stage;
    private String title;
    private Renderer renderer;
    private boolean alive;
    private int frameRate = 60;
    private int frameCounter;
    private int fps = frameRate;
    private long time;
    private long tick;

    @Getter
    @Setter
    private int timerCheckFrameFrequency = 100;

    public NoRenderEngine(int initialWidth, int initialHeight, String title) {
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        D2D2.getTextureManager().setTextureEngine(new NoRenderTextureEngine());

    }

    @Override
    public DisplayManager getDisplayManager() {
        throw new NotImplementedException("No monitors in no-render engine");
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
    public void create() {
        stage = new Stage();
        stage.setSize(initialWidth, initialHeight);
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
    public Stage getStage() {
        return stage;
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public void stop() {
        alive = false;
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
    public BitmapFont generateBitmapFont(TrueTypeBitmapFontBuilder trueTypeBitmapFontBuilder) {
        return D2D2.getBitmapFontManager().getDefaultBitmapFont();
    }


}
