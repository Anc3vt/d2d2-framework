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
package com.ancevt.d2d2.engine.lwjgl;

import com.ancevt.d2d2.engine.Monitor;
import com.ancevt.d2d2.engine.VideoMode;
import lombok.RequiredArgsConstructor;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorPhysicalSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetVideoModes;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;

@RequiredArgsConstructor
public final class LwjglMonitor implements Monitor {

    private final long id;

    private final LwjglDisplayManager displayManager;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return glfwGetMonitorName(id);
    }

    @Override
    public boolean isPrimary() {
        return id == glfwGetPrimaryMonitor();
    }

    @Override
    public List<VideoMode> getAvailableVideoModes() {
        List<VideoMode> videoModes = new ArrayList<>();

        Objects.requireNonNull(glfwGetVideoModes(id))
            .stream()
            .toList()
            .forEach(glfwVidMode ->
                videoModes.add(
                    VideoMode.builder()
                        .width(glfwVidMode.width())
                        .height(glfwVidMode.height())
                        .refreshRate(glfwVidMode.refreshRate())
                        .build()
                )
            );

        return videoModes;
    }

    @Override
    public VideoMode getVideoMode() {
        GLFWVidMode m = Objects.requireNonNull(glfwGetVideoMode(id));
        return new VideoMode(m.width(), m.height(), m.refreshRate());
    }

    @Override
    public void setVideoMode(VideoMode videoMode) {
        displayManager.memorizeWindowState();

        int width = videoMode.getWidth();
        int height = videoMode.getHeight();
        int refreshRate = videoMode.getRefreshRate();
        glfwSetWindowMonitor(displayManager.getWindowId(), id, 0, 0, width, height, refreshRate);
    }

    @Override
    public void reset() {
        displayManager.restoreWindowedMode();
    }

    @Override
    public PhysicalSize getPhysicalSize() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetMonitorPhysicalSize(id, width, height);
        return new PhysicalSize(width[0], height[0]);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id + getClass().hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LwjglMonitor that = (LwjglMonitor) o;

        return id == that.id;
    }


}
