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
package com.ancevt.d2d2.display.interactive;

import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.event.InteractiveEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DragUtil {

    public static void enableDrag(IDisplayObject displayObject, Interactive interactive) {
        AtomicReference<Float> oldXHolder = new AtomicReference<>(0f);
        AtomicReference<Float> oldYHolder = new AtomicReference<>(0f);

        interactive.addEventListener(DragUtil.class + displayObject.getName(), InteractiveEvent.DOWN, event -> {
            var e = (InteractiveEvent) event;
            oldXHolder.set(e.getX() + displayObject.getX());
            oldYHolder.set(e.getY() + displayObject.getY());
            IContainer parent = displayObject.getParent();
            parent.remove(displayObject);
            parent.add(displayObject);
            interactive.focus();
        });

        interactive.addEventListener(DragUtil.class + displayObject.getName(), InteractiveEvent.DRAG, event -> {
            var e = (InteractiveEvent) event;
            final float tx = e.getX() + displayObject.getX();
            final float ty = e.getY() + displayObject.getY();

            displayObject.move(tx - oldXHolder.get(), ty - oldYHolder.get());

            oldXHolder.set(tx);
            oldYHolder.set(ty);
        });
    }

    public static void enableDrag(Interactive interactive) {
        enableDrag(interactive, interactive);
    }

    public static void disableDrag(IDisplayObject displayObject, Interactive interactive) {
        interactive.removeEventListener(DragUtil.class + displayObject.getName(), InteractiveEvent.DOWN);
        interactive.removeEventListener(DragUtil.class + displayObject.getName(), InteractiveEvent.DRAG);
    }

    public static void disableDrag(Interactive interactive) {
        disableDrag(interactive, interactive);
    }


}
