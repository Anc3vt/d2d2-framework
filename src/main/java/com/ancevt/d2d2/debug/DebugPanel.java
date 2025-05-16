/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.event.core.Event;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.input.MouseButton;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Group;
import com.ancevt.d2d2.scene.GroupImpl;
import com.ancevt.d2d2.scene.interactive.InteractiveGroup;
import com.ancevt.d2d2.scene.shape.BorderedRectangle;
import com.ancevt.d2d2.scene.shape.RectangleShape;
import com.ancevt.d2d2.scene.text.BitmapText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Supplier;

public class DebugPanel extends GroupImpl {

    private static final Map<String, DebugPanel> debugPanels = new HashMap<>();
    private static boolean enabled;
    private static float scale = 1;

    private final BitmapText bitmapText;
    private final String systemPropertyName;
    private final RectangleShape bg;
    private final InteractiveGroup interactiveButton;
    private int oldX;
    private int oldY;
    private boolean shiftDown;
    private int mouseButton;

    private final List<Button> buttonList;
    private final Map<String, Button> buttonMap;

    private DebugPanel(String systemPropertyName) {
        debugPanels.put(systemPropertyName, this);

        final int width = 300;
        final int height = 300;

        this.systemPropertyName = systemPropertyName;
        addEventListener(SceneEvent.PostFrame.class, this::this_eachFrame);

        buttonList = new ArrayList<>();
        buttonMap = new HashMap<>();

        bg = new RectangleShape(width, height, Color.BLACK);
        bg.setAlpha(0.75f);
        addChild(bg);

        bitmapText = new BitmapText();
        //text.setBitmapFont(BitmapFont.loadBitmapFont("open-sans/OpenSans-14-Regular"));
        bitmapText.setColor(Color.WHITE);
        bitmapText.setSize(width, height);
        addChild(bitmapText, 1, 1);

        interactiveButton = InteractiveGroup.create(width, height);
        interactiveButton.addEventListener(InputEvent.MouseDown.class, this::interactiveButton_down);
        interactiveButton.addEventListener(InputEvent.MouseDrag.class, this::interactiveButton_drag);

        addEventListener(this, SceneEvent.AddToScene.class, this::this_addToStage);

        addChild(interactiveButton);

        load();

        setScale(scale, scale);
    }


    @Override
    public String toString() {
        return "DebugPanel_" + getName();
    }

    public void setText(Object text) {
        System.setProperty(systemPropertyName, String.valueOf(text));
    }

    public static void setPanelScale(float scale) {
        DebugPanel.scale = scale;
    }

    public static float getScale() {
        return DebugPanel.scale;
    }

