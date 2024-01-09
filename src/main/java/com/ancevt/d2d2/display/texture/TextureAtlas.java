/**
 * Copyright (C) 2023 the original author or authors.
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
package com.ancevt.d2d2.display.texture;

import com.ancevt.util.args.Args;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

public class TextureAtlas {

    private final int id;

    private int width;
    private int height;

    private boolean disposed;

    private TextureAtlas(int id) {
        this.id = id;
    }

    public TextureAtlas(int id, int width, int height) {
        this(id);
        setUp(width, height);
    }

    final void setUp(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Texture createTexture() {
        return createTexture(0, 0, getWidth(), getHeight());
    }

    public Texture createTexture(int x, int y, int width, int height) {
        return new Texture(this, x, y, width, height);
    }

    /**
     * 16,16,48,48
     *
     * @param textureCoords
     * @return
     */
    public Texture createTexture(String textureCoords) {
        var a = Args.of(textureCoords, ',');
        return new Texture(this, a.next(int.class), a.next(int.class), a.next(int.class), a.next(int.class));
    }

    public static @NotNull String convertCoords(@NotNull String textureCoords) {
        if (textureCoords.contains("h")) {
            StringTokenizer stringTokenizer = new StringTokenizer(textureCoords, "h");
            String firstToken = stringTokenizer.nextToken().trim();
            int count = parseInt(stringTokenizer.nextToken().trim());
            int[] textureCoordsInts = get4Values(firstToken);
            StringBuilder stringBuilder = new StringBuilder();
            int x = textureCoordsInts[0];
            int y = textureCoordsInts[1];
            int width = textureCoordsInts[2];
            int height = textureCoordsInts[3];

            int currentX = 0;
            for (int i = 0; i < count; i++) {
                stringBuilder.append(x + currentX).append(',');
                stringBuilder.append(y).append(',');
                stringBuilder.append(width).append(',');
                stringBuilder.append(height);

                if (i != count - 1) {
                    stringBuilder.append(';');
                }

                currentX += width;
            }

            textureCoords = stringBuilder.toString();
        } else if (textureCoords.contains("v")) {
            StringTokenizer stringTokenizer = new StringTokenizer(textureCoords, "v");
            String firstToken = stringTokenizer.nextToken().trim();
            int count = parseInt(stringTokenizer.nextToken().trim());
            int[] textureCoordsInts = get4Values(firstToken);
            StringBuilder stringBuilder = new StringBuilder();
            int x = textureCoordsInts[0];
            int y = textureCoordsInts[1];
            int width = textureCoordsInts[2];
            int height = textureCoordsInts[3];

            int currentY = 0;
            for (int i = 0; i < count; i++) {
                stringBuilder.append(x).append(',');
                stringBuilder.append(y + currentY).append(',');
                stringBuilder.append(width).append(',');
                stringBuilder.append(height);

                if (i != count - 1) {
                    stringBuilder.append(';');
                }

                currentY += height;
            }

            textureCoords = stringBuilder.toString();
        }

        return textureCoords;
    }

    /**
     * 16,16,48,48; 32,16,48,48; 48,16,48,48
     * or
     * 16,16,48,48h3
     */
    public Texture[] createTextures(@NotNull String textureCoords) {
        textureCoords = convertCoords(textureCoords);
        if (textureCoords.endsWith(";")) {
            textureCoords = textureCoords.substring(0, textureCoords.length() - 2);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(textureCoords, ";");
        Texture[] textures = new Texture[stringTokenizer.countTokens()];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = createTexture(stringTokenizer.nextToken().trim());
        }

        return textures;
    }

    public Texture[] createTexturesHor(int x, int y, int w, int h, int count) {
        Texture[] result = new Texture[count];

        for (int i = 0; i < result.length; i++) {
            result[i] = new Texture(this, x + (i * w), y, w, h);
        }

        return result;
    }

    public Texture[] createTexturesVert(int x, int y, int w, int h, int count) {
        Texture[] result = new Texture[count];

        for (int i = 0; i < result.length; i++) {
            result[i] = new Texture(this, x, y + (i * h), w, h);
        }

        return result;
    }

    @Contract("_ -> new")
    private static int @NotNull [] get4Values(@NotNull String coords) {
        String[] split = coords.split(",");
        return new int[]{
            parseInt(split[0]),
            parseInt(split[1]),
            parseInt(split[2]),
            parseInt(split[3]),
        };
    }

    public int getId() {
        return id;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    void setDisposed(boolean disposed) {
        this.disposed = disposed;
    }

    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public String toString() {
        return "TextureAtlas{" +
            "id=" + id +
            ", width=" + width +
            ", height=" + height +
            ", disposed=" + disposed +
            '}';
    }

}






























