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

import com.ancevt.d2d2.event.core.Event;
import com.ancevt.d2d2.event.core.EventPool;
import com.ancevt.d2d2.event.core.EventPooled;

public abstract class StageEvent extends Event {

    @EventPooled
    public static final class PostFrame extends NodeEvent {
        public static StageEvent.PostFrame create() {
            return EventPool.obtain(StageEvent.PostFrame.class);
        }
    }

    @EventPooled
    public static final class PreFrame extends NodeEvent {
        public static StageEvent.PreFrame create() {
            return EventPool.obtain(StageEvent.PreFrame.class);
        }
    }

    @EventPooled
    public static final class Tick extends NodeEvent {
        public static StageEvent.Tick create() {
            return EventPool.obtain(StageEvent.Tick.class);
        }
    }
}
