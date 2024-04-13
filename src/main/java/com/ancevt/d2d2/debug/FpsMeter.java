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
package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.event.Event;

import java.util.ArrayList;
import java.util.List;

public class FpsMeter extends BitmapText {

    private long time = System.currentTimeMillis();


    public FpsMeter(BitmapFont font) {
        super(font);
        addEventListener(Event.EXIT_FRAME, this::eachFrame);

    }

    public FpsMeter() {
        super();
        addEventListener(Event.EXIT_FRAME, this::eachFrame);
    }

    public int getFps() {
        return D2D2.backend().getActualFps();
    }

    public void eachFrame(Event event) {
        final long time2 = System.currentTimeMillis();
        if (time2 - time >= 1000) {
            time = System.currentTimeMillis();
            int displayFps = getFps();
            setText("" + displayFps);
            if (displayFps >= 40) setColor(Color.of(0x00FF00));
            else if (displayFps >= 30) setColor(Color.YELLOW);
            else setColor(Color.RED);
        }
    }

}

