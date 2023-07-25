package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.media.Media;

public class Tests_Media {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        Media media = Media.lookupSound("/home/ancevt/Software/FLStudio12/Data/Patches/Packs/Packs/Mine3/Skoraya_pomosch.mp3");

        DebugPanel.setEnabled(true);
        DebugPanel.show("test")
            .orElseThrow()
            .addButton("sound1", () -> {
                System.out.println(123);
                media.asyncPlay();
            })
            .addButton("sound2", () -> {
                System.out.println(123);
                media.stop();
            });


        DebugPanel.saveAll();

        D2D2.loop();
    }
}

/*

/home/ancevt/Software/FLStudio12/Data/Patches/Packs/Packs/Mine3/hollywood.mp3
/home/ancevt/Software/FLStudio12/Data/Patches/Packs/Packs/Mine3/kon-vocs.mp3
/home/ancevt/Software/FLStudio12/Data/Patches/Packs/Packs/Mine3/Skoraya_pomosch.mp3
/home/ancevt/Software/FLStudio12/Data/Patches/Packs/Packs/Mine3/tosample1.mp3


 */
