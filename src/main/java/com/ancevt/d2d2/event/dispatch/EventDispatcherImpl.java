/**
 * Copyright (C) 2025 the original author or authors.
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

package com.ancevt.d2d2.event.dispatch;

import com.ancevt.d2d2.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcherImpl implements EventDispatcher {

    private final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners = new HashMap<>();
    private final Map<Object, List<ListenerBinding<? extends Event>>> bindings = new HashMap<>();

    @Override
    public <T extends Event> void addEventListener(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public <T extends Event> void addEventListener(Object key, Class<T> eventType, EventListener<T> listener) {
        addEventListener(eventType, listener);
        bindings.computeIfAbsent(key, k -> new ArrayList<>())
                .add(new ListenerBinding<>(eventType, listener));
    }

    @Override
    public <T extends Event> void removeEventListener(Class<T> eventType, EventListener<T> listener) {
        List<EventListener<? extends Event>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }

    @Override
    public void removeAllEventListeners() {
        listeners.clear();
        bindings.clear();
    }

    @Override
    public void removeAllEventListenersByKey(Object key) {
        List<ListenerBinding<? extends Event>> keyBindings = bindings.remove(key);
        if (keyBindings != null) {
            for (ListenerBinding<? extends Event> binding : keyBindings) {
                binding.unregister(this);
            }
        }
    }

    @Override
    public <T extends Event> void dispatchEvent(T event) {
        if (event.getTarget() == null) {
            event.setTarget(this);
        }

        List<EventListener<? extends Event>> exactListeners = listeners.get(event.getClass());
        if (exactListeners != null) {
            for (EventListener<? extends Event> listener : exactListeners) {
                @SuppressWarnings("unchecked")
                EventListener<T> casted = (EventListener<T>) listener;
                casted.onEvent(event);
            }
        }

        for (Map.Entry<Class<? extends Event>, List<EventListener<? extends Event>>> entry : listeners.entrySet()) {
            Class<? extends Event> type = entry.getKey();
            if (type.isInstance(event) && !type.equals(event.getClass())) {
                for (EventListener<? extends Event> listener : entry.getValue()) {
                    @SuppressWarnings("unchecked")
                    EventListener<T> casted = (EventListener<T>) listener;
                    casted.onEvent(event);
                }
            }
        }
    }

    private static class ListenerBinding<T extends Event> {
        final Class<T> eventType;
        final EventListener<T> listener;

        ListenerBinding(Class<T> eventType, EventListener<T> listener) {
            this.eventType = eventType;
            this.listener = listener;
        }

        void unregister(EventDispatcher dispatcher) {
            dispatcher.removeEventListener(eventType, listener);
        }
    }
}
