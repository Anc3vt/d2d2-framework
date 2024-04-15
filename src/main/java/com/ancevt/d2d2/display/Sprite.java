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
package com.ancevt.d2d2.display;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.texture.Texture;

public class Sprite extends DisplayObject implements ISprite {

    public static final Color DEFAULT_COLOR = Color.WHITE;

    private float repeatX;
    private float repeatY;
    private Color color;
    private Texture texture;
    private double vertexBleedingFix = 0.05d;
    private double textureBleedingFix = 0.00005d;

    public Sprite() {
        setColor(DEFAULT_COLOR);
        setRepeat(1, 1);
        setName("_" + getClass().getSimpleName() + displayObjectId());
    }

    public Sprite(String assetPathToImage) {
        this(D2D2.textureManager().loadTextureAtlas(assetPathToImage).createTexture());
    }

    public Sprite(String assetPathToImage, int textureX, int textureY, int textureWidth, int textureHeight) {
        this(
            D2D2.textureManager()
                .loadTextureAtlas(assetPathToImage)
                .createTexture(
                    textureX,
                    textureY,
                    textureWidth,
                    textureHeight
                )
        );
    }

    public Sprite(Texture texture) {
        setTexture(texture);

        setColor(DEFAULT_COLOR);
        setRepeat(1, 1);
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
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void setTexture(Texture value) {
        this.texture = value;
        if (texture.getTextureAtlas().isDisposed()) {
            throw new IllegalStateException("Texture atlas " + texture.getTextureAtlas().getId() + " is disposed");
        }
    }

    @Override
    public void setTexture(String textureKey) {
        setTexture(D2D2.textureManager().getTexture(textureKey));
    }

    @Override
    public float getWidth() {
        return texture == null ? 0f : texture.width();
    }

    @Override
    public float getHeight() {
        return texture == null ? 0f : texture.height();
    }

    @Override
    public void onExitFrame() {
        // For overriding
    }

    @Override
    public Sprite cloneSprite() {
        Sprite result = new Sprite(getTexture());
        result.setXY(getX(), getY());
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
        if (getName().equals("_renderer_test_")) {

            System.out.println(v);

            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
