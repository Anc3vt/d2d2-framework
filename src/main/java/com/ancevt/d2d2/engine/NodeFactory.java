package com.ancevt.d2d2.engine;

import com.ancevt.d2d2.scene.BitmapCanvas;

public interface NodeFactory {

    BitmapCanvas createBitmapCanvas(int width, int height);
}
