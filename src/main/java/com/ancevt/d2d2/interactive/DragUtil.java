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
package com.ancevt.d2d2.interactive;

import com.ancevt.commons.Holder;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.event.InteractiveEvent;

public class DragUtil {

    public static void enableDrag(IDisplayObject displayObject, Interactive interactive) {
        Holder<Float> oldXHolder = new Holder<>(0f);
        Holder<Float> oldYHolder = new Holder<>(0f);

        interactive.addEventListener(DragUtil.class + displayObject.getName(), InteractiveEvent.DOWN, event -> {
            var e = (InteractiveEvent) event;
            oldXHolder.setValue(e.getX() + displayObject.getX());
            oldYHolder.setValue(e.getY() + displayObject.getY());
            IContainer parent = displayObject.getParent();
            parent.remove(displayObject);
            parent.add(displayObject);
        });

        interactive.addEventListener(DragUtil.class + displayObject.getName(), InteractiveEvent.DRAG, event -> {
            var e = (InteractiveEvent) event;
            final float tx = e.getX() + displayObject.getX();
            final float ty = e.getY() + displayObject.getY();

            displayObject.move(tx - oldXHolder.getValue(), ty - oldYHolder.getValue());

            oldXHolder.setValue(tx);
            oldYHolder.setValue(ty);
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
