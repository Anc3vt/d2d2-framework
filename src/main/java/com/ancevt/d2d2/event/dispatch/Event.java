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

public abstract class Event {
    private EventDispatcher target;

    void setTarget(EventDispatcher target) {
        this.target = target;
    }

    public EventDispatcher target() {
        return target;
    }

    @SuppressWarnings("unchecked")
    public <T> T targetAs(Class<T> type) {
        if (type.isInstance(target)) return (T) target;
        throw new ClassCastException("Expected target of type " + type.getName());
    }
}