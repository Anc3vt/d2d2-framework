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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public abstract class CommonEvent extends Event {

    @NoArgsConstructor(staticName = "create")
    static public final class Start extends CommonEvent {
    }

    @NoArgsConstructor(staticName = "create")
    static public final class Stop extends CommonEvent {
    }

    @NoArgsConstructor(staticName = "create")
    static public final class Change extends CommonEvent {

    }

    @NoArgsConstructor(staticName = "create")
    static public final class Complete extends CommonEvent {

    }

    @NoArgsConstructor(staticName = "create")
    static public final class Action extends CommonEvent {
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static public final class Resize extends CommonEvent {
        private float width;
        private float height;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static public final class Error extends CommonEvent {
        private Throwable throwable;
    }
}
