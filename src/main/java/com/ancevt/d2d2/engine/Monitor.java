package com.ancevt.d2d2.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public interface Monitor {

    long getId();

    String getName();

    boolean isPrimary();

    List<VideoMode> getAvailableVideoModes();

    VideoMode getVideoMode();

    void setVideoMode(VideoMode videoMode);

    void reset();

}
