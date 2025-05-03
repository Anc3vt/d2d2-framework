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

public interface EventDispatcher {

    <T extends Event> void addEventListener(Class<T> eventType, EventListener<T> listener);

    <T extends Event> void addEventListener(Object key, Class<T> eventType, EventListener<T> listener);

    <T extends Event> void removeEventListener(Class<T> eventType, EventListener<T> listener);

    <T extends Event> void removeEventListener(Object key, Class<T> eventType);

    void removeAllEventListeners();

    void removeAllEventListenersByKey(Object key);

    <T extends Event> void dispatchEvent(T event);
}
