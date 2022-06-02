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
package com.ancevt.d2d2.backend.norender;

import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.IFramedDisplayObject;
import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;

public class RendererStub implements IRenderer {

    private final Stage stage;
    private int zOrderCounter;

    public RendererStub(Stage stage) {
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

    private void renderDisplayObject(IDisplayObject displayObject) {
        if (!displayObject.isVisible()) return;

        zOrderCounter++;
        displayObject.setAbsoluteZOrderIndex(zOrderCounter);

        if (displayObject instanceof IContainer) {
            IContainer container = (IContainer) displayObject;
            for (int i = 0; i < container.getChildCount(); i++) {
                renderDisplayObject(container.getChild(i));
            }
        }

        if (displayObject instanceof IFramedDisplayObject) {
            ((IFramedDisplayObject) displayObject).processFrame();
        }

        displayObject.onEachFrame();
        displayObject.dispatchEvent(EventPool.simpleEventSingleton(Event.EACH_FRAME, displayObject));
    }
}
