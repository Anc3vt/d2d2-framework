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
package com.ancevt.d2d2.display;

import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;

public class FramedDisplayObjectContainer extends DisplayObjectContainer implements IFramedDisplayObject {

    private ISprite[] frames;

    private boolean playing;
    private boolean loop;
    private int slowing;
    private int slowingCounter;
    private int currentFrameIndex;
    private ISprite currentSprite;
    private boolean backward;

    public FramedDisplayObjectContainer(ISprite[] frameSprites, boolean cloneEach) {
        this();
        setFrameSprites(frameSprites, cloneEach);
    }

    public FramedDisplayObjectContainer(ISprite[] frameSprites) {
        this(frameSprites, false);
    }

    public FramedDisplayObjectContainer(Texture[] textures) {
        this();
        setFrameTextures(textures);
    }

    public FramedDisplayObjectContainer() {
        frames = new ISprite[0];
        slowingCounter = 0;
        setLoop(false);
        stop();
        setSlowing(DEFAULT_SLOWING);
        setName("_" + getClass().getSimpleName() + displayObjectId());
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
        if (!playing)
            return;

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

        if (this.currentFrameIndex >= frames.length) {
            if (loop) {
                this.currentFrameIndex = 0;
                dispatchEvent(EventPool.createEvent(Event.COMPLETE, this));
                play();
            } else {
                this.currentFrameIndex--;
                stop();
                dispatchEvent(EventPool.createEvent(Event.COMPLETE, this));
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
            this.add(currentSprite);
        }
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void setFrameTextures(Texture[] textures) {
        frames = new ISprite[textures.length];
        for (int i = 0; i < textures.length; i++) {
            frames[i] = new Sprite(textures[i]);
        }
        if (frames.length > 0) {
            setFrame(0);
        }
    }

    @Override
    public Texture[] getFrameTextures() {
        Texture[] textures = new Texture[frames.length];
        for (int i = 0; i < frames.length; i++) {
            textures[i] = frames[i].getTexture();
        }
        return textures;
    }

    @Override
    public void setFrameSprites(ISprite[] sprites, boolean cloneEach) {
        if (!cloneEach) {
            frames = sprites;
        } else {
            frames = new ISprite[sprites.length];
            for (int i = 0; i < sprites.length; i++) {
                ISprite frame = sprites[i].cloneSprite();
                frames[i] = frame;
            }
        }
        if (sprites.length > 0) {
            setFrame(0);
        }
    }

    @Override
    public void setFrameSprites(ISprite[] sprites) {
        setFrameSprites(sprites, false);
    }

    @Override
    public ISprite[] getFrameSprites() {
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
    public int getFrame() {
        return currentFrameIndex;
    }

    @Override
    public int getFrameCount() {
        return frames.length;
    }

    public final String getFrameListString() {
        final StringBuilder sb = new StringBuilder();

        for (ISprite frame : frames) {
            sb.append(frame).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "FramedDisplayObjectContainer{" +
                "playing=" + playing +
                ", loop=" + loop +
                ", slowing=" + slowing +
                ", frameIndex=" + currentFrameIndex +
                '}';
    }
}
