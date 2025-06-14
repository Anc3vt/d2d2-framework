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

package com.ancevt.d2d2.scene;

import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.exception.ContainerException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class BasicGroup extends AbstractNode implements Group {

    static final float MAX_X = Float.MAX_VALUE;
    static final float MAX_Y = Float.MAX_VALUE;

    final List<Node> children;

    public BasicGroup() {
        children = new CopyOnWriteArrayList<>();
        setName("_" + getClass().getSimpleName() + getNodeId());
    }

    private void addChildInternal(Node child, int index, Float x, Float y, PlaceBy placeBy) {
        if (child.hasParent() && child.getParent() != this) {
            child.removeFromParent();
        }

        if (x != null && y != null) {
            child.setPosition(x, y);
        }

        if (child instanceof AbstractNode d) d.setParent(this);

        child.dispatchEvent(NodeEvent.Add.create(this));

        children.remove(child);
        if (index != -1) {
            children.add(index, child);
        } else {
            children.add(child);
        }

        if (placeBy != null) {
            placeBy(child, placeBy);
        }

        Stage.dispatchAddToStage(child);
    }


    @Override
    public final void addChild(Node child) {
        addChildInternal(child, -1, null, null, null);
    }

    @Override
    public final void addChild(Node child, int index) {
        addChildInternal(child, index, null, null, null);
    }

    @Override
    public final void addChild(Node child, float x, float y) {
        addChildInternal(child, -1, x, y, null);
    }

    @Override
    public final void addChild(Node child, int index, float x, float y) {
        addChildInternal(child, index, x, y, null);
    }

    @Override
    public final void addChild(Node child, PlaceBy placeBy) {
        addChildInternal(child, -1, null, null, placeBy);
    }


    private void placeBy(Node node, PlaceBy placeBy) {
        float w = node.getWidth();
        float h = node.getHeight();

        switch (placeBy) {
            case TOP_LEFT -> addChild(node, -w, -h);
            case TOP -> addChild(node, -w / 2, -h);
            case TOP_RIGHT -> addChild(node, 0f, -h);
            case LEFT -> addChild(node, -w, -h / 2);
            case CENTER -> addChild(node, -w / 2, -h / 2);
            case RIGHT -> addChild(node, 0f, -h / 2);
            case BOTTOM_LEFT -> addChild(node, -w, 0f);
            case BOTTOM -> addChild(node, -w / 2, 0f);
            case BOTTOM_RIGHT -> addChild(node, 0f, 0f);
        }
    }

    @Override
    public void removeChild(Node child) {
        Stage.dispatchRemoveFromStage(child);
        if (child instanceof AbstractNode d) d.setParent(null);
        child.dispatchEvent(NodeEvent.Remove.create(this));
        children.remove(child);
    }

    @Override
    public Stream<Node> children() {
        return children.stream();
    }

    @Override
    public int indexOf(Node child) {
        return children.indexOf(child);
    }

    @Override
    public int getNumChildren() {
        return children.size();
    }

    @Override
    public Node getChild(int index) {
        if (index < 0 || index >= children.size())
            throw new ContainerException("Child index %d out of bounds (0-%d)".formatted(index, children.size() - 1));

        return children.get(index);
    }

    @Override
    public Node getChild(String name) {
        for (Node node : children) {
            if (node.getName().equals(name)) return node;
        }
        throw new ContainerException("No such display object named \"%s\" in container \"%s\"".formatted(name, getName()));
    }

    @Override
    public void removeAllChildren() {
        for (Node child : children) {
            removeChild(child);
        }
    }

    @Override
    public boolean contains(Node child) {
        return children.contains(child);
    }


    @Override
    public float getWidth() {
        float min = MAX_X;
        float max = 0;

        if(children.isEmpty()) {
            return 0f;
        }

        for (final Node child : children) {
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

        if(children.isEmpty()) {
            return 0f;
        }

        for (final Node child : children) {
            float y = child.getY();
            float yh = y + child.getHeight();

            min = Math.min(y, min);
            max = Math.max(yh, max);
        }

        return max - min;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                getName() +
                '}';
    }

}
