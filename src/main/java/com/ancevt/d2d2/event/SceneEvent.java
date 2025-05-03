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

package com.ancevt.d2d2.event;

import com.ancevt.d2d2.scene.Container;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public abstract class SceneEvent extends Event {

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class LoopUpdate extends SceneEvent {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class ExitFrame extends SceneEvent {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class EnterFrame extends SceneEvent {
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class Add extends SceneEvent {
        private Container parent;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class Remove extends SceneEvent {
        private Container parent;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class AddToScene extends SceneEvent {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class RemoveFromScene extends SceneEvent {
    }
}




