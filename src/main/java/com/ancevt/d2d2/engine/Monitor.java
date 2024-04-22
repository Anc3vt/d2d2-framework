package com.ancevt.d2d2.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public interface Monitor {

    long getId();

    String getName();

    boolean isPrimary();

    List<VideoMode> getVideoModes();

    VideoMode getCurrentVideoMode();

    void setCurrentVideoMode(VideoMode videoMode);


    void reset();

    @RequiredArgsConstructor
    @Getter
    class Rectangle {
        private final long id;
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        public Rectangle clone() {
            return new Rectangle(id, x, y, width, height);
        }
    }
}
