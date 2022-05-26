
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;

public class Tests_BitmapFonts {

    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, Tests_BitmapFonts.class.getName()));
        Root root = D2D2.getStage().getRoot();

        BitmapFont font2 = BitmapFont.loadBitmapFont("PressStart2P.bmf");
        BitmapText bitmapText2 = new BitmapText(font2);
        bitmapText2.setText("PRESSSTART.bmf`` алалала");

        BitmapFont.setDefaultBitmapFont(font2);

        root.add(bitmapText2, 0, 100);

        //bitmapText2.setScale(2,2);

        root.add(new FpsMeter());

        D2D2.loop();
    }
}
