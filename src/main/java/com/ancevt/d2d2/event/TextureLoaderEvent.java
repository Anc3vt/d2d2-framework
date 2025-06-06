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
import com.ancevt.d2d2.scene.texture.Texture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class TextureLoaderEvent extends Event {

    @NoArgsConstructor(staticName = "create")
    @Getter
    public static final class Start extends TextureLoaderEvent {
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    public static final class LoadComplete extends TextureLoaderEvent {
        private Texture texture;
        private byte[] bytes;
    }

    @AllArgsConstructor(staticName = "create")
    @Getter
    public static final class Error extends TextureLoaderEvent {
        private Throwable throwable;
    }
}

