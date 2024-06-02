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
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.input.KeyCode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class DebugStageKeyAssigner {

    public static Map<Integer, Runnable> actions = new HashMap<>();

    public static void onKeyDown(int keyCode, Runnable action) {

        if (!actions.containsKey(keyCode)) {
            D2D2.stage().addEventListener(InteractiveEvent.KEY_DOWN, event -> {
                InteractiveEvent e = event.casted();

                if (e.getKeyCode() == keyCode) action.run();
            });
            actions.put(keyCode, action);
        }
    }

    public static void onKeyHold(int keyCode, Runnable action) {

        AtomicBoolean hold = new AtomicBoolean();

        if (!actions.containsKey(keyCode)) {
            D2D2.stage().addEventListener(InteractiveEvent.KEY_DOWN, event -> {
                InteractiveEvent e = event.casted();

                if(e.getKeyCode() == keyCode) hold.set(true);
            });
            D2D2.stage().addEventListener(InteractiveEvent.KEY_UP, event -> {
                InteractiveEvent e = event.casted();

                if(e.getKeyCode() == keyCode) hold.set(false);
            });
            D2D2.stage().addEventListener(Event.LOOP_UPDATE, event -> {
                if (hold.get()) action.run();
            });
            actions.put(keyCode, action);
        }
    }


}
