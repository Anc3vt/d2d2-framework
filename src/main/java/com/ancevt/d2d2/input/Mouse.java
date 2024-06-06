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
package com.ancevt.d2d2.input;

import com.ancevt.d2d2.D2D2;

public class Mouse {

    private static int x;
    private static int y;

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static void setXY(int x, int y) {
        Mouse.x = x;
        Mouse.y = y;
    }

    public static void setVisible(boolean visible) {
        D2D2.getEngine().getDisplayManager().setMouseVisible(visible);
    }

    public static boolean isVisible() {
        return D2D2.getEngine().getDisplayManager().isMouseVisible();
    }


}
