package com.ancevt.d2d2.dev;


import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.input.Mouse;

public class Tests_PictureTest {

    private static float _x;
    private static float _y;

    public static void main(String[] args) {
        Root root = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));

        Sprite sprite = new Sprite(
                D2D2.getTextureManager()
                        .loadTextureAtlas("d2d2-picture-test.png")
                        .createTexture()
        );

        int repeat = 300;

        sprite.setRepeat(repeat, repeat);

        sprite.addEventListener(Event.EACH_FRAME, event -> {
            float x = Mouse.getX();
            float y = Mouse.getY();
            float cx = D2D2.getStage().getWidth() / 2;
            float cy = D2D2.getStage().getHeight() / 2;

            float mx = (cx - x) / 100f;
            float my = (cy - y) / 100f;

            _x += mx;
            _y += my;

            sprite.setXY(_x, _y);

            if (sprite.getX() < -sprite.getWidth() * repeat) sprite.setX(0);
            if (sprite.getY() < -sprite.getHeight() * repeat) sprite.setY(0);
        });

        root.add(sprite);
        root.add(new FpsMeter(), 10, 10);

        root.add(new Sprite("satellite"), 100, 100);
        D2D2.loop();
    }
}
