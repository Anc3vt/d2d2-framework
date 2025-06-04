/**
 * Copyright (C) 2025 the original author or authors.
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

package com.ancevt.d2d2.scene.interactive;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Colored;
import com.ancevt.d2d2.scene.BasicSprite;
import com.ancevt.d2d2.scene.texture.TextureRegion;
import lombok.Getter;

public class Combined9Sprites extends InteractiveGroup implements Colored {

    private final BasicSprite topLeft;
    private final BasicSprite top;
    private final BasicSprite topRight;
    private final BasicSprite left;
    private final BasicSprite center;
    private final BasicSprite right;
    private final BasicSprite bottomLeft;
    private final BasicSprite bottom;
    private final BasicSprite bottomRight;

    @Getter
    private boolean repeatsEnabled;


    public Combined9Sprites() {
        topLeft = new BasicSprite();
        top = new BasicSprite();
        topRight = new BasicSprite();
        left = new BasicSprite();
        center = new BasicSprite();
        right = new BasicSprite();
        bottomLeft = new BasicSprite();
        bottom = new BasicSprite();
        bottomRight = new BasicSprite();

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

    public Combined9Sprites(TextureRegion topLeftTextureRegion,
                            TextureRegion topTextureRegion,
                            TextureRegion topRightTextureRegion,
                            TextureRegion leftTextureRegion,
                            TextureRegion centerTextureRegion,
                            TextureRegion rightTextureRegion,
                            TextureRegion bottomLeftTextureRegion,
                            TextureRegion bottomTextureRegion,
                            TextureRegion bottomRightTextureRegion) {
        this();
        setTextures(topLeftTextureRegion, topTextureRegion, topRightTextureRegion,
                leftTextureRegion, centerTextureRegion, rightTextureRegion,
                bottomLeftTextureRegion, bottomTextureRegion, bottomRightTextureRegion);

    }

    public Combined9Sprites(TextureRegion all9PartsTextureRegion, int partWidth, int partHeight) {
        this();
        setTextures(all9PartsTextureRegion, partWidth, partHeight);
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

    public void setTextures(TextureRegion all9PartsTextureRegion, int partWidth, int partHeight) {
        setTextures(
            all9PartsTextureRegion.createSubregion(0, 0, partWidth, partHeight),
            all9PartsTextureRegion.createSubregion(partWidth, 0, partWidth, partHeight),
            all9PartsTextureRegion.createSubregion(partWidth * 2, 0, partWidth, partHeight),

            all9PartsTextureRegion.createSubregion(0, partHeight, partWidth, partHeight),
            all9PartsTextureRegion.createSubregion(partWidth, partHeight, partWidth, partHeight),
            all9PartsTextureRegion.createSubregion(partWidth * 2, partHeight, partWidth, partHeight),

            all9PartsTextureRegion.createSubregion(0, partHeight * 2, partWidth, partHeight),
            all9PartsTextureRegion.createSubregion(partWidth, partHeight * 2, partWidth, partHeight),
            all9PartsTextureRegion.createSubregion(partWidth * 2, partHeight * 2, partWidth, partHeight)
        );
    }

    public void setTextures(TextureRegion topLeftTextureRegion,
                            TextureRegion topTextureRegion,
                            TextureRegion topRightTextureRegion,
                            TextureRegion leftTextureRegion,
                            TextureRegion centerTextureRegion,
                            TextureRegion rightTextureRegion,
                            TextureRegion bottomLeftTextureRegion,
                            TextureRegion bottomTextureRegion,
                            TextureRegion bottomRightTextureRegion) {

        topLeft.setTextureRegion(topLeftTextureRegion);
        top.setTextureRegion(topTextureRegion);
        topRight.setTextureRegion(topRightTextureRegion);
        left.setTextureRegion(leftTextureRegion);
        center.setTextureRegion(centerTextureRegion);
        right.setTextureRegion(rightTextureRegion);
        bottomLeft.setTextureRegion(bottomLeftTextureRegion);
        bottom.setTextureRegion(bottomTextureRegion);
        bottomRight.setTextureRegion(bottomRightTextureRegion);
        rebuild();
    }

    public void setTextures(String[] textureKeys) {
//        topLeft.setTextureRegion(D2D2.getTextureEngine().getTextureRegion(textureKeys[0]));
//        top.setTextureRegion(D2D2.textureManager().getTextureRegion(textureKeys[1]));
//        topRight.setTextureRegion(D2D2.textureManager().getTextureRegion(textureKeys[2]));
//        left.setTextureRegion(D2D2.textureManager().getTextureRegion(textureKeys[3]));
//        center.setTextureRegion(D2D2.textureManager().getTextureRegion(textureKeys[4]));
//        right.setTextureRegion(D2D2.textureManager().getTextureRegion(textureKeys[5]));
//        bottomLeft.setTextureRegion(D2D2.textureManager().getTextureRegion(textureKeys[6]));
//        bottom.setTextureRegion(D2D2.textureManager().getTextureRegion(textureKeys[7]));
//        bottomRight.setTextureRegion(D2D2.textureManager().getTextureRegion(textureKeys[8]));
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
        topRight.setPosition(getWidth() - topRight.getWidth(), 0);
        bottomLeft.setPosition(0, getHeight() - bottomLeft.getHeight());
        bottomRight.setPosition(getWidth() - bottomRight.getWidth(), getHeight() - bottomRight.getHeight());
        top.setX(topLeft.getWidth());
        bottom.setPosition(bottomLeft.getWidth(), getHeight() - bottom.getHeight());
        left.setY(topLeft.getHeight());
        right.setPosition(getWidth() - right.getWidth(), topRight.getHeight());
        center.setPosition(topLeft.getWidth(), topLeft.getHeight());

        if (repeatsEnabled) {
            int textureLength;
            int targetRepeat;

            textureLength = top.getTextureRegion().getWidth();
            targetRepeat = (int) (getWidth() - topLeft.getWidth() - topRight.getWidth());
            targetRepeat /= textureLength;
            top.setScale(1.0f, 1.0f);
            top.setRepeatX(targetRepeat);

            textureLength = bottom.getTextureRegion().getWidth();
            targetRepeat = (int) (getWidth() - bottomLeft.getWidth() - bottomRight.getWidth());
            targetRepeat /= textureLength;
            bottom.setScale(1.0f, 1.0f);
            bottom.setRepeatX(targetRepeat);

            textureLength = left.getTextureRegion().getHeight();
            targetRepeat = (int) (getHeight() - topLeft.getHeight() - bottomLeft.getHeight());
            targetRepeat /= textureLength;
            left.setScale(1.0f, 1.0f);
            left.setRepeatY(targetRepeat);

            textureLength = right.getTextureRegion().getHeight();
            targetRepeat = (int) (getHeight() - topRight.getHeight() - bottomRight.getHeight());
            targetRepeat /= textureLength;
            right.setScale(1.0f, 1.0f);
            right.setRepeatY(targetRepeat);

            int centerTextureWidth = center.getTextureRegion().getWidth();
            int centerRepeatX = (int) (getWidth() - left.getWidth() - right.getWidth());
            centerRepeatX /= centerTextureWidth;

            int centerTextureHeight = center.getTextureRegion().getHeight();
            int centerREpeatY = (int) (getHeight() - top.getHeight() - bottom.getHeight());
            centerREpeatY /= centerTextureHeight;

            center.setScale(1.0f, 1.0f);
            center.setRepeat(centerRepeatX, centerREpeatY);

        } else {
            float textureLength;
            float targetScale;

            textureLength = top.getTextureRegion().getWidth();
            targetScale = getWidth() - topLeft.getWidth() - topRight.getWidth();
            targetScale /= textureLength;
            top.setRepeat(1, 1);
            top.setScaleX(targetScale);

            textureLength = bottom.getTextureRegion().getWidth();
            targetScale = getWidth() - bottomLeft.getWidth() - bottomRight.getWidth();
            targetScale /= textureLength;
            bottom.setRepeat(1, 1);
            bottom.setScaleX(targetScale);

            textureLength = left.getTextureRegion().getHeight();
            targetScale = getHeight() - topLeft.getHeight() - bottomLeft.getHeight();
            targetScale /= textureLength;
            left.setRepeat(1, 1);
            left.setScaleY(targetScale);

            textureLength = right.getTextureRegion().getHeight();
            targetScale = getHeight() - topRight.getHeight() - bottomRight.getHeight();
            targetScale /= textureLength;
            right.setRepeat(1, 1);
            right.setScaleY(targetScale);

            float centerTextureWidth = center.getTextureRegion().getWidth();
            float centerScaleX = getWidth() - left.getWidth() - right.getWidth();
            centerScaleX /= centerTextureWidth;

            float centerTextureHeight = center.getTextureRegion().getHeight();
            float centerScaleY = getHeight() - top.getHeight() - bottom.getHeight();
            centerScaleY /= centerTextureHeight;
            center.setRepeat(1, 1);
            center.setScale(centerScaleX, centerScaleY);
        }
    }
}
