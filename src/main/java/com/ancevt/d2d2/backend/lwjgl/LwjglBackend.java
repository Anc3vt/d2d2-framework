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

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.D2D2Backend;
import com.ancevt.d2d2.backend.VideoMode;
import com.ancevt.d2d2.backend.VideoModeControl;
import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.FractionalMetrics;
import com.ancevt.d2d2.display.text.TtfBitmapFontBuilder;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.LifecycleEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.Mouse;
import com.ancevt.d2d2.interactive.InteractiveManager;
import com.ancevt.d2d2.time.Timer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_FLOATING;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_ALT;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
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
public class LwjglBackend implements D2D2Backend {

    private static final String DEMO_TEXTURE_DATA_INF_FILE = "d2d2-core-demo-texture-data.inf";

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
    private boolean alive;
    private boolean borderless;
    private int frameRate = 60;
    private int fps = frameRate;
    private boolean alwaysOnTop;
    private boolean control;
    private boolean shift;
    private boolean alt;
    private int tick;
    private int frameCounter;
    private long time;
    private final VideoModeControl videoModeControl = new LwjglVideoModeControl();

    @Getter
    @Setter
    private int timerCheckFrameFrequency = 1;

    public LwjglBackend(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        D2D2.textureManager().setTextureEngine(new LwjglTextureEngine());
    }

    @Override
    public VideoModeControl getVideoModeControl() {
        return videoModeControl;
    }

    @Override
    public void setAlwaysOnTop(boolean b) {
        this.alwaysOnTop = b;

        glfwWindowHint(GLFW_FLOATING, alwaysOnTop ? GLFW_TRUE : GLFW_FALSE);
    }

    @Override
    public boolean isAlwaysOnTop() {
        return alwaysOnTop;
    }

    @Override
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    @Override
    public int getFrameRate() {
        return frameRate;
    }

    @Override
    public void stop() {
        if (!alive) return;
        alive = false;
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
        renderer = new LwjglRenderer(stage, this);
        ((LwjglRenderer) renderer).setLWJGLTextureEngine((LwjglTextureEngine) D2D2.textureManager().getTextureEngine());
        windowId = createWindow();
        setVisible(true);
    }

    @Override
    public long getWindowId() {
        return windowId;
    }

