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
package com.ancevt.d2d2.engine.norender;

import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.DisplayObject;
import com.ancevt.d2d2.display.Playable;
import com.ancevt.d2d2.display.Renderer;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;

public class NoRenderRendererStub implements Renderer {

    private final Stage stage;
    private int zOrderCounter;

    public NoRenderRendererStub(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void init(long windowId) {

    }

    @Override
    public void reshape(int width, int height) {

    }

    @Override
    public void renderFrame() {
        zOrderCounter = 0;
        renderDisplayObject(stage);
    }

    private void renderDisplayObject(DisplayObject displayObject) {
        if (!displayObject.isVisible()) return;

        displayObject.onEnterFrame();
        displayObject.dispatchEvent(EventPool.simpleEventSingleton(Event.ENTER_FRAME, displayObject));

        displayObject.onLoopUpdate();
        displayObject.dispatchEvent(EventPool.simpleEventSingleton(Event.LOOP_UPDATE, displayObject));

        zOrderCounter++;
        displayObject.setAbsoluteZOrderIndex(zOrderCounter);

        if (displayObject instanceof Container container) {
            for (int i = 0; i < container.getNumChildren(); i++) {
                renderDisplayObject(container.getChild(i));
            }
        }

        if (displayObject instanceof Playable) {
            ((Playable) displayObject).processFrame();
        }

        displayObject.onExitFrame();
        displayObject.dispatchEvent(EventPool.simpleEventSingleton(Event.EXIT_FRAME, displayObject));
    }

    private void dispatchLoopUpdate(DisplayObject o) {
        if (!o.isVisible()) return;

        if (o instanceof Container c) {
            for (int i = 0; i < c.getNumChildren(); i++) {
                DisplayObject child = c.getChild(i);
                dispatchLoopUpdate(child);
            }
        }

        o.dispatchEvent(EventPool.simpleEventSingleton(Event.LOOP_UPDATE, o));
        o.onLoopUpdate();
    }
}
