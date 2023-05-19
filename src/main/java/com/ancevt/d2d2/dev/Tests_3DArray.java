/**
 * Copyright (C) 2023 the original author or authors.
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
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tests_3DArray {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));


        int[][][] array = new int[10][10][10];
        fillArrayRandomData(array);

        List<IDisplayObject> displayObjectList = new ArrayList<>();

        for (int z = 0; z < array.length; z++) {
            float deep = array[z].length;
            float factorZ = (deep - (z + 1)) / deep;

            for (int y = 0; y < array[z].length; y++) {
                for (int x = 0; x < array[z][y].length; x++) {
                    int n = array[z][y][x];
                    Digit digit = new Digit(n, x, y, z);

                    float p = 25 * factorZ;

                    float fz = z + 1;

                    digit.setX(x * p + (fz * p));
                    digit.setY(y * p - (fz * p));

                    digit.setAlpha((5 - fz) / 5);

                    if (x == 0 && y == 0) {
                        System.out.println(factorZ);
                    }

                    displayObjectList.add(digit);
                }
            }
        }

        Collections.reverse(displayObjectList);

        IContainer container = new Container();
        displayObjectList.forEach(container::add);

        stage.add(container, 200, 200);

        D2D2.loop();
    }

    private static void fillArrayRandomData(int[][][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    array[i][j][k] = new Random().nextInt(99) + 1;
                }
            }
        }
    }

    private static class Digit extends BitmapText {

        private final int x;
        private final int y;
        private final int z;

        public Digit(int n, int x, int y, int z) {
            super("" + n);
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void onEachFrame() {
//            int mouseX = Mouse.getX();
//            int mouseY = Mouse.getY();
//
//            float zFactor = (z + 1);
//
//            float padding = 25f / zFactor;
//
//            float sw = D2D2.stage().getWidth();
//            float sh = D2D2.stage().getHeight();
//
//            float mxf = (float) mouseX / (z + 1);
//            float myf = (float) mouseY / (z + 1);
//
//            setXY(
//                    (y * padding) - mxf + sw / 2,
//                    (x * padding) - myf + sh / 2
//            );
        }
    }
}
