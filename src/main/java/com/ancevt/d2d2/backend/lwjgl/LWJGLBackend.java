/*
 *     Copyright 2015-2022 Ancevt (me@ancevt.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.backend.lwjgl;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.D2D2Backend;
import com.ancevt.d2d2.backend.VideoMode;
import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.ShaderProgram;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.input.Mouse;
import com.ancevt.d2d2.interactive.TouchProcessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static com.ancevt.d2d2.backend.lwjgl.OSDetector.isUnix;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_ALT;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

@Slf4j
public class LWJGLBackend implements D2D2Backend {

    private static final String DEMO_TEXTURE_DATA_INF_FILE = "d2d2-core-demo-texture-data.inf";

    private static final String DEFAULT_BITMAP_FONT = "Terminus.bmf";

    private IRenderer renderer;
    long windowId;
    private boolean mouseVisible;

    private int width;
    private int height;
    private String title;

    private boolean visible;

    private int mouseX;
    private int mouseY;
    private boolean isDown;

    private Stage stage;
    private boolean fullscreen;
    private int windowX;
    private int windowY;
    private int windowWidth;
    private int windowHeight;
    private int videoModeWidth;
    private int videoModeHeight;
    private long monitor;
    private VideoMode previousVideoMode;
    private boolean stopped;

    public LWJGLBackend(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        D2D2.getTextureManager().setTextureEngine(new LWJGLTextureEngine());
    }

    @Override
    public void stop() {
        if (stopped) return;
        stopped = true;
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
    public void create() {
        stage = new Stage();
        stage.onResize(width, height);
        renderer = new LWJGLRenderer(stage, this);
        ((LWJGLRenderer) renderer).setLWJGLTextureEngine((LWJGLTextureEngine) D2D2.getTextureManager().getTextureEngine());
        windowId = createWindow();
        setVisible(true);
    }

    @Override
    public long getWindowId() {
        return windowId;
    }

    @Override
    public void setSmoothMode(boolean value) {
        ((LWJGLRenderer) renderer).smoothMode = value;
    }

    @Override
    public boolean isSmoothMode() {
        return ((LWJGLRenderer) renderer).smoothMode;
    }

    @Override
    public void start() {
        startRenderLoop();
    }

    @Override
    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;

        glfwSetWindowSize(windowId, width, height);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setVisible(boolean value) {
        this.visible = value;
        if (value) {
            glfwShowWindow(windowId);
        } else {
            glfwHideWindow(windowId);
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public IRenderer getRenderer() {
        return renderer;
    }

    private long createWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();

        long windowId = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowId == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        monitor = glfwGetPrimaryMonitor();

        glfwSetWindowCloseCallback(windowId, window -> {
            if (isUnix()) {
                GLFWUtils.linuxCare(monitor, previousVideoMode);
            }
        });

        glfwSetWindowSizeCallback(windowId, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int width, int height) {
                renderer.reshape(width, height);
                stage.onResize(width, height);
            }
        });

        glfwSetScrollCallback(windowId, new GLFWScrollCallback() {
            @Override
            public void invoke(long win, double dx, double dy) {
                stage.getRoot().dispatchEvent(InputEvent.builder()
                        .type(InputEvent.MOUSE_WHEEL)
                        .x(Mouse.getX())
                        .y(Mouse.getY())
                        .delta((int) dy)
                        .drag(isDown)
                        .build());
            }
        });

        glfwSetMouseButtonCallback(windowId, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                isDown = action == 1;

                stage.getRoot().dispatchEvent(InputEvent.builder()
                        .type(action == 1 ? InputEvent.MOUSE_DOWN : InputEvent.MOUSE_UP)
                        .x(Mouse.getX())
                        .y(Mouse.getY())
                        .drag(isDown)
                        .mouseButton(button)
                        .build());

                TouchProcessor.instance.screenTouch(mouseX, mouseY, 0, button, isDown);
            }
        });

        glfwSetCursorPosCallback(windowId, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                mouseX = (int) x;
                mouseY = (int) y;

                //Mouse.setXY(getTransformedX(mouseX), getTransformedY(mouseY));
                Mouse.setXY(mouseX, mouseY);

                stage.getRoot().dispatchEvent(InputEvent.builder()
                        .type(InputEvent.MOUSE_MOVE)
                        .x(Mouse.getX())
                        .y(Mouse.getY())
                        .drag(isDown)
                        .build());

                TouchProcessor.instance.screenDrag(0, mouseX, mouseY);
            }
        });

        glfwSetCharCallback(windowId, (window, codepoint) -> {
            stage.getRoot().dispatchEvent(InputEvent.builder()
                    .type(InputEvent.KEY_TYPE)
                    .x(Mouse.getX())
                    .y(Mouse.getY())
                    .drag(isDown)
                    .codepoint(codepoint)
                    .keyType(String.valueOf(Character.toChars(codepoint)))
                    .build());
        });

        glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                stage.getRoot().dispatchEvent(InputEvent.builder()
                        .type(InputEvent.KEY_DOWN)
                        .x(Mouse.getX())
                        .y(Mouse.getY())
                        .keyChar((char) key)
                        .keyCode(key)
                        .drag(isDown)
                        .shift((mods & GLFW_MOD_SHIFT) != 0)
                        .control((mods & GLFW_MOD_CONTROL) != 0)
                        .alt((mods & GLFW_MOD_ALT) != 0)
                        .build());
            } else if (action == GLFW_RELEASE) {
                stage.getRoot().dispatchEvent(InputEvent.builder()
                        .type(InputEvent.KEY_UP)
                        .x(Mouse.getX())
                        .y(Mouse.getY())
                        .keyCode(key)
                        .keyChar((char) key)
                        .drag(isDown)
                        .shift((mods & GLFW_MOD_SHIFT) != 0)
                        .control((mods & GLFW_MOD_CONTROL) != 0)
                        .alt((mods & GLFW_MOD_ALT) != 0)
                        .build());
            }
        });

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
                windowId,
                (videoMode.width() - width) / 2,
                (videoMode.height() - height) / 2
        );

        videoModeWidth = videoMode.width();
        videoModeHeight = videoMode.height();

        glfwMakeContextCurrent(windowId);
        GL.createCapabilities();

        glfwSwapInterval(1);

        BitmapFont.setDefaultBitmapFont(BitmapFont.loadBitmapFont(DEFAULT_BITMAP_FONT));

        // TODO: remove loading demo texture data info from here
        D2D2.getTextureManager().loadTextureDataInfo(DEMO_TEXTURE_DATA_INF_FILE);

        renderer.init(windowId);
        renderer.reshape(width, height);

        glfwWindowHint(GLFW.GLFW_SAMPLES, 4);

        previousVideoMode = VideoMode.builder()
                .width(videoModeWidth)
                .height(videoModeHeight)
                .refreshRate(videoMode.refreshRate())
                .build();

        return windowId;
    }

    @Override
    public void putToClipboard(String string) {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(
                        new StringSelection(string),
                        null
                );
    }

    @Override
    public String getStringFromClipboard() {
        try {
            return Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .getData(DataFlavor.stringFlavor).toString();
        } catch (UnsupportedFlavorException e) {
            //e.printStackTrace(); // ignore exception
            return "";
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @SneakyThrows
    @Override
    public void setFullscreen(boolean value) {
        if (!fullscreen) {
            int[] x = new int[1];
            int[] y = new int[1];
            int[] w = new int[1];
            int[] h = new int[1];
            glfwGetWindowPos(windowId, x, y);
            glfwGetWindowSize(windowId, w, h);
            windowX = x[0];
            windowY = y[0];
            windowWidth = w[0];
            windowHeight = h[0];
        }

        if (value) {
            glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
            glfwSetWindowPos(windowId, 0, -20);
            glfwSetWindowSize(windowId, videoModeWidth, videoModeHeight);
        } else {
            glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
            glfwSetWindowPos(windowId, windowX, windowY);
            glfwSetWindowSize(windowId, windowWidth, windowHeight);
        }

        this.fullscreen = value;
    }

    @Override
    public boolean isFullscreen() {
        return fullscreen;
    }

    private void startRenderLoop() {
        while (!glfwWindowShouldClose(windowId)) {
            glfwPollEvents();

            try {
                renderer.renderFrame();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            glfwSwapBuffers(windowId);
        }

        String prop = System.getProperty("d2d2.glfw.no-terminate");
        if (prop != null && prop.equals("true")) {
            log.warn("d2d2.glfw.no-terminate is set");
            return;
        }

        glfwTerminate();
    }

    @Override
    public void setWindowXY(int x, int y) {
        windowX = x;
        windowY = y;
        glfwSetWindowPos(windowId, x, y);
    }

    @Override
    public int getWindowX() {
        return windowX;
    }

    @Override
    public int getWindowY() {
        return windowY;
    }

    @Override
    public void disposeShaderProgram(@NotNull ShaderProgram shaderProgram) {
        GL20.glUseProgram(0);
        GL20.glDeleteShader(shaderProgram.getVertexShaderHandle());
        GL20.glDeleteShader(shaderProgram.getFragmentShaderHandle());
        GL20.glDeleteProgram(shaderProgram.getId());
    }

    @Override
    public int prepareShaderProgram(@NotNull ShaderProgram shaderProgram) {
        StringBuilder logStringBuilder = new StringBuilder();

        int vertexShaderHandle = loadShader(GL20.GL_VERTEX_SHADER, shaderProgram.getVertexShaderSource(), logStringBuilder);
        int fragmentShaderHandle = loadShader(GL20.GL_FRAGMENT_SHADER, shaderProgram.getFragmentShaderSource(), logStringBuilder);

        if (vertexShaderHandle == -1 || fragmentShaderHandle == -1) {
            shaderProgram.setLog(logStringBuilder.toString());
            return -1;
        }

        shaderProgram.setHandles(vertexShaderHandle, fragmentShaderHandle);

        int program = GL20.glCreateProgram();
        if (program == 0) {
            shaderProgram.setLog(logStringBuilder.toString());
            return -1;
        }

        GL20.glAttachShader(program, vertexShaderHandle);
        GL20.glAttachShader(program, fragmentShaderHandle);
        GL20.glLinkProgram(program);

        ByteBuffer tmp = ByteBuffer.allocateDirect(4);
        tmp.order(ByteOrder.nativeOrder());
        IntBuffer buff = tmp.asIntBuffer();

        GL20.glGetProgramiv(program, GL20.GL_LINK_STATUS, buff);

        int linked = buff.get(0);
        if (linked == 0) {
            logStringBuilder.append(GL20.glGetProgramInfoLog(program));
            shaderProgram.setLog(logStringBuilder.toString());
            return -1;
        }

        return program;
    }

    private int loadShader(int type, String source, StringBuilder logStringBuilder) {
        IntBuffer intbuf = newIntBuffer(1);

        int shader = GL20.glCreateShader(type);
        if (shader == 0) return -1;

        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);
        GL20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, intbuf);

        int compiled = intbuf.get(0);
        if (compiled == 0) {
            String infoLog = GL20.glGetShaderInfoLog(shader);
            logStringBuilder.append(type == GL20.GL_VERTEX_SHADER ? "Vertex shader\n" : "Fragment shader:\n");
            logStringBuilder.append(infoLog);
            return -1;
        }

        return shader;
    }

    public static @NotNull IntBuffer newIntBuffer(int numInts) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(numInts * 4);
        buffer.order(ByteOrder.nativeOrder());
        return buffer.asIntBuffer();
    }
}


















