/*
 *     Copyright 2015-2022 Ancevt (me@ancevt.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.display;

import com.ancevt.d2d2.D2D2;
import org.jetbrains.annotations.NotNull;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;

public class Stage extends DisplayObjectContainer {

    private static final String USE_SET_ROOT_INSTEAD_MESSAGE = "use setRoot instead";

    private int width;
    private int height;

    private Root root;

    public Stage() {
    }

    public void setSize(float width, float height) {
        D2D2.getBackend().setWindowSize((int) width, (int) height);
    }

    public void onResize(int width, int height) {
        this.width = width;
        this.height = height;

        if (root == null) return;

        dispatchEvent(EventPool.simpleEventSingleton(Event.RESIZE, this));
    }

    @Override
    public String toString() {
        return "Stage{" +
                "width=" + width +
                ", height=" + height +
                ", root=" + root +
                '}';
    }

    @Override
    public void add(@NotNull IDisplayObject child) {
        throw new UnsupportedOperationException(USE_SET_ROOT_INSTEAD_MESSAGE);
    }

    @Override
    public void add(@NotNull IDisplayObject child, float x, float y) {
        throw new UnsupportedOperationException(USE_SET_ROOT_INSTEAD_MESSAGE);
    }

    @Override
    public void add(@NotNull IDisplayObject child, int indexAt) {
        throw new UnsupportedOperationException(USE_SET_ROOT_INSTEAD_MESSAGE);
    }

    @Override
    public void add(@NotNull IDisplayObject child, int indexAt, float x, float y) {
        throw new UnsupportedOperationException(USE_SET_ROOT_INSTEAD_MESSAGE);
    }

    @Override
    public void remove(@NotNull IDisplayObject child) {
        throw new UnsupportedOperationException(USE_SET_ROOT_INSTEAD_MESSAGE);
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
    public Root getRoot() {
        return root;
    }

    public void setRoot(Root root) {
        if (this.root == root) return;

        Root oldRoot = this.root;

        if (oldRoot != null) {
            super.remove(oldRoot);
            dispatchRemoveFromStage(oldRoot);
        }

        this.root = root;

        super.add(root);
        dispatchAddToStage(root);

        onResize(width, height);
    }

    static void dispatchAddToStage(@NotNull IDisplayObject displayObject) {
        if (displayObject.isOnScreen()) {
            displayObject.dispatchEvent(EventPool.createEvent(Event.ADD_TO_STAGE));
            if (displayObject instanceof IDisplayObjectContainer container) {
                for (int i = 0; i < container.getChildCount(); i++) {
                    dispatchAddToStage(container.getChild(i));
                }
            }
        }
    }

    static void dispatchRemoveFromStage(@NotNull IDisplayObject displayObject) {
        if (displayObject.isOnScreen()) {
            displayObject.dispatchEvent(EventPool.createEvent(Event.REMOVE_FROM_STAGE));

            if (displayObject instanceof IDisplayObjectContainer container) {
                for (int i = 0; i < container.getChildCount(); i++) {
                    dispatchRemoveFromStage(container.getChild(i));
                }
            }
        }
    }
}
