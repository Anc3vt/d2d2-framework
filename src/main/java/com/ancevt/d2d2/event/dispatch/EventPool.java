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

package com.ancevt.d2d2.event.dispatch;

import java.util.HashMap;
import java.util.Map;

public class EventPool {

    private static final Map<Class<? extends Event>, Event> pool = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Event> T obtain(Class<T> eventType) {
        if (!eventType.isAnnotationPresent(EventPooled.class)) {
            try {
                return eventType.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Can't create non-pooled event: " + eventType, e);
            }
        }

        Event pooledEvent = pool.get(eventType);
        if (pooledEvent == null) {
            try {
                pooledEvent = eventType.getDeclaredConstructor().newInstance();
                pool.put(eventType, pooledEvent);
            } catch (Exception e) {
                throw new RuntimeException("Can't create pooled event: " + eventType, e);
            }
        }

        return (T) pooledEvent;
    }

    public static void clear() {
        pool.clear();
    }
}
