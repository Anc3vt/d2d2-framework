package com.ancevt.d2d2.backend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

public interface VideoModeControl {

    Map<Long, String> getMonitors();

    Info getWindowInfo();

    Info getMonitorInfo(long monitorId);

    long getMonitorIdByName(String name);

    List<VideoMode> getVideoModes(long monitorId);

    long getPrimaryMonitorId();

    VideoMode getCurrentVideoMode(long monitorId);

    void setCurrentVideoMode(long monitorId, VideoMode videoMode);

    VideoMode setCurrentVideoMode(long monitorId, int width, int height, int refreshRate);

    VideoMode getMaxVideoMode(long monitorId);

    void reset(long monitorId);

    @Getter
    @RequiredArgsConstructor
    @ToString
    class Info {
        private final long id;
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        public Info clone() {
            return new Info(id, x, y, width, height);
        }
    }
}
