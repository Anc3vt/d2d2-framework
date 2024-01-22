package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.InteractiveEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.interactive.Interactive;
import com.ancevt.d2d2.interactive.InteractiveSprite;

import java.util.HashMap;
import java.util.Map;

import static com.ancevt.d2d2.D2D2.init;
import static com.ancevt.d2d2.D2D2.loop;

public class DebugMoveUtils {

    private static final Map<IDisplayObject, InteractiveSprite> interactiveContainers = new HashMap<>();

    private static final String ASSET_1X1 = "d2d2-core-1x1.png";

    private DebugMoveUtils() {
    }

    public static void setMovable(IDisplayObject o, boolean b) {
        boolean old = isMovable(o);
        if (old == b) return;

        if (b) {
            InteractiveSprite button = new InteractiveSprite(
                D2D2.getTextureManager().loadTextureAtlas(ASSET_1X1).createTexture()
            );

            button.setScale(o.getWidth(), o.getHeight());
            button.setAlpha(0.1f);
            button
                .putExtra("object", o)
                .putExtra("DebugMoveUtils.oldX", 0)
                .putExtra("DebugMoveUtils.oldY", 0);


            o.addEventListener(DebugMoveUtils.class, Event.REMOVE, event -> {
                DebugMoveUtils.setMovable(o, false);
            });

            o.addEventListener(DebugMoveUtils.class, Event.ENTER_FRAME, event -> {
                button.setScale(o.getWidth(), o.getHeight());
                button.setXY(o.getX(), o.getY());
//                IContainer parent = o.getParent();
//                button.removeFromParent();
//                parent.add(button);
            });

            button.addEventListener(InteractiveEvent.DOWN, DebugMoveUtils::interactiveButton_down);
            button.addEventListener(InteractiveEvent.UP, DebugMoveUtils::interactiveButton_up);
            button.addEventListener(InteractiveEvent.DRAG, DebugMoveUtils::interactiveButton_drag);
            button.addEventListener(InteractiveEvent.FOCUS_IN, DebugMoveUtils::interactiveButton_focusIn);
            button.addEventListener(InteractiveEvent.FOCUS_OUT, DebugMoveUtils::interactiveButton_focusOut);
            button.addEventListener(InteractiveEvent.KEY_DOWN, DebugMoveUtils::interactiveButton_keyDown);
            button.addEventListener(InteractiveEvent.KEY_REPEAT, DebugMoveUtils::interactiveButton_keyDown);

            button.setEnabled(true);

            button.setScale(o.getWidth(), o.getHeight());
            button.setXY(o.getX(), o.getY());
            IContainer parent = o.getParent();
            button.removeFromParent();
            parent.add(button);

            interactiveContainers.put(o, button);
        } else {
            Interactive button = interactiveContainers.get(o);
            if (button != null) {
                button.removeAllEventListeners();
                button.removeFromParent();
            }
            button.setEnabled(false);
            interactiveContainers.remove(o);

            button.removeAllEventListeners();


            o.removeEventListener(DebugMoveUtils.class, Event.REMOVE);
            o.removeEventListener(DebugMoveUtils.class, Event.ENTER_FRAME);
        }
    }

    private static void interactiveButton_keyDown(Event event) {
        InteractiveEvent e = event.casted();
        Interactive interactive = (Interactive) event.getSource();
        IDisplayObject o = interactive.getExtra("object");

        int speed = e.isShift() ? 10 : 1;

        switch (e.getKeyCode()) {
            case KeyCode.LEFT -> o.moveX(-speed);
            case KeyCode.RIGHT -> o.moveX(speed);
            case KeyCode.UP -> o.moveY(-speed);
            case KeyCode.DOWN -> o.moveY(speed);
        }

        System.out.printf("%s %s, %s%n", o, o.getX(), o.getY());

    }

    private static void interactiveButton_focusIn(Event event) {
        InteractiveSprite interactive = (InteractiveSprite) event.getSource();
        interactive.setColor(Color.YELLOW);
    }

    private static void interactiveButton_focusOut(Event event) {
        InteractiveSprite interactive = (InteractiveSprite) event.getSource();
        interactive.setColor(Color.WHITE);
    }

    private static void interactiveButton_up(Event event) {
    }


    private static void interactiveButton_down(Event event) {
        var e = (InteractiveEvent) event;

        Interactive interactive = (Interactive) event.getSource();
        IDisplayObject o = interactive.getExtra("object");

        o
            .putExtra("DebugMoveUtils.oldX", (int) (e.getX() + o.getX()))
            .putExtra("DebugMoveUtils.oldY", (int) (e.getY() + o.getY()));

        IContainer parent = o.getParent();
        interactive.removeFromParent();
        parent.add(interactive);

        interactive.focus();


        o.dispatchEvent(event);
    }

    private static void interactiveButton_drag(Event event) {
        var e = (InteractiveEvent) event;

        Interactive interactive = (Interactive) event.getSource();
        IDisplayObject o = interactive.getExtra("object");

        final int tx = (int) (e.getX() + o.getX());
        final int ty = (int) (e.getY() + o.getY());


        int oldX = o.getExtra("DebugMoveUtils.oldX");
        int oldY = o.getExtra("DebugMoveUtils.oldY");

        o.move(tx - oldX, ty - oldY);

        System.out.printf("%s %s, %s%n", o, o.getX(), o.getY());

        o
            .putExtra("DebugMoveUtils.oldX", tx)
            .putExtra("DebugMoveUtils.oldY", ty);
    }

    public static boolean isMovable(IDisplayObject o) {
        return interactiveContainers.containsKey(o);
    }

    public static void main(String[] args) {
        Stage stage = init(new LWJGLBackend(800, 600, "(floating)"));

        Sprite sprite = new Sprite("satellite");

        stage.add(sprite);

        DebugMoveUtils.setMovable(sprite, true);

        stage.add(new FpsMeter());
        loop();
    }
}
