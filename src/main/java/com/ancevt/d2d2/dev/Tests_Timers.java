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

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.time.Timer;

import static com.ancevt.d2d2.time.Timer.setInterval;
import static com.ancevt.d2d2.time.Timer.setTimeout;

public class Tests_Timers {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();


        PlainRect plainRect = new PlainRect(50, 50, Color.RED);


        Timer timer1 = setInterval(timer -> {
            plainRect.setColor(Color.createVisibleRandomColor());
        }, 1000);


        stage.addEventListener(InputEvent.KEY_DOWN, event -> {
            var e = (InputEvent) event;
            if(e.getKeyCode() == KeyCode.SPACE) {
                timer1.stop();
                System.out.println("STOP");
            }
        });


        stage.add(plainRect, 100, 100);

        stage.add(new FpsMeter());

        System.out.println(new FpsMeter());

        D2D2.loop();
    }
}
