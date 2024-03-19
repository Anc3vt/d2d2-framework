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
package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.event.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DebugLabel extends BitmapText {

    private static final int DEFAULT_UPDATE_RATE = 10;

    private static final Map<IDisplayObject, DebugLabel> labels = new HashMap<>();

    @Getter
    private final IDisplayObject target;

    @Getter
    @Setter
    private int updateRate = DEFAULT_UPDATE_RATE;

    private int tick;

    public DebugLabel(IDisplayObject target, BiConsumer<IDisplayObject, StringBuilder> func, int updateRate) {
        this.target = target;
        this.updateRate = updateRate;

        setMulticolorEnabled(true);

        target.addEventListener(this, Event.ENTER_FRAME, event -> {
            tick++;
            if (tick % updateRate == 0) {
                StringBuilder out = new StringBuilder();
                func.accept(target, out);
                setText(out.toString());

                setXY(target.getAbsoluteX(), target.getAbsoluteY());
                D2D2.stage().add(this);
            }
        });

        D2D2.stage().add(this);
        labels.put(target, this);
    }

    public void dispose() {
        target.removeEventListener(this, Event.ENTER_FRAME);
        target.removeEventListener(this, Event.REMOVE_FROM_STAGE);
        this.removeFromParent();
        labels.remove(this);
    }

    public static DebugLabel clear(IDisplayObject target) {
        DebugLabel debugLabel = labels.get(target);
        if (debugLabel != null) {
            debugLabel.dispose();
        }
        return debugLabel;
    }

    public static void clearAll() {
        new HashMap<>(labels).forEach((k, v) -> v.dispose());
    }

    public static DebugLabel createLabel(IDisplayObject target, BiConsumer<IDisplayObject, StringBuilder> func, int updateRate) {
        if (labels.containsKey(target)) {
            labels.get(target).dispose();
        }
        DebugLabel result = new DebugLabel(target, func, updateRate);
        target.addEventListener(result, Event.REMOVE_FROM_STAGE, event -> {
            result.dispose();
        });
        return result;
    }

    public static DebugLabel createLabel(IDisplayObject target, BiConsumer<IDisplayObject, StringBuilder> func) {
        return createLabel(target, func, DEFAULT_UPDATE_RATE);
    }

}
