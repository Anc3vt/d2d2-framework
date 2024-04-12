package com.ancevt.d2d2.backend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

public interface VideoModeControl {

    Map<Long, String> getMonitors();

    Info getWindowInfo();

    Info getMonitorInfo(long monitorId);

    long getMonitorIdByName(String name);

    List<VideoMode> getVideoModes(long monitorId);

    long getPrimaryMonitorId();

    VideoMode getVideoMode(long monitorId);

    void setVideoMode(long monitorId, VideoMode videoMode);

    @Getter
    @RequiredArgsConstructor
    class Info {
        private final long id;
        private final int x;
        private final int y;
        private final int width;
        private final int height;
    }
}
