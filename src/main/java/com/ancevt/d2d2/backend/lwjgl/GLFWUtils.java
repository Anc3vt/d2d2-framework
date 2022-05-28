/**
 * Copyright (C) 2022 the original author or authors.
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
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ancevt.d2d2.backend.lwjgl.OSDetector.isUnix;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetVideoModes;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;

public class GLFWUtils {

    private static final Map<Long, String> monitorNameMap = new HashMap<>();

    public static @NotNull Map<Long, String> getMonitors() {
        Map<Long, String> monitors = new HashMap();
        PointerBuffer glfwMonitors = glfwGetMonitors();
        for (int i = 0; i < glfwMonitors.limit(); i++) {
            long monitor = glfwMonitors.get(i);
            String name = glfwGetMonitorName(monitor);
            monitors.put(monitor, name);
        }
        return monitors;
    }

    @Contract(" -> new")
    public static int @NotNull [] getWindowInfo() {
        int x[] = new int[1];
        int y[] = new int[1];
        int w[] = new int[1];
        int h[] = new int[1];

        GLFW.glfwGetWindowPos(D2D2.getBackend().getWindowId(), x, y);
        GLFW.glfwGetWindowSize(D2D2.getBackend().getWindowId(), w, h);

        return new int[]{
                x[0], y[0], w[0], h[0]
        };
    }

    public static int @NotNull [] getMonitorInfo(long monitorId) {
        int x[] = new int[1];
        int y[] = new int[1];
        int w[] = new int[1];
        int h[] = new int[1];

        GLFW.glfwGetMonitorWorkarea(monitorId, x, y, w, h);
        return new int[]{
                x[0], y[0], w[0], h[0]
        };
    }

    public static long getMonitorByName(String monitorName) {
        Holder<Long> result = new Holder<>(0L);
        getMonitors().forEach((monitor, name) -> {
            if (monitorName.equals(name)) result.setValue(monitor);
        });
        return result.getValue();
    }

    public static @NotNull List<VideoMode> getVideoModes(long monitor) {
        List<VideoMode> videoModes = new ArrayList<>();

        glfwGetVideoModes(monitor).stream().toList().forEach(glfwVidMode -> {
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

    public static @NotNull VideoMode getMaxVideoMode(long monitor) {
        var videoModes = getVideoModes(monitor);
        return videoModes.get(videoModes.size() - 1);
    }

    public static long getPrimaryMonitorId() {
        return glfwGetPrimaryMonitor();
    }

    public static VideoMode getVideoMode(long monitor) {
        var glfwVideMode = glfwGetVideoMode(monitor);
        return VideoMode.builder()
                .width(glfwVideMode.width())
                .height(glfwVideMode.height())
                .refreshRate(glfwVideMode.refreshRate())
                .build();
    }

    public static void setVideoMode(long monitor, long windowId, int width, int height, int refreshRate) {
        setVideoMode(monitor, windowId,
                getVideoModes(monitor)
                        .stream()
                        .filter(videoMode -> width == videoMode.getWidth() &&
                                height == videoMode.getHeight() &&
                                (refreshRate == -1 || refreshRate == videoMode.getRefreshRate())
                        )
                        .findAny()
                        .orElseThrow(() -> {
                            throw new IllegalArgumentException("video mode " + width + "x" + height + " " + refreshRate + " not supported");
                        })
        );
    }

    @SneakyThrows
    public static void setVideoMode(long monitor, long windowId, @NotNull VideoMode videoMode) {
        if (isUnix()) {
            linuxCare(monitor, videoMode);

        }

        int width = videoMode.getWidth();
        int height = videoMode.getHeight();

        glfwSetWindowMonitor(
                windowId,
                monitor,
                0,
                0,
                videoMode.getWidth(),
                videoMode.getHeight(),
                videoMode.getRefreshRate()
        );

        //D2D2.getStarter().getRenderer().reshape(width, height);
        //D2D2.getStage().onResize(width, height);
    }

    @SneakyThrows
    public static void linuxCare(long monitor, @NotNull VideoMode videoMode) {
        String monitorName = monitorNameMap.get(monitor);
        if (monitorName == null) {
            monitorName = glfwGetMonitorName(monitor);
            monitorNameMap.put(monitor, monitorName);
        }

        new ProcessBuilder("xrandr",
                "--output",
                "" + monitorName,
                "--mode",
                videoMode.getWidth() + "x" + videoMode.getHeight()
        ).start();
        Thread.sleep(1000);
    }
}
