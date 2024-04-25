package com.ancevt.d2d2.engine;

import java.util.List;

public interface DisplayManager {

    List<Monitor> getMonitors();

    Monitor getPrimaryMonitor();

    void restoreWindowedMode();

    void setWindowSize(int width, int height);

    void setWindowXY(int x, int y);

    WindowState getWindowState();

    void setTitle(String title);

    String getTitle();

    void setVisible(boolean visible);

    boolean isVisible();

    void setBorderless(boolean borderless);

    boolean isBorderless();

    void focusWindow();

    long getWindowId();

    void setMouseVisible(boolean mouseVisible);

    boolean isMouseVisible();
}
