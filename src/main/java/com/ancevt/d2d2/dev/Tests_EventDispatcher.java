package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventDispatcher;
import com.ancevt.d2d2.event.TouchButtonEvent;

public class Tests_EventDispatcher {


    public static void main(String[] args) {
        Root root = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        root.setBackgroundColor(Color.of(0x112233));
        DebugPanel.setEnabled(true);

        EventDispatcher dispatcher = new EventDispatcher();

        dispatcher.addEventListener(dispatcher, "event1", Tests_EventDispatcher::dispatcher_event1);
        dispatcher.addEventListener(dispatcher, "event2", Tests_EventDispatcher::dispatcher_event2);

        DebugPanel.show("dispatch_event1").ifPresent(debugPanel -> {
            debugPanel.setXY(100, 100);
            debugPanel.addEventListener(TouchButtonEvent.TOUCH_DOWN, event -> {
                var e = (TouchButtonEvent) event;
                if (e.isLeftMouseButton()) {
                    dispatcher.dispatchEvent(Event.builder()
                            .type("event1")
                            .build());
                }
            });
        });

        DebugPanel.show("dispatch_event2").ifPresent(debugPanel -> {
            debugPanel.setXY(100, 200);
            debugPanel.addEventListener(TouchButtonEvent.TOUCH_DOWN, event -> {
                var e = (TouchButtonEvent) event;
                if (e.isLeftMouseButton()) {
                    dispatcher.dispatchEvent(Event.builder()
                            .type("event2")
                            .build());
                }
            });
        });

        DebugPanel.show("remove_event1_listener").ifPresent(debugPanel -> {
            debugPanel.setXY(100, 300);
            debugPanel.addEventListener(TouchButtonEvent.TOUCH_DOWN, event -> {
                var e = (TouchButtonEvent) event;
                if (e.isLeftMouseButton()) {
                    dispatcher.removeEventListener(dispatcher, "event1");
                }
            });
        });

        DebugPanel.show("remove_event2_listener").ifPresent(debugPanel -> {
            debugPanel.setXY(100, 300);
            debugPanel.addEventListener(TouchButtonEvent.TOUCH_DOWN, event -> {
                var e = (TouchButtonEvent) event;
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
