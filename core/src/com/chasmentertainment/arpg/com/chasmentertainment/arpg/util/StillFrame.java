package com.chasmentertainment.arpg.com.chasmentertainment.arpg.util;

public class StillFrame {
    private final int mX;
    private final int mY;
    private final int mWidth;
    private final int mHeight;

    public StillFrame(int x, int y, int width, int height) {
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }
}
