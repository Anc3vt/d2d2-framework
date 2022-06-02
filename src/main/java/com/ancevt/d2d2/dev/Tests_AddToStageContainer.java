/**
 * Copyright (C) 2022 the original author or authors.
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

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;

public class Tests_AddToStageContainer {
    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        stage.setBackgroundColor(Color.DARK_GRAY);

        Container container = new Container();

        Sprite sprite = new Sprite("satellite") {{
            addEventListener(Event.ADD_TO_STAGE, e -> System.out.println("ADD " + e));
            addEventListener(Event.REMOVE_FROM_STAGE, e -> System.out.println("REMOVE " + e));
        }};

        Container almostRootContainer = new Container();

        almostRootContainer.add(container);
        container.add(sprite);
        stage.add(almostRootContainer);

        // ----------------------------------

        DebugPanel.setEnabled(true);
        DebugPanel.show("debug-remove-from-stage", "").ifPresent(debugPanel -> {
            debugPanel.addButton("rmamc", almostRootContainer::removeFromParent);
            debugPanel.addButton("rmcnt", container::removeFromParent);
            debugPanel.addButton("rmspt", sprite::removeFromParent);
        });
        DebugPanel.show("debug-add-to-stage", "").ifPresent(debugPanel -> {
            debugPanel.addButton("adamc", () -> stage.add(almostRootContainer));
            debugPanel.addButton("adcnt", () -> almostRootContainer.add(container));
            debugPanel.addButton("adspt", () -> container.add(sprite));
        });

        D2D2.loop();
        DebugPanel.saveAll();
    }
}
