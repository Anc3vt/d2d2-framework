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
package com.ancevt.d2d2.scene.text;

import java.awt.RenderingHints;
import java.util.Map;

public enum FractionalMetrics {

    DEFAULT,
    ON,
    OFF;

    private static final Map<FractionalMetrics, Object> map = Map.of(
        FractionalMetrics.DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT,
        FractionalMetrics.ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON,
        FractionalMetrics.OFF, RenderingHints.VALUE_FRACTIONALMETRICS_OFF
    );

    public static Object nativeValue(FractionalMetrics en) {
        return map.get(en);
    }
}
