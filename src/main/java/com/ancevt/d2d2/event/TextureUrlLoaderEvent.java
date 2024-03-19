/**
 * Copyright (C) 2024 the original author or authors.
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

import com.ancevt.d2d2.display.texture.TextureAtlas;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TextureUrlLoaderEvent extends Event {

    public static final String TEXTURE_LOAD_COMPLETE = "textureLoadComplete";
    public static final String TEXTURE_LOAD_START = "textureLoadStart";
    public static final String TEXTURE_LOAD_ERROR = "textureLoadError";

    private final byte[] bytes;
    private final TextureAtlas textureAtlas;

}
