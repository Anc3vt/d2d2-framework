/**
 * Copyright (C) 2023 the original author or authors.
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
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.debug.DebugGrid;
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;

public class Tests_Sprite_float_repeat {
    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        DebugGrid debugGrid = new DebugGrid();
        stage.add(debugGrid);
        debugGrid.setScale(1f, 1f);
        debugGrid.setAlpha(0.25f);

        Sprite sprite = new Sprite("test16x16");

        sprite.setScale(4f, 4f);

        DebugPanel.setEnabled(true);
        DebugPanel.show("repeatxf", "").ifPresent(debugPanel -> {

            debugPanel.addEventListener(Event.EACH_FRAME, event -> {
                debugPanel.setText("repeatX: " + sprite.getRepeatX() + "\n" +
                        "repeatY: " + sprite.getRepeatY());
            });

            debugPanel.addButton("<", () -> sprite.setRepeatX(sprite.getRepeatX() - 0.25f));
            debugPanel.addButton(">", () -> sprite.setRepeatX(sprite.getRepeatX() + 0.25f));
            debugPanel.addButton("^", () -> sprite.setRepeatY(sprite.getRepeatY() - 0.25f));
            debugPanel.addButton("v", () -> sprite.setRepeatY(sprite.getRepeatY() + 0.25f));

            debugPanel.addButton("vbf-", () -> sprite.setVertexBleedingFix(sprite.getVertexBleedingFix() - 0.25));
            debugPanel.addButton("vbf+", () -> sprite.setVertexBleedingFix(sprite.getVertexBleedingFix() + 0.25));

            debugPanel.addButton("tbf-", () -> sprite.setTextureBleedingFix(sprite.getTextureBleedingFix() - 0.25));
            debugPanel.addButton("tbf+", () -> sprite.setTextureBleedingFix(sprite.getTextureBleedingFix() + 0.25));
        });

        stage.add(sprite, 128, 256);

        stage.add(new FpsMeter());
        D2D2.loop();
        DebugPanel.saveAll();
    }
}
