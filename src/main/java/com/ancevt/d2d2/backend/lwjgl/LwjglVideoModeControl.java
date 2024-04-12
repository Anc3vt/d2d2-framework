package com.ancevt.d2d2.backend.lwjgl;

import com.ancevt.commons.Holder;
import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.VideoModeControl;
import com.ancevt.d2d2.backend.VideoMode;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetVideoModes;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;

public class LwjglVideoModeControl implements VideoModeControl {
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
    public VideoMode getVideoMode(long monitorId) {
        GLFWVidMode glfwVideMode = glfwGetVideoMode(monitorId);
        return VideoMode.builder()
            .width(glfwVideMode.width())
            .height(glfwVideMode.height())
            .refreshRate(glfwVideMode.refreshRate())
            .build();
    }

    @Override
    public void setVideoMode(long monitorId, VideoMode videoMode) {
        int width = videoMode.getWidth();
        int height = videoMode.getHeight();

        glfwSetWindowMonitor(
            D2D2.backend().getWindowId(),
            monitorId,
            0,
            0,
            videoMode.getWidth(),
            videoMode.getHeight(),
            videoMode.getRefreshRate()
        );

    }
}