/**
 * Copyright (C) 2024 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.engine;

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

    int getMaxRefreshRate(long monitorId);

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
