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

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.interactive.InteractiveManager;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.FractionalMetrics;
import com.ancevt.d2d2.display.text.TtfBitmapFontBuilder;
import com.ancevt.d2d2.engine.Engine;
import com.ancevt.d2d2.engine.MonitorManager;
import com.ancevt.d2d2.engine.VideoMode;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.LifecycleEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.Mouse;
import com.ancevt.d2d2.lifecycle.SystemProperties;
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
import java.nio.file.Files;
import java.nio.file.Path;
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

// TODO: rewrite with VBO abd refactor
@Slf4j
public class LwjglEngine implements Engine {

    private static final String DEMO_TEXTURE_DATA_INF_FILE = "d2d2-core-demo-texture-data.inf";

    private LwjglRenderer renderer;
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

    private final MonitorManager monitorManager = new LwjglMonitorManager();

    @Getter
    @Setter
    private int timerCheckFrameFrequency = 1;

    public LwjglEngine(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        D2D2.textureManager().setTextureEngine(new LwjglTextureEngine());
    }

    @Override
    public void focusWindow() {
        GLFW.glfwFocusWindow(windowId);
    }

    @Override
    public MonitorManager getMonitorManager() {
        return monitorManager;
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

        if (Objects.equals(System.getProperty(SystemProperties.GLFWHINT_ALWAYSONTOP), "true")) {
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

    @Override
    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
        renderer.setFrameRate(frameRate);
    }

    @Override
    public int getFrameRate() {
        return frameRate;
    }

    @Override
    public int getActualFps() {
        return renderer.getFps();
    }

    private void startRenderLoop() {

        while (!glfwWindowShouldClose(windowId) && alive) {
            glfwPollEvents();
            renderer.renderFrame();
            glfwSwapBuffers(windowId);
            Timer.processTimers();
        }

        String prop = System.getProperty("d2d2.glfw.no-terminate");
        if (prop != null && prop.equals("true")) {
            log.warn("d2d2.glfw.no-terminate is set");
            return;
        }

        glfwTerminate();
    }


    private static void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
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
    public BitmapFont generateBitmapFont(TtfBitmapFontBuilder builder) {

        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        InputStream inputStream = builder.getTtfInputStream() != null ?
            builder.getTtfInputStream() : new FileInputStream(builder.getTtfPath().toFile());

        Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        String fontName = font.getName();
        ge.registerFont(font);

        boolean bold = builder.isBold();
        boolean italic = builder.isItalic();
        int fontSize = builder.getFontSize();
        int fontStyle = Font.PLAIN | (bold ? Font.BOLD : Font.PLAIN) | (italic ? Font.ITALIC : Font.PLAIN);

        font = new Font(fontName, fontStyle, fontSize);

        //TODO: compute atlas height automatically

        int atlasWidth = 32 * builder.getFontSize();
        int atlasHeight = 32 * builder.getFontSize();
        BufferedImage bufferedImage = new BufferedImage(atlasHeight, atlasWidth, BufferedImage.TYPE_INT_ARGB);

        //BufferedImage bufferedImage = new BufferedImage(builder.getAtlasWidth(), builder.getAtlasHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();

        if (builder.fractionalMetrics() != null)
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, FractionalMetrics.nativeValue(builder.fractionalMetrics()));

        if (builder.isTextAntialiasOn())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (builder.isTextAntialiasGasp())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        if (builder.isTextAntialiasLcdHrgb())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if (builder.isTextAntialiasLcdHbgr())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);

        if (builder.isTextAntialiasLcdVrgb())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);

        if (builder.isTextAntialiasLcdVbgr())
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR);

        g2.setColor(Color.WHITE);

        List<CharInfo> charInfos = new ArrayList<>();

        String string = builder.getCharSourceString();

        int x = 0;
        int y = font.getSize();

        for (int i = 0; i < string.length(); i++) {

            char c = string.charAt(i);

            FontMetrics fontMetrics = g2.getFontMetrics(font);
            int w = fontMetrics.charWidth(c);
            int h = fontMetrics.getHeight();
            int toY = fontMetrics.getDescent();

            g2.setFont(font);
            g2.drawString(String.valueOf(c), x, y);

            CharInfo charInfo = new CharInfo();
            charInfo.character = c;
            charInfo.x = x + builder.getOffsetX();
            charInfo.y = y - h + toY + builder.getOffsetY();

            charInfo.width = w + builder.getOffsetX();
            charInfo.height = h + builder.getOffsetY();

            charInfos.add(charInfo);

            x += w + builder.getSpacingX();

            if (x >= bufferedImage.getWidth() - font.getSize()) {
                y += h + builder.getSpacingY();
                x = 0;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        // meta
        stringBuilder.append("#meta ");
        stringBuilder.append("spacingX ").append(builder.getSpacingX()).append(" ");
        stringBuilder.append("spacingY ").append(builder.getSpacingY()).append(" ");
        stringBuilder.append("\n");

        // char infos
        charInfos.forEach(charInfo ->
            stringBuilder
                .append(charInfo.character)
                .append(' ')
                .append(charInfo.x)
                .append(' ')
                .append(charInfo.y)
                .append(' ')
                .append(charInfo.width)
                .append(' ')
                .append(charInfo.height)
                .append('\n')
        );

        byte[] charsDataBytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", pngOutputStream);
        byte[] pngDataBytes = pngOutputStream.toByteArray();

        if (System.getProperty(SystemProperties.D2D2_BITMAPFONT_SAVEBMF) != null) {
            String assetPath = builder.getTtfAssetPath();
            Path ttfPath = builder.getTtfPath();

            String fileName = assetPath != null ?
                Path.of(assetPath).getFileName().toString() : ttfPath.getFileName().toString();

            String saveToPathString = System.getProperty(SystemProperties.D2D2_BITMAPFONT_SAVEBMF);

            Path destinationPath = Files.createDirectories(Path.of(saveToPathString));

            fileName = fileName.substring(0, fileName.length() - 4) + "-" + fontSize;

            Files.write(destinationPath.resolve(fileName + ".png"), pngDataBytes);
            Files.writeString(destinationPath.resolve(fileName + ".bmf"), stringBuilder.toString());
            log.info("BMF written {}/{}", destinationPath, fileName);
        }

        return D2D2.bitmapFontManager().loadBitmapFont(
            new ByteArrayInputStream(charsDataBytes),
            new ByteArrayInputStream(pngDataBytes),
            builder.getName()
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
