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
package com.ancevt.d2d2.backend.lwjgl;

import com.ancevt.commons.Holder;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.VideoMode;
import com.ancevt.d2d2.backend.VideoModeControl;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetVideoModes;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LwjglVideoModeControl implements VideoModeControl {

    private Info savedWindowInfo;

    @Override
    public Map<Long, String> getMonitors() {
        Map<Long, String> monitors = new HashMap<>();
        PointerBuffer glfwMonitors = glfwGetMonitors();
        for (int i = 0; i < Objects.requireNonNull(glfwMonitors).limit(); i++) {
            long monitor = glfwMonitors.get(i);
            String name = glfwGetMonitorName(monitor);
            monitors.put(monitor, name);
        }
        return monitors;
    }

    @Override
    public Info getWindowInfo() {
        int x[] = new int[1];
        int y[] = new int[1];
        int w[] = new int[1];
        int h[] = new int[1];

        GLFW.glfwGetWindowPos(D2D2.backend().getWindowId(), x, y);
        GLFW.glfwGetWindowSize(D2D2.backend().getWindowId(), w, h);

        return new Info(D2D2.backend().getWindowId(), x[0], y[0], w[0], h[0]);
    }

    @Override
    public Info getMonitorInfo(long monitorId) {
        int x[] = new int[1];
        int y[] = new int[1];
        int w[] = new int[1];
        int h[] = new int[1];

        GLFW.glfwGetMonitorWorkarea(monitorId, x, y, w, h);
        return new Info(monitorId, x[0], y[0], w[0], h[0]);
    }

    @Override
    public long getMonitorIdByName(String name) {
        Holder<Long> result = new Holder<>(0L);
        getMonitors().forEach((monitor, n) -> {
            if (Objects.equals(name, n)) result.setValue(monitor);
        });
        return result.getValue();
    }

    @Override
    public List<VideoMode> getVideoModes(long monitorId) {
        List<VideoMode> videoModes = new ArrayList<>();

        Objects.requireNonNull(glfwGetVideoModes(monitorId)).stream().toList().forEach(glfwVidMode -> {
            videoModes.add(
                VideoMode.builder()
                    .width(glfwVidMode.width())
                    .height(glfwVidMode.height())
                    .refreshRate(glfwVidMode.refreshRate())
                    .build()
            );
        });

        return videoModes;
    }

    @Override
    public long getPrimaryMonitorId() {
        return glfwGetPrimaryMonitor();
    }

    @Override
    public VideoMode getCurrentVideoMode(long monitorId) {
        GLFWVidMode glfwVideMode = glfwGetVideoMode(monitorId);
        return VideoMode.builder()
            .width(glfwVideMode.width())
            .height(glfwVideMode.height())
            .refreshRate(glfwVideMode.refreshRate())
            .build();
    }

    @Override
    public void setCurrentVideoMode(long monitorId, VideoMode videoMode) {
        int width = videoMode.getWidth();
        int height = videoMode.getHeight();
        int refreshRate = videoMode.getRefreshRate();
        glfwSetWindowMonitor(getWindowInfo().getId(), monitorId, 0, 0, width, height, refreshRate);
    }

    @Override
    public VideoMode setCurrentVideoMode(long monitorId, int width, int height, int refreshRate) {
        savedWindowInfo = getWindowInfo().clone();

        List<VideoMode> videoModes = getVideoModes(monitorId);
        VideoMode videoMode = videoModes
            .stream()
            .filter(vm -> vm.getWidth() == width)
            .filter(vm -> vm.getHeight() == height)
            .filter(vm -> vm.getRefreshRate() == refreshRate)
            .findFirst()
            .orElseThrow();
        setCurrentVideoMode(monitorId, videoMode);
        return videoMode;
    }

    @Override
    public VideoMode getMaxVideoMode(long monitorId) {
        List<VideoMode> videoModes = getVideoModes(monitorId);
        return videoModes.get(videoModes.size() - 1);
    }

    @Override
    public int getMaxRefreshRate(long monitorId) {
        return getVideoModes(monitorId).stream().mapToInt(VideoMode::getRefreshRate).max().orElseThrow();
    }

    @Override
    public void reset(long monitorId) {
        if (savedWindowInfo == null) {
            savedWindowInfo = getWindowInfo().clone();
        }

        glfwSetWindowMonitor(
            getWindowInfo().getId(),
            NULL,
            savedWindowInfo.getX(),
            savedWindowInfo.getY(),
            savedWindowInfo.getWidth(),
            savedWindowInfo.getHeight(),
            GLFW_DONT_CARE
        );

        savedWindowInfo = null;
    }


}
