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

package com.ancevt.d2d2.scene.interactive;

import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.scene.Group;
import com.ancevt.d2d2.scene.Node;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DragUtil {

    public static void enableDrag(Node node, Interactive interactive) {
        AtomicReference<Float> oldXHolder = new AtomicReference<>(0f);
        AtomicReference<Float> oldYHolder = new AtomicReference<>(0f);

        interactive.addEventListener(DragUtil.class + node.getName(), InputEvent.MouseDown.class, e -> {
            oldXHolder.set(e.getX() + node.getX());
            oldYHolder.set(e.getY() + node.getY());
            Group parent = node.getParent();
            parent.removeChild(node);
            parent.addChild(node);
            interactive.focus();
        });

        interactive.addEventListener(DragUtil.class + node.getName(), InputEvent.MouseDrag.class, e -> {
            final float tx = e.getX() + node.getX();
            final float ty = e.getY() + node.getY();

            node.move(tx - oldXHolder.get(), ty - oldYHolder.get());

            oldXHolder.set(tx);
            oldYHolder.set(ty);
        });
    }

    public static void enableDrag(Interactive interactive) {
        enableDrag(interactive, interactive);
    }

    public static void disableDrag(Node node, Interactive interactive) {
        interactive.removeEventListener(DragUtil.class + node.getName(), InputEvent.MouseDown.class);
        interactive.removeEventListener(DragUtil.class + node.getName(), InputEvent.MouseDrag.class);
    }

    public static void disableDrag(Interactive interactive) {
        disableDrag(interactive, interactive);
    }


}
