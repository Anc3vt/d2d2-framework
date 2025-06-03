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

package com.ancevt.d2d2.scene.shape;

import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.scene.*;
import com.ancevt.d2d2.scene.texture.TextureRegion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class RectangleShape extends AbstractNode implements Shape, Resizable, Colored, Textured {

    private float width = 100f;
    private float height = 100f;

    private Color color = Color.WHITE;

    @Getter
    @Setter
    private float textureURepeat = 1.0f;

    @Getter
    @Setter
    private float textureVRepeat = 1.0f;

    @Getter
    @Setter
    private TextureRegion textureRegion;

    @Getter
    @Setter
    private float textureRotation = 0f; // в радианах

    @Getter
    @Setter
    private float textureScaleX = 1f;

    @Getter
    @Setter
    private float textureScaleY = 1f;

    public RectangleShape(float width, float height) {
        this();
        this.width = width;
        this.height = height;
    }

    public RectangleShape(float width, float height, Color color) {
        this();
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        dispatchEvent(CommonEvent.Resize.create(width, height));
    }

    @Override
    public void setWidth(float value) {
        this.width = value;
        dispatchEvent(CommonEvent.Resize.create(width, height));
    }

    @Override
    public void setHeight(float value) {
        this.height = value;
        dispatchEvent(CommonEvent.Resize.create(width, height));
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setTextureUVRepeat(float uRepeat, float vRepeat) {
        this.textureURepeat = uRepeat;
        this.textureVRepeat = vRepeat;
    }

    @Override
    public void setTextureScale(float scaleX, float scaleY) {
        textureScaleX = scaleX;
        textureScaleY = scaleY;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "{");
        sb.append("width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", color=").append(color);
        sb.append('}');
        return sb.toString();
    }
}
