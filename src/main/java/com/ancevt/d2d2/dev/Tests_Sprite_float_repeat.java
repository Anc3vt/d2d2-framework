package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.DebugGrid;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;

public class Tests_Sprite_float_repeat {
    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();


        DebugGrid debugGrid = new DebugGrid();
        stage.add(debugGrid);

        Sprite sprite = new Sprite("test16x16");

        sprite.setScale(2f, 2f);

        stage.add(sprite, 128, 256);

        stage.add(new FpsMeter());
        D2D2.loop();
    }
}
