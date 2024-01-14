package com.ancevt.d2d2.debug;

import com.ancevt.commons.concurrent.Async;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.ISprite;
import com.ancevt.d2d2.event.Event;

import java.util.concurrent.atomic.AtomicReference;

public class DebugSpriteColorTryOn {

    public static void forSprite(ISprite sprite) {
        AtomicReference<DebugPanel> r = new AtomicReference<>();
        AtomicReference<DebugPanel> g = new AtomicReference<>();
        AtomicReference<DebugPanel> b = new AtomicReference<>();

        DebugPanel.show("R " + sprite.getName()).ifPresent(p -> {
            r.set(p);
            p.setY(10);
            p.setSize(100, 50);
            p.addEventListener(Event.EACH_FRAME, event -> {
                p.setText(p.getX() + " " + sprite.getColor().toHexString());
            });
        });

        DebugPanel.show("G " + sprite.getName()).ifPresent(p -> {
            g.set(p);
            p.setY(100);
            p.setSize(100, 50);
            p.addEventListener(Event.EACH_FRAME, event -> {
                p.setText(p.getX() + " " + sprite.getColor().toHexString());
            });
        });

        DebugPanel.show("B " + sprite.getName()).ifPresent(p -> {
            b.set(p);
            p.setY(190);
            p.setSize(100, 50);
            p.addEventListener(Event.EACH_FRAME, event -> {
                p.setText(p.getX() + " " + sprite.getColor().toHexString());
            });
        });

        Async.run(() -> {
            while (true) {

                if (b.get() != null) {

                    sprite.setColor(new Color(
                        (int) r.get().getX(),
                        (int) g.get().getX(),
                        (int) b.get().getX()
                    ));

                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
