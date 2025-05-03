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

package com.ancevt.d2d2.event;

import com.ancevt.d2d2.event.dispatch.Event;
import com.ancevt.d2d2.event.dispatch.EventPool;
import com.ancevt.d2d2.event.dispatch.EventPooled;

public abstract class CommonEvent extends Event {

    @EventPooled
    public static final class Start extends CommonEvent {
        public static Start create() {
            return EventPool.obtain(Start.class);
        }
    }

    @EventPooled
    public static final class Stop extends CommonEvent {
        public static Stop create() {
            return EventPool.obtain(Stop.class);
        }
    }

    @EventPooled
    public static final class Change extends CommonEvent {
        public static Change create() {
            return EventPool.obtain(Change.class);
        }
    }

    @EventPooled
    public static final class Update extends CommonEvent {
        public static Update create() {
            return EventPool.obtain(Update.class);
        }
    }

    @EventPooled
    public static final class Complete extends CommonEvent {
        public static Complete create() {
            return EventPool.obtain(Complete.class);
        }
    }

    @EventPooled
    public static final class Action extends CommonEvent {
        public static Action create() {
            return EventPool.obtain(Action.class);
        }
    }

    @EventPooled
    public static final class Resize extends SceneEvent {
        private float width;
        private float height;

        public static Resize create(float width, float height) {
            Resize event = EventPool.obtain(Resize.class);
            event.width = width;
            event.height = height;
            return event;
        }
    }

    @EventPooled
    public static final class Error extends CommonEvent {
        private Throwable throwable;

        public static Error create(Throwable throwable) {
            Error event = EventPool.obtain(Error.class);
            event.throwable = throwable;
            return event;
        }
    }
}
