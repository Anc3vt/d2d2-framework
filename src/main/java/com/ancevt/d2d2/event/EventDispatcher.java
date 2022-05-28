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
package com.ancevt.d2d2.event;

import com.ancevt.commons.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher implements IEventDispatcher {

    private final Map<String, List<EventListener>> map;

    /**
     * ref to 'map' above
     * key : type
     */
    private final Map<Object, Pair<String, EventListener>> keysTypeListenerMap;

    public EventDispatcher() {
        map = new HashMap<>();
        keysTypeListenerMap = new HashMap<>();
    }

    @Override
    public void addEventListener(String type, EventListener listener) {
        List<EventListener> listeners = map.getOrDefault(type, createList());
        listeners.add(listener);
        map.put(type, listeners);
    }

    @Override
    public void addEventListener(String type, EventListener listener, boolean reset) {
        if (reset) removeEventListener(type, listener);
        addEventListener(type, listener);
    }

    private void addEventListenerByKey(Object key, String type, EventListener listener) {
        addEventListener(type, listener);
        if(keysTypeListenerMap.containsKey(key)) {
            throw new IllegalStateException("key '%s' is already exists".formatted(key));
        }
        keysTypeListenerMap.put(key, Pair.of(type, listener));
    }

    /**
     * You may remove listeners by given 'key'
     */
    private void addEventListenerByKey(Object key, String type, EventListener listener, boolean reset) {
        addEventListener(type, listener, reset);
        keysTypeListenerMap.put(key, Pair.of(type, listener));
    }

    @Override
    public void addEventListener(@NotNull Object owner, String type, EventListener listener) {
        addEventListenerByKey(owner.hashCode() + type, type, listener);
    }

    @Override
    public void addEventListener(@NotNull Object owner, String type, EventListener listener, boolean reset) {
        addEventListenerByKey(owner.hashCode() + type, type, listener, reset);
    }

    private @NotNull List<EventListener> createList() {
        return new CopyOnWriteArrayList<>();
    }

    @Override
    public void removeEventListener(String type, EventListener listener) {
        List<EventListener> listeners = map.get(type);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    private void removeEventListenerByKey(Object key) {
        Pair<String, EventListener> pair = keysTypeListenerMap.remove(key);
        if (pair != null) {
            removeEventListener(pair.getFirst(), pair.getSecond());
        }
    }

    @Override
    public void removeEventListener(@NotNull Object owner, String type) {
        removeEventListenerByKey(owner.hashCode() + type);
    }

    @Override
    public void dispatchEvent(@NotNull Event event) {
        List<EventListener> listeners = map.get(event.getType());
        event.setSource(this);
        if (listeners != null) {
            listeners.forEach(e -> e.onEvent(event));
        }
    }

    @Override
    public void removeAllEventListeners(String type) {
        map.remove(type);
    }

    @Override
    public void removeAllEventListeners() {
        map.clear();
    }
}
