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

import com.ancevt.d2d2.scene.texture.TextureRegion;

public class BasicSprite extends AbstractNode implements Sprite {

    private float repeatX;
    private float repeatY;
    private Color color;
    private TextureRegion textureRegion;
    private double vertexBleedingFix = 0d;
    private double textureBleedingFix = 0d;

    public BasicSprite() {
        setColor(DEFAULT_COLOR);
        setRepeat(1, 1);
        setName("_" + getClass().getSimpleName() + getNodeId());
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setColor(int rgb) {
        setColor(new Color(rgb));
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setRepeat(float repeatX, float repeatY) {
        setRepeatX(repeatX);
        setRepeatY(repeatY);
    }

    @Override
    public void setRepeatX(float value) {
        this.repeatX = value;
    }

    @Override
    public void setRepeatY(float value) {
        this.repeatY = value;
    }

    @Override
    public float getRepeatX() {
        return repeatX;
    }

    @Override
    public float getRepeatY() {
        return repeatY;
    }

    @Override
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    @Override
    public void setTextureRegion(TextureRegion value) {
        this.textureRegion = value;
        if (textureRegion != null && textureRegion.getTexture().isDisposed()) {
            //TODO: uncomment following:
            //throw new IllegalStateException("Texture " + textureRegion.getTexture().getId() + " is disposed");
        }
    }

    @Override
    public float getWidth() {
        return textureRegion == null ? 0f : textureRegion.getWidth();
    }

    @Override
    public float getHeight() {
        return textureRegion == null ? 0f : textureRegion.getHeight();
    }

    @Override
    public Sprite cloneSprite() {
        Sprite result = engine.getNodeFactory().createSprite(getTextureRegion());
        result.setPosition(getX(), getY());
        result.setRepeat(getRepeatX(), getRepeatY());
        result.setScale(getScaleX(), getScaleY());
        result.setAlpha(getAlpha());
        result.setColor(getColor().cloneColor());
        result.setVisible(isVisible());
        result.setRotation(getRotation());
        return result;
    }

    @Override
    public void setTextureBleedingFix(double v) {
        this.textureBleedingFix = v;
    }

    @Override
    public double getTextureBleedingFix() {
        return textureBleedingFix;
    }

    @Override
    public void setVertexBleedingFix(double v) {
        vertexBleedingFix = v;
    }

    @Override
    public double getVertexBleedingFix() {
        return vertexBleedingFix;
    }
}
