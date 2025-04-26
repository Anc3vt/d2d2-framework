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
package com.ancevt.d2d2.scene;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public interface Container extends SceneEntity {

    static <T extends SceneEntity> Optional<T> findDisplayObjectById(Container fromRoot, int id) {
        return (Optional<T>) listDisplayObjects(fromRoot, new ArrayList<>())
            .stream()
            .filter(o -> o.getDisplayObjectId() == id)
            .findAny();
    }

    static <T extends SceneEntity> Optional<T> findDisplayObjectByName(Container fromRoot, String name) {
        return (Optional<T>) listDisplayObjects(fromRoot, new ArrayList<>())
            .stream()
            .filter(o -> Objects.equals(o.getName(), name))
            .findFirst();
    }

    static List<SceneEntity> listDisplayObjects(SceneEntity o, List<SceneEntity> list) {
        list.add(o);

        if (o instanceof Container c) {
            c.children().forEach(child -> listDisplayObjects(child, list));
        }

        return list;
    }

    default <T extends SceneEntity> Optional<T> findDisplayObjectById(int id) {
        return findDisplayObjectById(this, id);
    }

    default <T extends SceneEntity> Optional<T> findDisplayObjectByName(String name) {
        return findDisplayObjectByName(this, name);
    }

    void addChild(SceneEntity child);

    void addChild(SceneEntity child, int index);

    void addChild(SceneEntity child, float x, float y);

    void addChild(SceneEntity child, int index, float x, float y);

    void addChild(SceneEntity child, PlaceBy placeBy);

    default void addChildren(Collection<SceneEntity> children) {
        children.forEach(this::addChild);
    }

    default void removeChildren(Collection<SceneEntity> children) {
        children.forEach(this::removeChild);
    }

    Stream<SceneEntity> children();

    void removeChild(SceneEntity child);

    int indexOf(SceneEntity child);

    int getNumChildren();

    SceneEntity getChild(int index);

    SceneEntity getChild(String name);

    boolean contains(SceneEntity child);

    void removeAllChildren();
}
