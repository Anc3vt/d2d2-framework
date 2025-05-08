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

import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.scene.*;

public class NoRenderRendererStub implements Renderer {

    private final Root root;
    private int zOrderCounter;

    public NoRenderRendererStub(Root root) {
        this.root = root;
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
        renderDisplayObject(root);
    }

    private void renderDisplayObject(Node node) {
        if (!node.isVisible()) return;

        node.onEnterFrame();
        node.dispatchEvent(NodeEvent.EnterFrame.create());

        node.onLoopUpdate();
        node.dispatchEvent(NodeEvent.LoopUpdate.create());

        zOrderCounter++;
        node.setAbsoluteZOrderIndex(zOrderCounter);

        if (node instanceof Group group) {
            for (int i = 0; i < group.getNumChildren(); i++) {
                renderDisplayObject(group.getChild(i));
            }
        }

        if (node instanceof Playable) {
            ((Playable) node).processFrame();
        }

        node.onExitFrame();
        node.dispatchEvent(NodeEvent.ExitFrame.create());
    }

}
