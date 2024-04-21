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
package com.ancevt.d2d2.effect;

import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.IColored;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.text.BitmapText;

public class Glow8Shadows extends Container {

    private final IDisplayObject[] elements;

    public Glow8Shadows(IColored source, Color color, float distance, float alpha, float offsetX, float offsetY) {
        float[][] coords = {
            {0.0f, -1.0f},
            {1.0f, -1.0f},
            {1.0f, 0.0f},
            {1.0f, 1.0f},
            {0.0f, 1.0f},
            {-1.0f, 1.0f},
            {-1.0f, 0.0f},
            {-1.0f, -1.0f}
        };


        Sprite sprite = null;


        elements = new IColored[8];
        for (int i = 0; i < coords.length; i++) {
            float[] currentCoords = coords[i];


            if (sprite == null) {
                if (source instanceof BitmapText bitmapText) {
                    bitmapText = bitmapText.cloneBitmapText();
                    bitmapText.setCacheAsSprite(true);
                    sprite = bitmapText.cachedSprite();
                } else if (source instanceof Sprite s) {
                    sprite = s.cloneSprite();
                } else {
                    throw new IllegalArgumentException("Could not glow8 display object type: " + source.getClass().getName());
                }
            } else {
                sprite = sprite.cloneSprite();
            }

            sprite.setXY(currentCoords[0] * distance, currentCoords[1] * distance);
            sprite.move(offsetX, offsetY);
            sprite.setColor(color);
            sprite.setAlpha(alpha);
            elements[i] = sprite;

            add(sprite);
        }

    }

    public static Glow8Shadows createDefault(IColored o) {
        return new Glow8Shadows(o, Color.BLACK, 0.80f, 1f, 0, 0);
    }

}
