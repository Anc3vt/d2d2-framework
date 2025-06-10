package com.ancevt.d2d2.scene;

import com.ancevt.d2d2.D2D2;

public abstract class Bitmap extends AbstractNode {

    abstract public void setPixel(int x, int y, int color);

    abstract public int getPixel(int x, int y);

    abstract public byte[] getPixels(int x, int y, int w, int h);

    abstract public void setPixels(int x, int y, int w, int h, byte[] pixels);

    abstract public void clear();

    public static Bitmap create(int width, int height) {
        return D2D2.getEngine().getNodeFactory().createBitmapCanvas(width, height);
    }
}
