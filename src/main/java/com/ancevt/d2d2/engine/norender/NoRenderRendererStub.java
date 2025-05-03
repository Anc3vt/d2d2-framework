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

package com.ancevt.d2d2.engine.norender;

import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.scene.*;

public class NoRenderRendererStub implements Renderer {

    private final Scene scene;
    private int zOrderCounter;

    public NoRenderRendererStub(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void init(long windowId) {

    }

    @Override
    public void reshape() {

    }

    @Override
    public void renderFrame() {
        zOrderCounter = 0;
        renderDisplayObject(scene);
    }

    private void renderDisplayObject(SceneEntity sceneEntity) {
        if (!sceneEntity.isVisible()) return;

        sceneEntity.onEnterFrame();
        sceneEntity.dispatchEvent(SceneEvent.EnterFrame.create());

        sceneEntity.onLoopUpdate();
        sceneEntity.dispatchEvent(SceneEvent.LoopUpdate.create());

        zOrderCounter++;
        sceneEntity.setAbsoluteZOrderIndex(zOrderCounter);

        if (sceneEntity instanceof Container container) {
            for (int i = 0; i < container.getNumChildren(); i++) {
                renderDisplayObject(container.getChild(i));
            }
        }

        if (sceneEntity instanceof Playable) {
            ((Playable) sceneEntity).processFrame();
        }

        sceneEntity.onExitFrame();
        sceneEntity.dispatchEvent(SceneEvent.ExitFrame.create());
    }

}
