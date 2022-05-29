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
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventDispatcher;
import com.ancevt.d2d2.event.InteractiveButtonEvent;

public class Tests_EventDispatcher {


    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        stage.setBackgroundColor(Color.of(0x112233));
        DebugPanel.setEnabled(true);

        EventDispatcher dispatcher = new EventDispatcher();

        dispatcher.addEventListener(dispatcher, "event1", Tests_EventDispatcher::dispatcher_event1);
        dispatcher.addEventListener(dispatcher, "event2", Tests_EventDispatcher::dispatcher_event2);

        DebugPanel.show("dispatch_event1").ifPresent(debugPanel -> {
            debugPanel.setXY(100, 100);
            debugPanel.addEventListener(InteractiveButtonEvent.DOWN, event -> {
                var e = (InteractiveButtonEvent) event;
                if (e.isLeftMouseButton()) {
                    dispatcher.dispatchEvent(Event.builder()
                            .type("event1")
                            .build());
                }
            });
        });

        DebugPanel.show("dispatch_event2").ifPresent(debugPanel -> {
            debugPanel.setXY(100, 200);
            debugPanel.addEventListener(InteractiveButtonEvent.DOWN, event -> {
                var e = (InteractiveButtonEvent) event;
                if (e.isLeftMouseButton()) {
                    dispatcher.dispatchEvent(Event.builder()
                            .type("event2")
                            .build());
                }
            });
        });

        DebugPanel.show("remove_event1_listener").ifPresent(debugPanel -> {
            debugPanel.setXY(100, 300);
            debugPanel.addEventListener(InteractiveButtonEvent.DOWN, event -> {
                var e = (InteractiveButtonEvent) event;
                if (e.isLeftMouseButton()) {
                    dispatcher.removeEventListener(dispatcher, "event1");
                }
            });
        });

        DebugPanel.show("remove_event2_listener").ifPresent(debugPanel -> {
            debugPanel.setXY(100, 300);
            debugPanel.addEventListener(InteractiveButtonEvent.DOWN, event -> {
                var e = (InteractiveButtonEvent) event;
                if (e.isLeftMouseButton()) {
                    dispatcher.removeEventListener(dispatcher, "event2");
                }
            });
        });


        D2D2.loop();
        DebugPanel.saveAll();
    }

    private static void dispatcher_event1(Event event) {
        System.out.println("event1");
    }

    private static void dispatcher_event2(Event event) {
        System.out.println("event2");
    }
}
