
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.DebugGrid;
import com.ancevt.d2d2.display.DisplayObjectContainer;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.Mouse;

public class Tests_Input {


    private static IDisplayObject cursor;

    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, Tests_Input.class.getName() + "(floating)"));

        Root root = D2D2.getStage().getRoot();

        root.addEventListener(InputEvent.KEY_DOWN, Tests_Input::keyDown);
        root.addEventListener(InputEvent.KEY_UP, Tests_Input::keyUp);
        root.addEventListener(InputEvent.MOUSE_DOWN, Tests_Input::mouseDown);
        root.addEventListener(InputEvent.MOUSE_MOVE, Tests_Input::mouseMove);
        root.addEventListener(InputEvent.MOUSE_WHEEL, Tests_Input::mouseWheel);

        DisplayObjectContainer container = new DisplayObjectContainer();
        Sprite sprite = new Sprite("satellite");

        container.add(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);
        root.add(container, Mouse.getX(), Mouse.getY());

        cursor = container;
        //cursor.setAlpha(0.25f);

        DebugGrid debugGrid = new DebugGrid();
        //debugGrid.setScale(2f,2f);
        root.add(debugGrid);

        D2D2.loop();
    }

    private static void mouseDown(Event event) {
        InputEvent e = (InputEvent) event;
        cursor.setXY(e.getX(), e.getY());

        System.out.println("Mouse down " + ((InputEvent) event).getX());
    }

    private static void mouseWheel(Event event) {
        InputEvent e = (InputEvent) event;

        if (e.getDelta() > 0) {
            cursor.toScale(1.1f, 1.1f);
        } else {
            cursor.toScale(0.9f, 0.9f);
        }

        System.out.println("wheel: " + e.getDelta());
    }

    private static void mouseMove(Event event) {
        InputEvent e = (InputEvent) event;

        if (e.isDrag()) {
            cursor.setXY(e.getX(), e.getY());
            System.out.println("move: " + e.getX() + ", " + e.getY() + " " + e.isDrag());
        }
    }

    private static float nextHalf(float v) {
        return (float) (Math.ceil(v * 16) / 16);
    }


    private static void keyDown(Event event) {
        InputEvent e = (InputEvent) event;

        if (e.getKeyCode() == 259) {
            System.out.println("Removed");
            ((Root) e.getSource()).removeAllEventListeners(InputEvent.KEY_DOWN);
        }

        System.out.println(e);
    }

    private static void keyUp(Event event) {
        InputEvent e = (InputEvent) event;

        if (e.getKeyCode() == KeyCode.UP) {
            System.out.println("up");
        }
    }
}
