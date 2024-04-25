package com.ancevt.d2d2.engine.lwjgl;

import com.ancevt.d2d2.engine.Monitor;
import com.ancevt.d2d2.engine.DisplayManager;
import com.ancevt.d2d2.engine.WindowState;
import com.ancevt.d2d2.exception.MonitorException;
import org.lwjgl.PointerBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwFocusWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;

public class LwjglDisplayManager implements DisplayManager {

    private WindowState savedWindowState;
    private boolean mouseVisible;
    private boolean borderless;
    long windowId;
    private boolean visible;


    @Override
    public List<Monitor> getMonitors() {
        List<Monitor> result = new ArrayList<>();
        PointerBuffer glfwMonitors = glfwGetMonitors();
        for (int i = 0; i < Objects.requireNonNull(glfwMonitors).limit(); i++) {
            long id = glfwMonitors.get(i);
            result.add(new LwjglMonitor(id, this));
        }
        return result;
    }

    @Override
    public Monitor getPrimaryMonitor() {
        return getMonitors().stream()
            .filter(Monitor::isPrimary)
            .findAny()
            .orElseThrow(() -> new MonitorException("No primary monitor detected"));
    }

    @Override
    public WindowState getWindowState() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(getWindowId(), width, height);

        int[] xPos = new int[1];
        int[] yPos = new int[1];
        glfwGetWindowPos(getWindowId(), xPos, yPos);

        return new WindowState(windowId, xPos[0], yPos[0], width[0], height[0]);
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    void saveWindowState() {
        if (savedWindowState == null) {
            savedWindowState = getWindowState();
        }
    }

    @Override
    public void restoreWindowedMode() {
        if (savedWindowState != null) {
            System.out.println("savedWindowState != null");
            glfwSetWindowMonitor(
                getWindowId(),
                NULL,
                savedWindowState.getX(),
                savedWindowState.getY(),
                savedWindowState.getWidth(),
                savedWindowState.getHeight(),
                GLFW_DONT_CARE
            );
            System.out.println(savedWindowState);
            savedWindowState = null;
        }
    }

    @Override
    public void setBorderless(boolean borderless) {
        this.borderless = borderless;
        glfwWindowHint(GLFW_DECORATED, borderless ? GLFW_FALSE : GLFW_TRUE);
    }

    @Override
    public boolean isBorderless() {
        return borderless;
    }

    @Override
    public void focusWindow() {
        glfwFocusWindow(getWindowId());
    }

    @Override
    public long getWindowId() {
        return windowId;
    }

    @Override
    public void setWindowXY(int x, int y) {
        glfwSetWindowPos(getWindowId(), x, y);
    }

    @Override
    public void setWindowSize(int width, int height) {
        glfwSetWindowSize(windowId, width, height);
    }

    @Override
    public void setMouseVisible(boolean mouseVisible) {
        this.mouseVisible = mouseVisible;
        glfwSetInputMode(windowId, GLFW_CURSOR, mouseVisible ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_HIDDEN);
    }

    @Override
    public boolean isMouseVisible() {
        return mouseVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        if (this.visible == visible) return;

        this.visible = visible;
        if (visible) {
            glfwShowWindow(windowId);
        } else {
            glfwHideWindow(windowId);
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }
}
