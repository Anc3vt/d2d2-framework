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


package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.scene.Node;
import com.ancevt.d2d2.scene.text.BitmapText;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class DebugLabel extends BitmapText {

    private static final int DEFAULT_UPDATE_RATE = 10;

    private static final Map<Node, DebugLabel> labels = new HashMap<>();

    @Getter
    private final Node target;

    @Getter
    @Setter
    private int updateRate = DEFAULT_UPDATE_RATE;

    private int tick;

    public DebugLabel(Node target, BiConsumer<Node, StringBuilder> func, int updateRate) {
        this.target = target;
        this.updateRate = updateRate;

        setMulticolor(true);

        target.addEventListener(this, SceneEvent.PreFrame.class, e -> {
            tick++;
            if (tick % updateRate == 0) {
                StringBuilder out = new StringBuilder();
                func.accept(target, out);
                setText(out.toString());

                setPosition(target.getGlobalX(), target.getGlobalY());
                D2D2.root().addChild(this);
            }
        });

        D2D2.root().addChild(this);
        labels.put(target, this);
    }

    public void dispose() {
        target.removeEventListener(this, SceneEvent.PreFrame.class);
        target.removeEventListener(this, SceneEvent.RemoveFromScene.class);
        this.removeFromParent();
        labels.remove(this);
    }

    public static DebugLabel clear(Node target) {
        DebugLabel debugLabel = labels.get(target);
        if (debugLabel != null) {
            debugLabel.dispose();
        }
        return debugLabel;
    }

    public static void clearAll() {
        new HashMap<>(labels).forEach((k, v) -> v.dispose());
    }

    public static DebugLabel createLabel(Node target, BiConsumer<Node, StringBuilder> func, int updateRate) {
        if (labels.containsKey(target)) {
            labels.get(target).dispose();
        }
        DebugLabel result = new DebugLabel(target, func, updateRate);
        target.addEventListener(result, SceneEvent.RemoveFromScene.class, e -> {
            result.dispose();
        });
        return result;
    }

    public static DebugLabel createLabel(Node target, BiConsumer<Node, StringBuilder> func) {
        return createLabel(target, func, DEFAULT_UPDATE_RATE);
    }

}
