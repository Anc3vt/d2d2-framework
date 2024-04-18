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
package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.ISprite;
import com.ancevt.d2d2.event.Event;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class DebugSpriteColorTryOn {

    private DebugSpriteColorTryOn() {}

    public static void forSprite(ISprite sprite) {
        AtomicReference<DebugPanel> r = new AtomicReference<>();
        AtomicReference<DebugPanel> g = new AtomicReference<>();
        AtomicReference<DebugPanel> b = new AtomicReference<>();

        DebugPanel.show("R " + sprite.getName()).ifPresent(p -> {
            r.set(p);
            p.setY(10);
            p.setSize(100, 50);
            p.addEventListener(Event.EXIT_FRAME, event -> {
                p.setText(p.getX() + " " + sprite.getColor().toHexString());
            });
        });

        DebugPanel.show("G " + sprite.getName()).ifPresent(p -> {
            g.set(p);
            p.setY(100);
            p.setSize(100, 50);
            p.addEventListener(Event.EXIT_FRAME, event -> {
                p.setText(p.getX() + " " + sprite.getColor().toHexString());
            });
        });

        DebugPanel.show("B " + sprite.getName()).ifPresent(p -> {
            b.set(p);
            p.setY(190);
            p.setSize(100, 50);
            p.addEventListener(Event.EXIT_FRAME, event -> {
                p.setText(p.getX() + " " + sprite.getColor().toHexString());
            });
        });

        CompletableFuture.runAsync(() -> {
            while (true) {

                if (b.get() != null) {

                    sprite.setColor(new Color(
                        (int) r.get().getX(),
                        (int) g.get().getX(),
                        (int) b.get().getX()
                    ));
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
