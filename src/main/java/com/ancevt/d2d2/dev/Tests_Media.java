/**
 * Copyright (C) 2024 the original author or authors.
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

import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.media.Media;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.Stage;

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
