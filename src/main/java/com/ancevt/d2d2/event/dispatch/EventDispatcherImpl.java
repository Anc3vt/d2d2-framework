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

import java.util.*;

public class EventDispatcherImpl<S> implements EventDispatcher<S> {

    private final Map<Class<? extends Event<S>>, List<EventListener<? extends Event<S>>>> listeners = new HashMap<>();
    private final Map<Object, List<ListenerBinding<S, ? extends Event<S>>>> bindings = new HashMap<>();

    @Override
    public <T extends Event<S>> void addEventListener(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    @Override
    public <T extends Event<S>> void addEventListener(Object key, Class<T> eventType, EventListener<T> listener) {
        addEventListener(eventType, listener);
        bindings.computeIfAbsent(key, k -> new ArrayList<>())
                .add(new ListenerBinding<>(eventType, listener));
    }

    @Override
    public <T extends Event<S>> void removeEventListener(Class<T> eventType, EventListener<T> listener) {
        List<EventListener<? extends Event<S>>> eventListeners = listeners.get(eventType);
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
        List<ListenerBinding<S, ? extends Event<S>>> keyBindings = bindings.remove(key);
        if (keyBindings != null) {
            for (ListenerBinding<S, ? extends Event<S>> binding : keyBindings) {
                binding.unregister(this);
            }
        }
    }

    @Override
    public <T extends Event<S>> void dispatchEvent(T event) {
        if (event.getTarget() == null) {
            event.setTarget((S) this);
        }

        // Сначала обработать точные совпадения по классу
        List<EventListener<? extends Event<S>>> exactListeners = listeners.get(event.getClass());
        if (exactListeners != null) {
            for (EventListener<? extends Event<S>> listener : exactListeners) {
                @SuppressWarnings("unchecked")
                EventListener<T> casted = (EventListener<T>) listener;
                casted.onEvent(event);
            }
        }

        // Потом обработать по совместимости типов (родительские интерфейсы)
        for (Map.Entry<Class<? extends Event<S>>, List<EventListener<? extends Event<S>>>> entry : listeners.entrySet()) {
            Class<? extends Event<S>> type = entry.getKey();
            if (type.isInstance(event) && !type.equals(event.getClass())) {
                for (EventListener<? extends Event<S>> listener : entry.getValue()) {
                    @SuppressWarnings("unchecked")
                    EventListener<T> casted = (EventListener<T>) listener;
                    casted.onEvent(event);
                }
            }
        }
    }

    private static class ListenerBinding<S, T extends Event<S>> {
        final Class<T> eventType;
        final EventListener<T> listener;

        ListenerBinding(Class<T> eventType, EventListener<T> listener) {
            this.eventType = eventType;
            this.listener = listener;
        }

        void unregister(EventDispatcher<S> dispatcher) {
            dispatcher.removeEventListener(eventType, listener);
        }
    }
}
