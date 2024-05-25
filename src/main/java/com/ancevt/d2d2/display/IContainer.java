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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public interface IContainer extends IDisplayObject {

    static <T extends IDisplayObject> Optional<T> findDisplayObjectById(IContainer fromRoot, int id) {
        return (Optional<T>) listDisplayObjects(fromRoot, new ArrayList<>())
            .stream()
            .filter(o -> o.getDisplayObjectId() == id)
            .findAny();
    }

    static <T extends IDisplayObject> Optional<T> findDisplayObjectByName(IContainer fromRoot, String name) {
        return (Optional<T>) listDisplayObjects(fromRoot, new ArrayList<>())
            .stream()
            .filter(o -> Objects.equals(o.getName(), name))
            .findFirst();
    }

    static List<IDisplayObject> listDisplayObjects(IDisplayObject o, List<IDisplayObject> list) {
        list.add(o);

        if (o instanceof IContainer c) {
            c.children().forEach(child -> listDisplayObjects(child, list));
        }

        return list;
    }

    default <T extends IDisplayObject> Optional<T> findDisplayObjectById(int id) {
        return findDisplayObjectById(this, id);
    }

    default <T extends IDisplayObject> Optional<T> findDisplayObjectByName(String name) {
        return findDisplayObjectByName(this, name);
    }

    void add(IDisplayObject child);

    void add(IDisplayObject child, int index);

    void add(IDisplayObject child, float x, float y);

    void add(IDisplayObject child, int index, float x, float y);

    void add(IDisplayObject child, PlaceBy placeBy);

    default void addAll(Collection<IDisplayObject> children) {
        children.forEach(this::add);
    }

    default void removeAll(Collection<IDisplayObject> children) {
        children.forEach(this::remove);
    }

    Stream<IDisplayObject> children();

    void remove(IDisplayObject child);

    int indexOf(IDisplayObject child);

    int getNumChildren();

    IDisplayObject getChild(int index);

    IDisplayObject getChild(String name);

    boolean contains(IDisplayObject child);

    void removeAllChildren();
}