    public static void setEnabled(boolean enabled) {
        DebugPanel.enabled = enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    private void this_addToStage(Event event) {
        removeEventListener(this, SceneEvent.AddToScene.class);
        D2D2.root().addEventListener(InputEvent.KeyDown.class, this::root_keyDown);
        D2D2.root().addEventListener(InputEvent.KeyUp.class, this::root_keyUp);
    }

    private void root_keyDown(InputEvent.KeyDown e) {
        if (KeyCode.isShift(e.getKeyCode())) {
            shiftDown = true;
        }
    }

    private void root_keyUp(InputEvent.KeyUp e) {
        if (KeyCode.isShift(e.getKeyCode())) {
            shiftDown = false;
        }
    }

    private void interactiveButton_down(InputEvent.MouseDown e) {
        mouseButton = e.getButton();

        oldX = (int) (e.getX() + getX());
        oldY = (int) (e.getY() + getY());

        Group parent = getParent();
        parent.removeChild(this);
        parent.addChild(this);

        dispatchEvent(e);
    }

    private void interactiveButton_drag(InputEvent.MouseDrag e) {
        if (mouseButton == MouseButton.RIGHT) {
            bg.setSize(e.getX() + 1f, e.getY() + 1f);
            if (bg.getWidth() < 5f) {
                bg.setWidth(5f);
            }
            if (bg.getHeight() < 5f) {
                bg.setHeight(5f);
            }

            bitmapText.setSize(bg.getWidth(), bg.getHeight());
            interactiveButton.setSize(bg.getWidth(), bg.getHeight());

            dispatchEvent(CommonEvent.Resize.create(getWidth(), getHeight()));

            return;
        }

        final int tx = (int) (e.getX() + getX());
        final int ty = (int) (e.getY() + getY());

        move(tx - oldX, ty - oldY);

        oldX = tx;
        oldY = ty;
    }

    public void setWidth(float v) {
        bg.setWidth(v);
        bitmapText.setWidth(bg.getWidth());
        interactiveButton.setWidth(bg.getWidth());
    }

    public void setHeight(float v) {
        bg.setHeight(v);
        bitmapText.setHeight(bg.getHeight());
        interactiveButton.setHeight(bg.getHeight());
    }

    public float getWidth() {
        return bg.getWidth();
    }

    public float getHeight() {
        return bg.getHeight();
    }

    public void setSize(float w, float h) {
        setWidth(w);
        setHeight(h);
    }

    @Override
    public void setX(float value) {
        super.setX(value);
    }

    private void this_eachFrame(Event event) {
        if (System.getProperty(systemPropertyName) != null) {
            bitmapText.setText("[" + systemPropertyName + "]\n" + System.getProperty(systemPropertyName));
        }

        if (bg.getWidth() < 10) bg.setWidth(10);
        if (bitmapText.getWidth() < 10) bitmapText.setWidth(10);
    }

    private void load() {
        File f = file();
        if (f.exists()) {
            String string = readFromFile(f);
            JsonObject o = JsonParser.parseString(string).getAsJsonObject();
            float x = o.get("x").getAsFloat();
            float y = o.get("y").getAsFloat();
            float w = o.get("w").getAsFloat();
            float h = o.get("h").getAsFloat();
            String data = o.get("data").getAsString();

            bg.setSize(w, h);
            bitmapText.setSize(w, h);
            interactiveButton.setSize(w, h);
            bitmapText.setText(data);
            setPosition(x, y);
        }
    }

    private void save() {
        JsonObject o = new JsonObject();
        o.addProperty("x", getX());
        o.addProperty("y", getY());
        o.addProperty("w", getWidth());
        o.addProperty("h", getHeight());
        o.addProperty("data", bitmapText.getText());
        saveToFile(file(), o.toString());
    }

    private File file() {
        return file(MD5.hash(systemPropertyName) + ".json");
    }

    private static void saveToFile(File file, String string) {
        try {
            Files.writeString(
                    Path.of(file.getAbsolutePath()),
                    string,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFromFile(File file) {
        try {
            return Files.readString(Path.of(file.getAbsolutePath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private static File directory() {
        File dir = new File(
                System.getProperty("user.home")
                        + File.separator
                        + ".d2d2-debug-panel"
                        + File.separator
                        + ApplicationMainClassNameExtractor.getMainClassName()
        );

        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File file(String name) {
        return new File(directory().getAbsolutePath() + File.separator + name);
    }

    public DebugPanel addButton(String text, Runnable onPress) {
        if (!buttonMap.containsKey(text)) {
            Button button = new Button(text);
            button.pressFunction = onPress;
            addChild(button, buttonList.size() * (Button.DEFAULT_WIDTH + 1), -Button.DEFAULT_HEIGHT);
            buttonList.add(button);
            buttonMap.put(text, button);
        }

        return this;
    }

    public static void saveAll() {
        debugPanels.values().forEach(DebugPanel::save);
    }

    public static Optional<DebugPanel> get(String propertyName) {
        return Optional.ofNullable(debugPanels.get(propertyName));
    }

    public static Optional<DebugPanel> show(String propertyName) {
        return show(propertyName, "");
    }

    public static Optional<DebugPanel> show(String propertyName, Object value) {
        if (enabled) {
            DebugPanel debugPanel = debugPanels.get(propertyName);
            if (debugPanel == null) {
                debugPanel = new DebugPanel(propertyName);
            }

            D2D2.root().addChild(debugPanel);
            if (propertyName != null) {
                System.setProperty(propertyName, String.valueOf(value));
            }
            return Optional.of(debugPanel);
        }
        return Optional.empty();
    }

    public static Optional<DebugPanel> show(String propertyName, Supplier<Object> supplier) {
        return show(propertyName, supplier.get());
    }

    public static void setProperty(String key, Object value) {
        System.setProperty(key, String.valueOf(value));
    }

    public static class Button extends BorderedRectangle {

        private static final float DEFAULT_WIDTH = 50f;
        private static final float DEFAULT_HEIGHT = 12f;

        private final InteractiveGroup interactiveButton;

        private Runnable pressFunction;

        public Button(Object text) {
            super(DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.BLACK, Color.WHITE);
            setBorderWidth(0.2f);
            interactiveButton = InteractiveGroup.create(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            BitmapText bitmapText = new BitmapText();
            bitmapText.setText(String.valueOf(text));

            addChild(interactiveButton);
            addChild(bitmapText, 2, -2);

            interactiveButton.addEventListener(InputEvent.MouseDown.class, this::interactiveButton_down);
        }

        private void interactiveButton_down(Event event) {
            if (pressFunction != null) {
                pressFunction.run();
            }
        }


    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ApplicationMainClassNameExtractor {
        /**
         * Retrieves the fully qualified name of the main class of the application.
         *
         * @return The fully qualified name of the main class.
         * @throws ApplicationMainClassNameExtractor.MainClassNameExtractorException if the main class name cannot be extracted reliably.
         */
        public static String getMainClassName() throws ApplicationMainClassNameExtractor.MainClassNameExtractorException {
            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
                Thread thread = entry.getKey();
                if (thread.getId() == 1) {
                    StackTraceElement[] stackTraceElements = entry.getValue();
                    for (int i = stackTraceElements.length - 1; i >= 0; i--) {
                        StackTraceElement stackTraceElement = stackTraceElements[i];
                        if (stackTraceElement.getMethodName().equals("main")) {
                            return stackTraceElement.getClassName();
                        }
                    }
                }
            }
            throw new ApplicationMainClassNameExtractor.MainClassNameExtractorException("Unable to extract application main class name");
        }

        public static class MainClassNameExtractorException extends RuntimeException {

            public MainClassNameExtractorException(String message) {
                super(message);
            }
        }
    }


    public class MD5 {
        public static byte[] hash(byte[] bytes) {
            try {
                return MessageDigest.getInstance("MD5").digest(bytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public static String hash(String string) {
            return bytesToHex(hash(string.getBytes(StandardCharsets.UTF_8)));
        }

        public static String hashFile(String path) {
            try {
                return bytesToHex(hash(Files.readAllBytes(Paths.get(path))));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public static String hashFile(Path file) {
            try {
                return bytesToHex(hash(Files.readAllBytes(file)));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public static String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

}
