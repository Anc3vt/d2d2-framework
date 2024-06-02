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
package com.ancevt.d2d2.display.interactive;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Colored;
import com.ancevt.d2d2.display.SimpleSprite;
import com.ancevt.d2d2.display.texture.TextureClip;
import lombok.Getter;

public class Combined9Sprites extends InteractiveContainer implements Colored {

    private final SimpleSprite topLeft;
    private final SimpleSprite top;
    private final SimpleSprite topRight;
    private final SimpleSprite left;
    private final SimpleSprite center;
    private final SimpleSprite right;
    private final SimpleSprite bottomLeft;
    private final SimpleSprite bottom;
    private final SimpleSprite bottomRight;

    @Getter
    private boolean repeatsEnabled;


    public Combined9Sprites() {
        topLeft = new SimpleSprite();
        top = new SimpleSprite();
        topRight = new SimpleSprite();
        left = new SimpleSprite();
        center = new SimpleSprite();
        right = new SimpleSprite();
        bottomLeft = new SimpleSprite();
        bottom = new SimpleSprite();
        bottomRight = new SimpleSprite();

        addChild(topLeft);
        addChild(top);
        addChild(topRight);
        addChild(left);
        addChild(center);
        addChild(right);
        addChild(bottomLeft);
        addChild(bottom);
        addChild(bottomRight);

        setEnabled(false);
    }

    public Combined9Sprites(String[] textureKeys) {
        this();
        setTextures(textureKeys);
    }

    public Combined9Sprites(TextureClip topLeftTextureClip,
                            TextureClip topTextureClip,
                            TextureClip topRightTextureClip,
                            TextureClip leftTextureClip,
                            TextureClip centerTextureClip,
                            TextureClip rightTextureClip,
                            TextureClip bottomLeftTextureClip,
                            TextureClip bottomTextureClip,
                            TextureClip bottomRightTextureClip) {
        this();
        setTextures(topLeftTextureClip, topTextureClip, topRightTextureClip,
            leftTextureClip, centerTextureClip, rightTextureClip,
            bottomLeftTextureClip, bottomTextureClip, bottomRightTextureClip);

    }

    public Combined9Sprites(TextureClip all9PartsTextureClip, int partWidth, int partHeight) {
        this();
        setTextures(all9PartsTextureClip, partWidth, partHeight);
    }

    @Override
    public void setColor(Color color) {
        topLeft.setColor(color);
        top.setColor(color);
        topRight.setColor(color);
        left.setColor(color);
        center.setColor(color);
        right.setColor(color);
        bottomLeft.setColor(color);
        bottom.setColor(color);
        bottomRight.setColor(color);
    }

    @Override
    public void setColor(int rgb) {
        topLeft.setColor(Color.of(rgb));
        top.setColor(Color.of(rgb));
        topRight.setColor(Color.of(rgb));
        left.setColor(Color.of(rgb));
        center.setColor(Color.of(rgb));
        right.setColor(Color.of(rgb));
        bottomLeft.setColor(Color.of(rgb));
        bottom.setColor(Color.of(rgb));
        bottomRight.setColor(Color.of(rgb));
    }

    @Override
    public Color getColor() {
        return topLeft.getColor();
    }

    public void setRepeatsEnabled(boolean repeatsEnabled) {
        this.repeatsEnabled = repeatsEnabled;
        rebuild();
    }

    public void setTextures(TextureClip all9PartsTextureClip, int partWidth, int partHeight) {
        setTextures(
            all9PartsTextureClip.getSubTexture(0, 0, partWidth, partHeight),
            all9PartsTextureClip.getSubTexture(partWidth, 0, partWidth, partHeight),
            all9PartsTextureClip.getSubTexture(partWidth * 2, 0, partWidth, partHeight),

            all9PartsTextureClip.getSubTexture(0, partHeight, partWidth, partHeight),
            all9PartsTextureClip.getSubTexture(partWidth, partHeight, partWidth, partHeight),
            all9PartsTextureClip.getSubTexture(partWidth * 2, partHeight, partWidth, partHeight),

            all9PartsTextureClip.getSubTexture(0, partHeight * 2, partWidth, partHeight),
            all9PartsTextureClip.getSubTexture(partWidth, partHeight * 2, partWidth, partHeight),
            all9PartsTextureClip.getSubTexture(partWidth * 2, partHeight * 2, partWidth, partHeight)
        );
    }

    public void setTextures(TextureClip topLeftTextureClip,
                            TextureClip topTextureClip,
                            TextureClip topRightTextureClip,
                            TextureClip leftTextureClip,
                            TextureClip centerTextureClip,
                            TextureClip rightTextureClip,
                            TextureClip bottomLeftTextureClip,
                            TextureClip bottomTextureClip,
                            TextureClip bottomRightTextureClip) {

        topLeft.setTexture(topLeftTextureClip);
        top.setTexture(topTextureClip);
        topRight.setTexture(topRightTextureClip);
        left.setTexture(leftTextureClip);
        center.setTexture(centerTextureClip);
        right.setTexture(rightTextureClip);
        bottomLeft.setTexture(bottomLeftTextureClip);
        bottom.setTexture(bottomTextureClip);
        bottomRight.setTexture(bottomRightTextureClip);
        rebuild();
    }

