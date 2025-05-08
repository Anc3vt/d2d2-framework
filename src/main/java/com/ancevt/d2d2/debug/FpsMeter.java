/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.core.Event;
import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.text.Font;
import com.ancevt.d2d2.scene.text.Text;

public class FpsMeter extends Text {

    private long time = System.currentTimeMillis();


    public FpsMeter(Font font) {
        super(font);
        addEventListener(NodeEvent.AfterRenderFrame.class, this::eachFrame);

    }

    public FpsMeter() {
        super();
        addEventListener(NodeEvent.AfterRenderFrame.class, this::eachFrame);
    }

    public int getFps() {
        return D2D2.engine().getActualFps();
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

