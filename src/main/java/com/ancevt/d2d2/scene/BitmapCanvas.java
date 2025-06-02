package com.ancevt.d2d2.scene;

import com.ancevt.d2d2.D2D2;

public interface BitmapCanvas extends Node {

    void setPixel(int x, int y, int color);

    int getPixel(int x, int y);

    static BitmapCanvas create(int width, int height) {
        return D2D2.engine().nodeFactory().createBitmapCanvas(width, height);
    }

}