    public void setTextures(String[] textureKeys) {
        topLeft.setTexture(D2D2.getTextureManager().getTexture(textureKeys[0]));
        top.setTexture(D2D2.getTextureManager().getTexture(textureKeys[1]));
        topRight.setTexture(D2D2.getTextureManager().getTexture(textureKeys[2]));
        left.setTexture(D2D2.getTextureManager().getTexture(textureKeys[3]));
        center.setTexture(D2D2.getTextureManager().getTexture(textureKeys[4]));
        right.setTexture(D2D2.getTextureManager().getTexture(textureKeys[5]));
        bottomLeft.setTexture(D2D2.getTextureManager().getTexture(textureKeys[6]));
        bottom.setTexture(D2D2.getTextureManager().getTexture(textureKeys[7]));
        bottomRight.setTexture(D2D2.getTextureManager().getTexture(textureKeys[8]));
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        rebuild();
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        rebuild();
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        rebuild();
    }

    private void rebuild() {
        topRight.setXY(getWidth() - topRight.getWidth(), 0);
        bottomLeft.setXY(0, getHeight() - bottomLeft.getHeight());
        bottomRight.setXY(getWidth() - bottomRight.getWidth(), getHeight() - bottomRight.getHeight());
        top.setX(topLeft.getWidth());
        bottom.setXY(bottomLeft.getWidth(), getHeight() - bottom.getHeight());
        left.setY(topLeft.getHeight());
        right.setXY(getWidth() - right.getWidth(), topRight.getHeight());
        center.setXY(topLeft.getWidth(), topLeft.getHeight());

        if (repeatsEnabled) {
            int textureLength;
            int targetRepeat;

            textureLength = top.getTexture().getWidth();
            targetRepeat = (int) (getWidth() - topLeft.getWidth() - topRight.getWidth());
            targetRepeat /= textureLength;
            top.setScale(1.0f, 1.0f);
            top.setRepeatX(targetRepeat);

            textureLength = bottom.getTexture().getWidth();
            targetRepeat = (int) (getWidth() - bottomLeft.getWidth() - bottomRight.getWidth());
            targetRepeat /= textureLength;
            bottom.setScale(1.0f, 1.0f);
            bottom.setRepeatX(targetRepeat);

            textureLength = left.getTexture().getHeight();
            targetRepeat = (int) (getHeight() - topLeft.getHeight() - bottomLeft.getHeight());
            targetRepeat /= textureLength;
            left.setScale(1.0f, 1.0f);
            left.setRepeatY(targetRepeat);

            textureLength = right.getTexture().getHeight();
            targetRepeat = (int) (getHeight() - topRight.getHeight() - bottomRight.getHeight());
            targetRepeat /= textureLength;
            right.setScale(1.0f, 1.0f);
            right.setRepeatY(targetRepeat);

            int centerTextureWidth = center.getTexture().getWidth();
            int centerRepeatX = (int) (getWidth() - left.getWidth() - right.getWidth());
            centerRepeatX /= centerTextureWidth;

            int centerTextureHeight = center.getTexture().getHeight();
            int centerREpeatY = (int) (getHeight() - top.getHeight() - bottom.getHeight());
            centerREpeatY /= centerTextureHeight;

            center.setScale(1.0f, 1.0f);
            center.setRepeat(centerRepeatX, centerREpeatY);

        } else {
            float textureLength;
            float targetScale;

            textureLength = top.getTexture().getWidth();
            targetScale = getWidth() - topLeft.getWidth() - topRight.getWidth();
            targetScale /= textureLength;
            top.setRepeat(1, 1);
            top.setScaleX(targetScale);

            textureLength = bottom.getTexture().getWidth();
            targetScale = getWidth() - bottomLeft.getWidth() - bottomRight.getWidth();
            targetScale /= textureLength;
            bottom.setRepeat(1, 1);
            bottom.setScaleX(targetScale);

            textureLength = left.getTexture().getHeight();
            targetScale = getHeight() - topLeft.getHeight() - bottomLeft.getHeight();
            targetScale /= textureLength;
            left.setRepeat(1, 1);
            left.setScaleY(targetScale);

            textureLength = right.getTexture().getHeight();
            targetScale = getHeight() - topRight.getHeight() - bottomRight.getHeight();
            targetScale /= textureLength;
            right.setRepeat(1, 1);
            right.setScaleY(targetScale);

            float centerTextureWidth = center.getTexture().getWidth();
            float centerScaleX = getWidth() - left.getWidth() - right.getWidth();
            centerScaleX /= centerTextureWidth;

            float centerTextureHeight = center.getTexture().getHeight();
            float centerScaleY = getHeight() - top.getHeight() - bottom.getHeight();
            centerScaleY /= centerTextureHeight;
            center.setRepeat(1, 1);
            center.setScale(centerScaleX, centerScaleY);
        }
    }
}
