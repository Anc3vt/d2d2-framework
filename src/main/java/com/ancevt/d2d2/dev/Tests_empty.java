
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.Root;

public class Tests_empty {


    public static void main(String[] args) {
        Root root = D2D2.init(new LWJGLBackend(800, 600, "(floating"));

        D2D2.loop();
    }
}
