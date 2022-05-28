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

import com.ancevt.d2d2.display.IDisplayObjectContainer;

public class EventPool {

    private static final Event SIMPLE_EVENT_SINGLETON = Event.builder().build();

    public static Event createEvent(String type, IDisplayObjectContainer parent) {
        return Event.builder().type(type).parent(parent).build();
    }

    public static Event createEvent(String type) {
        return createEvent(type, null);
    }

    public static Event simpleEventSingleton(String type, IEventDispatcher source) {
        SIMPLE_EVENT_SINGLETON.type = type;
        SIMPLE_EVENT_SINGLETON.source = source;
        return SIMPLE_EVENT_SINGLETON;
    }
}
