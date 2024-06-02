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
package com.ancevt.d2d2.display.shape;

import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.BaseDisplayObject;
import com.ancevt.d2d2.display.Colored;
import com.ancevt.d2d2.display.Resizable;
import com.ancevt.d2d2.event.Event;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RectangleShape extends BaseDisplayObject implements IShape, Resizable, Colored {

    private float width = 100f;
    private float height = 100f;
    private Color color = Color.WHITE;

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
        dispatchEvent(Event.builder().type(Event.RESIZE).build());
    }

    @Override
    public void setWidth(float value) {
        this.width = value;
        dispatchEvent(Event.builder().type(Event.RESIZE).build());
    }

    @Override
    public void setHeight(float value) {
        this.height = value;
        dispatchEvent(Event.builder().type(Event.RESIZE).build());
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
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName() + "{");
        sb.append("width=").append(width);
        sb.append(", height=").append(height);
        sb.append(", color=").append(color);
        sb.append('}');
        return sb.toString();
    }
}
