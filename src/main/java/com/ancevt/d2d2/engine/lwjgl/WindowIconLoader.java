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

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WindowIconLoader {

    private static final String ASSET_ICON_DIR = "assets/icon";
    private static final String DEFAULT_ASSET_ICON_DIR = "assets/defaultd2d2windowicon";
    private static final int[] SIZES = {16, 32, 64, 128};

    static void loadIcons(long windowId) {
        boolean success = false;

        if (isIconDirectoryExists()) {
            try {
                success = doLoadIcons(windowId, ASSET_ICON_DIR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // or load fallback D2D2 icon
        if (!success) {
            doLoadIcons(windowId, DEFAULT_ASSET_ICON_DIR);
        }
    }

    private static boolean doLoadIcons(long windowId, String assetIconDirPath) {
        SortedMap<Integer, InputStream> availableSizes = new TreeMap<>();

        for (int size : SIZES) {
            InputStream inputStream = getIconAsInputStream(assetIconDirPath + "/" + size + ".png");

            if (inputStream != null) {
                availableSizes.put(size, inputStream);
            }
        }

        if (availableSizes.isEmpty()) return false;

        GLFWImage.Buffer icons = GLFWImage.malloc(availableSizes.size());

        AtomicInteger counter = new AtomicInteger(0);
        availableSizes.forEach((size, inputStream) -> {
            icons
                .position(counter.get())
                .width(size)
                .height(size)
                .pixels(inputStreamToByteBuffer(inputStream));

            counter.getAndIncrement();
        });

        GLFW.glfwSetWindowIcon(windowId, icons);

        return true;
    }

    private static ByteBuffer inputStreamToByteBuffer(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
            ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
                    buffer.put((byte) (pixel & 0xFF));           // Blue component
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component
                }
            }

            buffer.flip();
            return buffer;
        } catch (IOException e) {
            throw new IllegalStateException("Could't load icon", e);
        }
    }

    private static boolean isIconDirectoryExists() {
        try (InputStream inputStream = WindowIconLoader.class.getClassLoader().getResourceAsStream(ASSET_ICON_DIR)) {
            return inputStream != null;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static InputStream getIconAsInputStream(String assetPath) {
        return WindowIconLoader.class.getClassLoader().getResourceAsStream(assetPath);
    }

}
