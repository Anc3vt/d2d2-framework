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


import java.util.*;
import java.util.stream.Stream;

public interface Group extends Node {

    static Group create() {
        return new GroupImpl();
    }

    @SuppressWarnings("unchecked")
    static <T extends Node> Optional<T> findDisplayObjectById(Group fromRoot, int id) {
        return (Optional<T>) listDisplayObjects(fromRoot, new ArrayList<>())
                .stream()
                .filter(o -> o.getDisplayObjectId() == id)
                .findAny();
    }

    @SuppressWarnings("unchecked")
    static <T extends Node> Optional<T> findDisplayObjectByName(Group fromRoot, String name) {
        return (Optional<T>) listDisplayObjects(fromRoot, new ArrayList<>())
                .stream()
                .filter(o -> Objects.equals(o.getName(), name))
                .findFirst();
    }

    static List<Node> listDisplayObjects(Node o, List<Node> list) {
        list.add(o);

        if (o instanceof Group c) {
            c.children().forEach(child -> listDisplayObjects(child, list));
        }

        return list;
    }

    default <T extends Node> Optional<T> findDisplayObjectById(int id) {
        return findDisplayObjectById(this, id);
    }

    default <T extends Node> Optional<T> findDisplayObjectByName(String name) {
        return findDisplayObjectByName(this, name);
    }

    void addChild(Node child);

    void addChild(Node child, int index);

    void addChild(Node child, float x, float y);

    void addChild(Node child, int index, float x, float y);

    void addChild(Node child, PlaceBy placeBy);

    default void addChildren(Collection<Node> children) {
        children.forEach(this::addChild);
    }

    default void removeChildren(Collection<Node> children) {
        children.forEach(this::removeChild);
    }

    Stream<Node> children();

    void removeChild(Node child);

    int indexOf(Node child);

    int getNumChildren();

    Node getChild(int index);

    Node getChild(String name);

    boolean contains(Node child);

    void removeAllChildren();


}
