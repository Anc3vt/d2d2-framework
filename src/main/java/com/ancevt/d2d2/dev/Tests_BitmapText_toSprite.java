
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;

public class Tests_BitmapText_toSprite {
    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, "D2D2Demo2"));
        Root root = new Root();

        BitmapText bitmapText = new BitmapText();
        bitmapText.setText("JAVA: How to create png image from BufferedImage. Image ...");

        Sprite sprite = bitmapText.toSprite();

        sprite.setScale(2,2);
        root.add(sprite, 10, 20);

        D2D2.getTextureManager().bitmapTextToTextureAtlas(bitmapText);

        root.add(new FpsMeter());
        D2D2.getStage().setRoot(root);
        D2D2.loop();
    }
}
