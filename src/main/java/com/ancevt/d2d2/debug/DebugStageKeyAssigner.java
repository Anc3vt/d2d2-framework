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