    @Override
    public void setSmoothMode(boolean value) {
        ((LwjglRenderer) renderer).smoothMode = value;

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        if (value) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        } else {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        }
    }


    @Override
    public boolean isSmoothMode() {
        return ((LwjglRenderer) renderer).smoothMode;
    }

    @Override
    public void start() {
        alive = true;
        stage.dispatchEvent(
            LifecycleEvent.builder()
                .type(LifecycleEvent.START_MAIN_LOOP)
                .build()
        );
        startRenderLoop();
        stage.dispatchEvent(
            LifecycleEvent.builder()
                .type(LifecycleEvent.EXIT_MAIN_LOOP)
                .build()
        );
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

    @Override
    public IRenderer getRenderer() {
        return renderer;
    }

    private long createWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();

        if (Objects.equals(System.getProperty("glfwhint.alwaysontop"), "true")) {
            glfwWindowHint(GLFW_FLOATING, 1);
        }

        long windowId = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowId == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        monitor = glfwGetPrimaryMonitor();

        /* TODO: takme care of linux any other way
        glfwSetWindowCloseCallback(windowId, window -> {
            if (OSDetector.isUnix()) {
                GLFWUtils.linuxCare(monitor, previousVideoMode);
            }
        });

         */

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
                stage.dispatchEvent(InputEvent.builder()
                    .type(InputEvent.MOUSE_WHEEL)
                    .x(Mouse.getX())
                    .y(Mouse.getY())
                    .delta((int) dy)
                    .control(control)
                    .shift(shift)
                    .drag(isDown)
                    .build());
            }
        });

        glfwSetMouseButtonCallback(windowId, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int mouseButton, int action, int mods) {
                isDown = action == 1;

                stage.dispatchEvent(InputEvent.builder()
                    .type(action == 1 ? InputEvent.MOUSE_DOWN : InputEvent.MOUSE_UP)
                    .x(Mouse.getX())
                    .y(Mouse.getY())
                    .drag(isDown)
                    .mouseButton(mouseButton)
                    .build());

                InteractiveManager.getInstance().screenTouch(mouseX, mouseY, 0, mouseButton, isDown);
            }
        });

        glfwSetCursorPosCallback(windowId, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                mouseX = (int) x;
                mouseY = (int) y;

                //Mouse.setXY(getTransformedX(mouseX), getTransformedY(mouseY));
                Mouse.setXY(mouseX, mouseY);

                stage.dispatchEvent(InputEvent.builder()
                    .type(InputEvent.MOUSE_MOVE)
                    .x(Mouse.getX())
                    .y(Mouse.getY())
                    .drag(isDown)
                    .build());

                InteractiveManager.getInstance().screenDrag(0, mouseX, mouseY);
            }
        });

        glfwSetCharCallback(windowId, (window, codepoint) -> {
            stage.dispatchEvent(InputEvent.builder()
                .type(InputEvent.KEY_TYPE)
                .x(Mouse.getX())
                .y(Mouse.getY())
                .alt(alt)
                .control(control)
                .shift(shift)
                .drag(isDown)
                .codepoint(codepoint)
                .keyType(String.valueOf(Character.toChars(codepoint)))
                .build());
        });

        glfwSetKeyCallback(windowId, (window, key, scancode, action, mods) -> {

            switch (action) {
                case GLFW_PRESS -> {
                    if (key == KeyCode.LEFT_SHIFT || key == KeyCode.RIGHT_SHIFT) shift = true;
                    if (key == KeyCode.LEFT_CONTROL || key == KeyCode.RIGHT_CONTROL) control = true;
                    if (key == KeyCode.LEFT_ALT || key == KeyCode.RIGHT_ALT) alt = true;

                    stage.dispatchEvent(InputEvent.builder()
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
                }

                case GLFW_REPEAT -> stage.dispatchEvent(InputEvent.builder()
                    .type(InputEvent.KEY_REPEAT)
                    .x(Mouse.getX())
                    .y(Mouse.getY())
                    .keyCode(key)
                    .keyChar((char) key)
                    .drag(isDown)
                    .shift((mods & GLFW_MOD_SHIFT) != 0)
                    .control((mods & GLFW_MOD_CONTROL) != 0)
                    .alt((mods & GLFW_MOD_ALT) != 0)
                    .build());

                case GLFW_RELEASE -> {
                    if (key == KeyCode.LEFT_SHIFT || key == KeyCode.RIGHT_SHIFT) shift = false;
                    if (key == KeyCode.LEFT_CONTROL || key == KeyCode.RIGHT_CONTROL) control = false;
                    if (key == KeyCode.LEFT_ALT || key == KeyCode.RIGHT_ALT) alt = false;

                    stage.dispatchEvent(InputEvent.builder()
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
            }
        });

        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(
            windowId,
            windowX = (videoMode.width() - width) / 2,
            windowY = (videoMode.height() - height) / 2
        );

        videoModeWidth = videoMode.width();
        videoModeHeight = videoMode.height();

        glfwMakeContextCurrent(windowId);
        GL.createCapabilities();

        glfwSwapInterval(1);

        // TODO: remove loading demo texture data info from here
        D2D2.textureManager().loadTextureDataInfo(DEMO_TEXTURE_DATA_INF_FILE);

        renderer.init(windowId);
        renderer.reshape(width, height);

        glfwWindowHint(GLFW.GLFW_SAMPLES, 4);

        setSmoothMode(false);

        previousVideoMode = VideoMode.builder()
            .width(videoModeWidth)
            .height(videoModeHeight)
            .refreshRate(videoMode.refreshRate())
            .build();

        return windowId;
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

    @Override
    public int getFps() {
        return fps;
    }

    private void startRenderLoop() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1000000000.0 / frameRate;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (!glfwWindowShouldClose(windowId) && alive) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerFrame;
            lastTime = now;

            while (delta >= 1) {
                glfwPollEvents();
                renderer.renderFrame();
                glfwSwapBuffers(windowId);
                frames++;
                delta--;
            }

            if (frames < frameRate) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fps = frames;
                frames = 0;
            }
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

    @SneakyThrows
    @Override
    public BitmapFont generateBitmapFont(TtfBitmapFontBuilder ttfBitmapFontBuilder) {

        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        InputStream inputStream = ttfBitmapFontBuilder.getTtfInputStream() != null ?
            ttfBitmapFontBuilder.getTtfInputStream() : new FileInputStream(ttfBitmapFontBuilder.getTtfPath().toFile());

        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        String fontName = font.getName();
        ge.registerFont(font);

        boolean bold = ttfBitmapFontBuilder.isBold();
        boolean italic = ttfBitmapFontBuilder.isItalic();
        int fontSize = ttfBitmapFontBuilder.getFontSize();
        int fontStyle = Font.PLAIN | (bold ? Font.BOLD : Font.PLAIN) | (italic ? Font.ITALIC : Font.PLAIN);

        font = new Font(fontName, fontStyle, fontSize);

        BufferedImage bufferedImage = new BufferedImage(ttfBitmapFontBuilder.getAtlasWidth(), ttfBitmapFontBuilder.getAtlasHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();

        if (ttfBitmapFontBuilder.fractionalMetrics() != null)
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, FractionalMetrics.nativeValue(ttfBitmapFontBuilder.fractionalMetrics()));

        if (ttfBitmapFontBuilder.isTextAntialiasOn())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (ttfBitmapFontBuilder.isTextAntialiasGasp())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        if (ttfBitmapFontBuilder.isTextAntialiasLcdHrgb())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if (ttfBitmapFontBuilder.isTextAntialiasLcdHbgr())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);

        if (ttfBitmapFontBuilder.isTextAntialiasLcdVrgb())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);

        if (ttfBitmapFontBuilder.isTextAntialiasLcdVbgr())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR);

        g2.setColor(Color.WHITE);

        List<CharInfo> charInfos = new ArrayList<>();

        String string = ttfBitmapFontBuilder.getCharSourceString();

        int x = 0, y = font.getSize();

        for (int i = 0; i < string.length(); i++) {

            char c = string.charAt(i);

            FontMetrics fontMetrics = g2.getFontMetrics(font);
            int width = fontMetrics.charWidth(c);
            int height = fontMetrics.getHeight();
            int toY = fontMetrics.getDescent();

            g2.setFont(font);
            g2.drawString(String.valueOf(c), x, y);

            CharInfo charInfo = new CharInfo();
            charInfo.character = c;
            charInfo.x = x;
            charInfo.y = y - height + toY + ttfBitmapFontBuilder.getOffsetY();

            charInfo.width = width;
            charInfo.height = height + ttfBitmapFontBuilder.getOffsetY();

            charInfos.add(charInfo);

            x += width + ttfBitmapFontBuilder.getSpacingX();

            if (x >= bufferedImage.getWidth() - font.getSize()) {
                y += height + ttfBitmapFontBuilder.getSpacingY();
                x = 0;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        charInfos.forEach(charInfo -> {
            stringBuilder.append(charInfo.character);
            stringBuilder.append(' ');
            stringBuilder.append(charInfo.x);
            stringBuilder.append(' ');
            stringBuilder.append(charInfo.y);
            stringBuilder.append(' ');
            stringBuilder.append(charInfo.width);
            stringBuilder.append(' ');
            stringBuilder.append(charInfo.height);
            stringBuilder.append('\n');
        });

        byte[] charsDataBytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", pngOutputStream);

        byte[] pngDataBytes = pngOutputStream.toByteArray();

        return D2D2.bitmapFontManager().loadBitmapFont(
            new ByteArrayInputStream(charsDataBytes),
            new ByteArrayInputStream(pngDataBytes),
            ttfBitmapFontBuilder.getName()
        );
    }

    private static class CharInfo {
        public char character;
        public int x;
        public int y;
        public int width;
        public int height;

        @Override
        public String toString() {
            return "CharInfo{" +
                "character=" + character +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
        }
    }
}
