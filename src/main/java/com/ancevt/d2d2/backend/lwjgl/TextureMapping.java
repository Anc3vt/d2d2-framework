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
package com.ancevt.d2d2.backend.lwjgl;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TextureMapping {
    private final Map<Integer, Integer> ids;
    private final Map<Integer, BufferedImage> images;

    public TextureMapping() {
        ids = new HashMap<>();
        images = new HashMap<>();
    }

    public Map<Integer, Integer> ids() {
        return ids;
    }

    public Map<Integer, BufferedImage> images() {
        return images;
    }
}
