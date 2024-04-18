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

import com.ancevt.d2d2.display.texture.TextureAtlas;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class TextureLoadQueue {

    private Queue<LoadTask> tasks;

    public TextureLoadQueue() {
        tasks = new LinkedList<>();
    }

    public void putLoad(LoadTask loadTask) {
        tasks.add(loadTask);
    }

    public boolean hasTasks() {
        return !tasks.isEmpty();
    }

    public LoadTask poll() {
        return tasks.poll();
    }

    public static class LoadTask {

        private final TextureAtlas textureAtlas;
        private final int width;
        private final int height;
        private final ByteBuffer byteBuffer;

        public LoadTask(TextureAtlas textureAtlas, int width, int height, ByteBuffer byteBuffer) {

            this.textureAtlas = textureAtlas;
            this.width = width;
            this.height = height;
            this.byteBuffer = byteBuffer;
        }

        public TextureAtlas getTextureAtlas() {
            return textureAtlas;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public ByteBuffer getByteBuffer() {
            return byteBuffer;
        }
    }
}
