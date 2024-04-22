package com.ancevt.d2d2.engine.lwjgl;

import com.ancevt.d2d2.engine.Monitor;
import com.ancevt.d2d2.engine.MonitorManager;
import com.ancevt.d2d2.exception.MonitorException;
import org.lwjgl.PointerBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwGetMonitors;

public class LwjglMonitorManager implements MonitorManager {

    @Override
    public List<Monitor> getMonitors() {
        List<Monitor> result = new ArrayList<>();
        PointerBuffer glfwMonitors = glfwGetMonitors();
        for (int i = 0; i < Objects.requireNonNull(glfwMonitors).limit(); i++) {
            long id = glfwMonitors.get(i);
            result.add(new LwjglMonitor(id));
        }
        return result;
    }

    @Override
    public Monitor getPrimaryMonitor() {
        return getMonitors().stream()
            .filter(Monitor::isPrimary)
            .findAny()
            .orElseThrow(()->new MonitorException("No primary monitor detected"));
    }
}
