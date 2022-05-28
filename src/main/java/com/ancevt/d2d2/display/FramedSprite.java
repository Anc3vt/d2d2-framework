/**
 * Copyright (C) 2022 the original author or authors.
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
package com.ancevt.d2d2.display;

import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;
import org.jetbrains.annotations.NotNull;

public class FramedSprite extends Sprite implements IFramedDisplayObject {

    private Texture[] frameTextures;

    private boolean playing;
    private boolean loop;
    private int slowing;
    private int slowingCounter;
    private int currentFrameIndex;
    private boolean backward;

    public FramedSprite() {
        frameTextures = new Texture[0];
        slowingCounter = 0;
        setLoop(false);
        stop();
        setSlowing(DEFAULT_SLOWING);
        setName("_" + getClass().getSimpleName() + displayObjectId());
    }

    public FramedSprite(ISprite[] frameSprites, boolean cloneEach) {
        this();
        setFrameSprites(frameSprites, cloneEach);
    }

    public FramedSprite(ISprite[] frameSprites) {
        this(frameSprites, false);
    }

    public FramedSprite(Texture[] textures) {
        this();
        setFrameTextures(textures);
    }

    @Override
    public void setBackward(boolean backward) {
        this.backward = backward;
    }

    @Override
    public boolean isBackward() {
        return backward;
    }

    @Override
    public void processFrame() {
        if (!playing) return;

        slowingCounter++;
        if (slowingCounter >= slowing) {
            slowingCounter = 0;
            if(backward) prevFrame(); else nextFrame();
        }
    }

    @Override
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public boolean isLoop() {
        return loop;
    }

    @Override
    public void setSlowing(int slowing) {
        this.slowing = slowing;
    }

    @Override
    public int getSlowing() {
        return slowing;
    }

    @Override
    public void nextFrame() {
        setFrame(++currentFrameIndex);
        drawCurrentFrame();
    }

    @Override
    public void prevFrame() {
        currentFrameIndex--;
        if (currentFrameIndex < 0) {
            if(loop) {
                currentFrameIndex = getFrameCount() - 1;
            } else {
                currentFrameIndex = 0;
            }
        }
        drawCurrentFrame();
    }

    @Override
    public void setFrame(int frameIndex) {
        this.currentFrameIndex = frameIndex;
        slowingCounter = 0;

        if (this.currentFrameIndex >= frameTextures.length) {
            if (loop) {
                this.currentFrameIndex = 0;
                dispatchEvent(EventPool.createEvent(Event.COMPLETE));
                play();
            } else {
                this.currentFrameIndex--;
                stop();
                dispatchEvent(EventPool.createEvent(Event.COMPLETE));
            }
        }

        drawCurrentFrame();
    }

    @Override
    public int getFrame() {
        return currentFrameIndex;
    }

    @Override
    public int getFrameCount() {
        return frameTextures.length;
    }

    @Override
    public void play() {
        playing = true;
    }

    @Override
    public void stop() {
        playing = false;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void setFrameTextures(Texture[] textures) {
        this.frameTextures = textures;
        if (textures.length > 0) {
            setFrame(0);
        }
    }

    @Override
    public Texture[] getFrameTextures() {
        return this.frameTextures;
    }

    @Override
    public void setFrameSprites(ISprite @NotNull [] sprites, boolean cloneEach) {
        Texture[] textures = new Texture[sprites.length];
        for (int i = 0; i < sprites.length; i++) {
            textures[i] = sprites[i].getTexture();
        }
        setFrameTextures(textures);
    }

    @Override
    public void setFrameSprites(ISprite[] sprites) {
        setFrameSprites(sprites, false);
    }

    @Override
    public ISprite[] getFrameSprites() {
        ISprite[] sprites = new Sprite[frameTextures.length];
        for (int i = 0; i < frameTextures.length; i++) {
            sprites[i] = new Sprite(frameTextures[i]);
        }
        return sprites;
    }

    private void drawCurrentFrame() {
        super.setTexture(frameTextures[currentFrameIndex]);
    }

    @Override
    public void setTexture(Texture value) {
        throw new IllegalStateException("Unable to set texture directly. Use setFrameTextures([]) instead");
    }
}






