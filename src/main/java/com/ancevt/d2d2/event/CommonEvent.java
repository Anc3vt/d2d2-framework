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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public abstract class CommonEvent<S> extends Event<S> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class Start<S> extends CommonEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class Change<S> extends CommonEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class Complete<S> extends CommonEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class Action<S> extends CommonEvent<S> {
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class Resize<S> extends CommonEvent<S> {
        private int width;
        private int height;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class Error<S> extends CommonEvent<S> {
        private Throwable throwable;
    }
}
