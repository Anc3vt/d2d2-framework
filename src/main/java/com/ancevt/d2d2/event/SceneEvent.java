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
import com.ancevt.d2d2.scene.Group;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class SceneEvent extends Event {

    @EventPooled
    public static final class Tick extends SceneEvent {
        public static Tick create() {
            return EventPool.obtain(Tick.class);
        }
    }

    @EventPooled
    public static final class PostFrame extends SceneEvent {
        public static PostFrame create() {
            return EventPool.obtain(PostFrame.class);
        }
    }

    @EventPooled
    public static final class PreFrame extends SceneEvent {
        public static PreFrame create() {
            return EventPool.obtain(PreFrame.class);
        }
    }

    @EventPooled
    @Getter
    public static final class Add extends SceneEvent {
        private Group parent;

        public static Add create(Group parent) {
            Add e = EventPool.obtain(Add.class);
            e.parent = parent;
            return e;
        }
    }

    @EventPooled
    @Getter
    public static final class Remove extends SceneEvent {
        private Group parent;

        public static Remove create(Group parent) {
            Remove e = EventPool.obtain(Remove.class);
            e.parent = parent;
            return e;
        }
    }

    @EventPooled
    public static final class AddToScene extends SceneEvent {
        public static AddToScene create() {
            return EventPool.obtain(AddToScene.class);
        }
    }

    @EventPooled
    public static final class RemoveFromScene extends SceneEvent {
        public static RemoveFromScene create() {
            return EventPool.obtain(RemoveFromScene.class);
        }
    }

}
