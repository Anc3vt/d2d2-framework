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
import com.ancevt.d2d2.debug.DebugGrid;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.Mouse;

public class Tests_Input {


    private static IDisplayObject cursor;
    private static Stage stage;

    public static void main(String[] args) {
        stage = D2D2.init(new LWJGLBackend(800, 600, Tests_Input.class.getName() + "(floating)"));

        stage.addEventListener(InputEvent.KEY_DOWN, Tests_Input::keyDown);
        stage.addEventListener(InputEvent.KEY_UP, Tests_Input::keyUp);
        stage.addEventListener(InputEvent.MOUSE_DOWN, Tests_Input::mouseDown);
        stage.addEventListener(InputEvent.MOUSE_MOVE, Tests_Input::mouseMove);
        stage.addEventListener(InputEvent.MOUSE_WHEEL, Tests_Input::mouseWheel);

        Container container = new Container();
        Sprite sprite = new Sprite("satellite");

        container.add(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);
        stage.add(container, Mouse.getX(), Mouse.getY());

        cursor = container;

        DebugGrid debugGrid = new DebugGrid();
        stage.add(debugGrid);

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

        if (e.getKeyCode() == KeyCode.DELETE) {
            System.out.println("Removed");
            stage.removeAllEventListeners(InputEvent.KEY_DOWN);
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
