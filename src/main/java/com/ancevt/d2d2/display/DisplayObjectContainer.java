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
package com.ancevt.d2d2.display;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.texture.TextureManager;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;
import com.ancevt.d2d2.exception.ContainerException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DisplayObjectContainer extends DisplayObject implements IDisplayObjectContainer {

    static final int MAX_X = 1048576;
    static final int MAX_Y = 1048576;

    final List<IDisplayObject> children;

    public DisplayObjectContainer() {
        children = new CopyOnWriteArrayList<>();
        setName("_" + getClass().getSimpleName() + displayObjectId());
    }

    @Override
    public void add(@NotNull IDisplayObject child) {
        ((DisplayObject) child).setParent(this);
        child.dispatchEvent(EventPool.createEvent(Event.ADD, this));
        children.remove(child);
        children.add(child);

        Stage.dispatchAddToStage(child);
    }

    @Override
    public void add(@NotNull IDisplayObject child, int index) {
        ((DisplayObject) child).setParent(this);
        child.dispatchEvent(EventPool.createEvent(Event.ADD, this));

        children.remove(child);
        children.add(index, child);

        Stage.dispatchAddToStage(child);
    }

    @Override
    public void add(@NotNull IDisplayObject child, float x, float y) {
        child.setXY(x, y);
        ((DisplayObject) child).setParent(this);
        child.dispatchEvent(EventPool.createEvent(Event.ADD, this));

        children.remove(child);
        children.add(child);

        Stage.dispatchAddToStage(child);
    }

    @Override
    public void add(@NotNull IDisplayObject child, int index, float x, float y) {
        child.setXY(x, y);
        ((DisplayObject) child).setParent(this);
        child.dispatchEvent(EventPool.createEvent(Event.ADD, this));

        children.remove(child);
        children.add(index, child);

        Stage.dispatchAddToStage(child);
    }

    @Override
    public void remove(@NotNull IDisplayObject child) {
        Stage.dispatchRemoveFromStage(child);
        ((DisplayObject) child).setParent(null);
        child.dispatchEvent(EventPool.createEvent(Event.REMOVE, this));
        children.remove(child);
    }

    @Override
    public int indexOf(@NotNull IDisplayObject child) {
        return children.indexOf(child);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public @NotNull IDisplayObject getChild(int index) {
        IDisplayObject child = children.get(index);
        if (child == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return child;
    }

    @Override
    public IDisplayObject getChild(String name) {
        for (IDisplayObject displayObject : children) {
            if (displayObject.getName().equals(name)) return displayObject;
        }
        throw new ContainerException("No such display object with name \"%s\" in container \"%s\"".formatted(name, getName()));
    }

    @Override
    public void removeAllChildren() {
        children.clear();
    }

    @Override
    public boolean contains(@NotNull IDisplayObject child) {
        return children.contains(child);
    }

    @Override
    public TextureManager textureManager() {
        return D2D2.getTextureManager();
    }

    @Override
    public void onEachFrame() {
        // For overriding
    }

    @Override
    public float getWidth() {
        float min = MAX_X;
        float max = 0;

        for (final IDisplayObject child : children) {
            float x = child.getX();
            float xw = x + child.getWidth();

            min = Math.min(x, min);
            max = Math.max(xw, max);
        }

        return max - min;
    }

    @Override
    public float getHeight() {
        float min = MAX_Y;
        float max = 0;

        for (final IDisplayObject child : children) {

            final float y = child.getY();
            final float yh = y + child.getHeight();

            min = Math.min(y, min);
            max = Math.max(yh, max);
        }

        return max - min;
    }

    @Override
    public String toString() {
        return "DisplayObjectContainer{" +
                getName() +
                '}';
    }
}


































