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
package com.ancevt.d2d2.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
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

        log.trace("{} addEventListener: type={}, listener={}", this, type, listener);
    }

    private void addEventListenerByKey(Object key, String type, EventListener listener) {
        if (keysTypeListenerMap.containsKey(key)) {
            removeEventListenerByKey(key);
            //throw new IllegalStateException("key '%s' is already exists".formatted(key));
        }
        addEventListener(type, listener);
        keysTypeListenerMap.put(key, Pair.of(type, listener));
    }

    @Override
    public void addEventListener(Object key, String type, EventListener listener) {
        addEventListenerByKey(key.hashCode() + type, type, listener);
        log.trace("{} addEventListener key: {}, type: {}, listener: {}", this, key, type, listener);
    }

    private List<EventListener> createList() {
        return new CopyOnWriteArrayList<>();
    }

    @Override
    public void removeEventListener(String type, EventListener listener) {
        List<EventListener> listeners = map.get(type);
        if (listeners != null) {
            listeners.remove(listener);
        }
        log.trace("{} removeEventListener: type={}, listener={}", this, type, listener);
    }

    private void removeEventListenerByKey(Object key) {
        Pair<String, EventListener> pair = keysTypeListenerMap.remove(key);
        if (pair != null) {
            removeEventListener(pair.getFirst(), pair.getSecond());
        }
    }

    @Override
    public void removeEventListener(Object key, String type) {
        log.trace("{} removeEventListener: type={}", this, type);
        removeEventListenerByKey(key.hashCode() + type);
    }

    @Override
    public void dispatchEvent(Event event) {
        List<EventListener> listeners = map.get(event.getType());
        event.setSource(this);

        if (listeners != null) {
            listeners.forEach(e -> e.onEvent(event));
        }
    }

    @Override
    public void removeAllEventListeners(String type) {
        map.remove(type);
        log.trace("removeAllEventListeners type: {}", type);
    }

    @Override
    public void removeAllEventListeners() {
        map.clear();
        log.trace("removeAllEventListeners");
    }

    @RequiredArgsConstructor(staticName = "of")
    @Getter
    private static class Pair<T1, T2> {
        public final T1 first;
        public final T2 second;
    }
}
