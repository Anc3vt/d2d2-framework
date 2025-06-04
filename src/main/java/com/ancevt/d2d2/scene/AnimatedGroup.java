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

public class AnimatedGroup extends BasicGroup implements Animated {

    private Sprite[] frames;

    private boolean playing;
    private boolean loop;
    private int slowing = DEFAULT_SLOWING;
    private int slowingCounter;
    private int currentFrameIndex;
    private Sprite currentSprite;
    private boolean backward;

    public AnimatedGroup() {
        setName("_" + getClass().getSimpleName() + getNodeId());
    }

    public AnimatedGroup(Sprite[] frameSprites) {
        this();
        setFrameSprites(frameSprites);
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
            if (backward) prevFrame();
            else nextFrame();
        }
    }

    @Override
    public void setLoop(boolean b) {
        this.loop = b;
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

        if (this.currentFrameIndex >= frames.length) {
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

    private void drawCurrentFrame() {
        if (currentSprite != null && currentSprite.getParent() != null) {
            currentSprite.removeFromParent();
        }

        currentSprite = frames[currentFrameIndex];
        if (currentSprite != null) {
            this.addChild(currentSprite);
        }
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    public void setFrameSprites(Sprite[] sprites) {
        frames = sprites;
        if (sprites.length > 0) {
            setFrame(0);
        }
    }

    public Sprite[] getFrameSprites() {
        return frames;
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
    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }

    @Override
    public int getNumFrames() {
        return frames.length;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "playing=" + playing +
                ", loop=" + loop +
                ", slowing=" + slowing +
                ", frameIndex=" + currentFrameIndex +
                '}';
    }
}
