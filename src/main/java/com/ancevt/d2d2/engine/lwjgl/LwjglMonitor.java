package com.ancevt.d2d2.engine.lwjgl;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.engine.Monitor;
import com.ancevt.d2d2.engine.VideoMode;
import lombok.RequiredArgsConstructor;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetVideoModes;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.system.MemoryUtil.NULL;

@RequiredArgsConstructor
public class LwjglMonitor implements Monitor {

    public final long id;

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
    public List<VideoMode> getVideoModes() {
        List<VideoMode> videoModes = new ArrayList<>();

        Objects.requireNonNull(glfwGetVideoModes(id)).stream().toList().forEach(glfwVidMode -> {
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
    public VideoMode getCurrentVideoMode() {
        GLFWVidMode m = Objects.requireNonNull(glfwGetVideoMode(id));
        return new VideoMode(m.width(), m.height(), m.refreshRate());
    }

    @Override
    public void setCurrentVideoMode(VideoMode videoMode) {
        int width = videoMode.getWidth();
        int height = videoMode.getHeight();
        int refreshRate = videoMode.getRefreshRate();
        glfwSetWindowMonitor(getWindowId(), id, 0, 0, width, height, refreshRate);
    }

    @Override
    public void reset() {
        Stage stage = D2D2.stage();

        glfwSetWindowMonitor(
            getWindowId(),
            NULL,
            10,
            10,
            (int) stage.getWidth(),
            (int) stage.getHeight(),
            GLFW_DONT_CARE
        );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LwjglMonitor that = (LwjglMonitor) o;

        return id == that.id;
    }

    private static long getWindowId() {
        return D2D2.engine().getWindowId();
    }
}
