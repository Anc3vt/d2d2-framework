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
package com.ancevt.d2d2.display;

import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;
import com.ancevt.d2d2.exception.ContainerException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Container extends DisplayObject implements IContainer {

    static final int MAX_X = 1048576;
    static final int MAX_Y = 1048576;

    final List<IDisplayObject> children;

    public Container() {
        children = new CopyOnWriteArrayList<>();
        setName("_" + getClass().getSimpleName() + displayObjectId());
    }

    @Override
    public void add(IDisplayObject child) {
        ((DisplayObject) child).setParent(this);
        child.dispatchEvent(EventPool.createEvent(Event.ADD, this));

        children.remove(child);
        children.add(child);

        Stage.dispatchAddToStage(child);
    }

    @Override
    public void add(IDisplayObject child, int index) {
        ((DisplayObject) child).setParent(this);
        child.dispatchEvent(EventPool.createEvent(Event.ADD, this));

        children.remove(child);
        children.add(index, child);

        Stage.dispatchAddToStage(child);
    }

    @Override
    public void add(IDisplayObject child, float x, float y) {
        child.setXY(x, y);
        ((DisplayObject) child).setParent(this);
        child.dispatchEvent(EventPool.createEvent(Event.ADD, this));

        children.remove(child);
        children.add(child);

        Stage.dispatchAddToStage(child);
    }

    @Override
    public void add(IDisplayObject child, int index, float x, float y) {
        child.setXY(x, y);
        ((DisplayObject) child).setParent(this);
        child.dispatchEvent(EventPool.createEvent(Event.ADD, this));

        children.remove(child);
        children.add(index, child);

        Stage.dispatchAddToStage(child);
    }

    @Override
    public void remove(IDisplayObject child) {
        Stage.dispatchRemoveFromStage(child);
        ((DisplayObject) child).setParent(null);
        child.dispatchEvent(EventPool.createEvent(Event.REMOVE, this));
        children.remove(child);
    }

    @Override
    public int indexOf(IDisplayObject child) {
        return children.indexOf(child);
    }

    @Override
    public int getNumberOfChildren() {
        return children.size();
    }

    @Override
    public IDisplayObject getChild(int index) {
        if (index < 0 || index >= children.size())
            throw new ContainerException("Child index %d out of bounds (0-%d)".formatted(index, children.size() - 1));

        return children.get(index);
    }

    @Override
    public IDisplayObject getChild(String name) {
        for (IDisplayObject displayObject : children) {
            if (displayObject.getName().equals(name)) return displayObject;
        }
        throw new ContainerException("No such display object named \"%s\" in container \"%s\"".formatted(name, getName()));
    }

    @Override
    public void removeAllChildren() {
        children.clear();
    }

    @Override
    public boolean contains(IDisplayObject child) {
        return children.contains(child);
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
            float y = child.getY();
            float yh = y + child.getHeight();

            min = Math.min(y, min);
            max = Math.max(yh, max);
        }

        return max - min;
    }

    @Override
    public String toString() {
        return "Container{" +
            getName() +
            '}';
    }

    public static IContainer createContainer() {
        return new Container();
    }

    public static IContainer createContainer(IDisplayObject childDisplayObject) {
        IContainer container = createContainer();
        container.add(childDisplayObject);
        return container;
    }
}
