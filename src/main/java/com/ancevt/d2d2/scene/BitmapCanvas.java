package com.ancevt.d2d2.scene;

public class BitmapCanvas extends AbstractNode {

    private final int width;
    private final int height;

    private final int[][] pixels;

    public BitmapCanvas(int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width][height];
    }

    public void setPixel(int x, int y, int color) {
        pixels[x][y] = color;
    }

    public int getPixel(int x, int y) {
        return pixels[x][y];
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
