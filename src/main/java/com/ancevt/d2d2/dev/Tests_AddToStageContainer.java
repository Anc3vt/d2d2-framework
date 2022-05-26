
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.display.DisplayObjectContainer;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.event.Event;

public class Tests_AddToStageContainer {
    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        Root root = D2D2.getStage().getRoot();

        DisplayObjectContainer container = new DisplayObjectContainer();

        Sprite sprite = new Sprite("satellite") {{
            addEventListener(Event.ADD_TO_STAGE, e -> System.out.println("ADD " + e));
            addEventListener(Event.REMOVE_FROM_STAGE, e -> System.out.println("REMOVE " + e));
        }};

        // sprite.dispatchEvent(EventPool.simpleEventSingleton(Event.ADD_TO_STAGE, sprite));

        DisplayObjectContainer almostRootContainer = new DisplayObjectContainer();

        almostRootContainer.add(container);
        container.add(sprite);
        root.add(almostRootContainer);

        // ----------------------------------

        DebugPanel.setEnabled(true);
        DebugPanel.show("debug-remove-from-stage", "").ifPresent(debugPanel -> {
            debugPanel.addButton("rmamc", almostRootContainer::removeFromParent);
            debugPanel.addButton("rmcnt", container::removeFromParent);
            debugPanel.addButton("rmspt", sprite::removeFromParent);
        });
        DebugPanel.show("debug-add-to-stage", "").ifPresent(debugPanel -> {
            debugPanel.addButton("adamc", () -> root.add(almostRootContainer));
            debugPanel.addButton("adcnt", () -> almostRootContainer.add(container));
            debugPanel.addButton("adspt", () -> container.add(sprite));
        });

        D2D2.loop();
        DebugPanel.saveAll();
    }
}
