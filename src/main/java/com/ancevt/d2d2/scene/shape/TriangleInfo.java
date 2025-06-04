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

package com.ancevt.d2d2.scene.shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public class TriangleInfo {

    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;
    private final float x3;
    private final float y3;

    public boolean contains(Vertex p) {
        return
                (p.x == x1 && p.y == y1) &&
                        (p.x == x2 && p.y == y2) &&
                        (p.x == x3 && p.y == y3);
    }
}
