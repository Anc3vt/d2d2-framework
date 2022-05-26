
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.DisplayObjectContainer;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;

public class Tests {

    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, "Hello D2D2 (floating)"));

        Root root = new Root();
        D2D2.getStage().setRoot(root);

        TextureAtlas textureAtlas = D2D2.getTextureManager().loadTextureAtlas("d2d2-logo.png");
        Texture texture = textureAtlas.createTexture();

        for (int i = 0; i < 1; i++) {
            DisplayObjectContainer container = new DisplayObjectContainer();

            Sprite sprite = new Sprite(texture);
            sprite.setColor(Color.createRandomColor());
            container.addEventListener(Event.EACH_FRAME, e -> {
                container.move(0.01f, 0.01f);
                container.rotate(1);
                //container.toScale(1.005f, 1.005f);
            });
            sprite.setRepeat(1, 1);
            container.add(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);

            root.add(container, (float) (Math.random() * 300) + 100, (float) (Math.random() * 300) + 100);
        }

        BitmapText bitmapText = new BitmapText();
        bitmapText.setName("_test_");
        bitmapText.setColor(Color.WHITE);
        bitmapText.setText("Hello BitmapText\nSecond line");
        bitmapText.addEventListener(Event.EACH_FRAME, e -> {
            //bitmapText.toScale(1.001f, 1.001f);
            //bitmapText.rotate(1f);
            bitmapText.moveX(0.01f);
        });

        bitmapText.setScale(5, 5);

        root.add(bitmapText, 10, 200);


        root.add(new FpsMeter(), 10, 10);

        D2D2.loop();
    }
}
