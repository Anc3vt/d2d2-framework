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

package com.ancevt.d2d2.scene;

import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.scene.texture.TextureRegion;
import lombok.Getter;
import lombok.Setter;

public class AnimatedSprite extends SpriteImpl implements Animated {

    @Getter
    private TextureRegion[] frameTextureRegions;

    @Getter
    private boolean playing;

    @Getter
    @Setter
    private boolean loop;

    @Getter
    @Setter
    private int slowing = DEFAULT_SLOWING;
    private int slowingCounter;
    private int currentFrameIndex;

    @Getter
    @Setter
    private boolean backward;

    public AnimatedSprite(TextureRegion[] textureRegions) {
        this();
        setFrameTextureRegions(textureRegions);
    }

    public AnimatedSprite() {
        setName("_" + getClass().getSimpleName() + getNodeId());
    }

    public static AnimatedSprite create() {
        return new AnimatedSprite();
    }

    public static AnimatedSprite create(TextureRegion[] textureRegions) {
        return new AnimatedSprite(textureRegions);
    }

    @Override
    public void processFrame() {
        if (!playing) return;

        slowingCounter++;
        if (slowingCounter >= slowing) {
            slowingCounter = 0;
            if (backward) prevFrame();
            else nextFrame();
        }
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
            if (loop) {
                currentFrameIndex = getNumFrames() - 1;
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

        if (this.currentFrameIndex >= frameTextureRegions.length) {
            if (loop) {
                this.currentFrameIndex = 0;
                dispatchEvent(CommonEvent.Complete.create());
                play();
            } else {
                this.currentFrameIndex--;
                stop();
                dispatchEvent(CommonEvent.Complete.create());
            }
        }

        drawCurrentFrame();
    }

    @Override
    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    @Override
    public int getNumFrames() {
        return frameTextureRegions.length;
    }

    @Override
    public void play() {
        playing = true;
    }

    @Override
    public void stop() {
        playing = false;
    }

    public void setFrameTextureRegions(TextureRegion[] textureRegions) {
        this.frameTextureRegions = textureRegions;
        if (textureRegions.length > 0) {
            setFrame(0);
        }
    }

    private void drawCurrentFrame() {
        super.setTextureRegion(frameTextureRegions[currentFrameIndex]);
    }

    @Override
    public void setTextureRegion(TextureRegion value) {
        throw new IllegalStateException("Unable to set texture directly. Use setFrameTextures([]) instead");
    }
}






