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

package com.ancevt.d2d2.scene.texture;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.common.Disposable;
import com.ancevt.d2d2.util.Args;

import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

public class Texture implements Disposable {

    private final int id;
    private final int width;
    private final int height;

    private Texture(int id) {
        this(id, 0, 0);
    }

    public Texture(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public TextureRegion createTextureRegion() {
        return createTextureRegion(0, 0, getWidth(), getHeight());
    }

    public TextureRegion createTextureRegion(int x, int y, int width, int height) {
        return new TextureRegion(this, x, y, width, height);
    }

    /**
     * 16,16,48,48
     *
     * @param coords
     * @return
     */
    public TextureRegion createTextureRegion(String coords) {
        if (coords.contains(";")) {
            coords = coords.split(";")[0];
        }
        var a = Args.of(coords, ',');

        int x = a.get(int.class, 0);
        int y = a.get(int.class, 1);
        int w = a.get(int.class, 2);
        String hString = a.get(String.class, 3);

        if (hString.contains("h")) {
            hString = hString.substring(0, hString.indexOf('h'));
        }

        if (hString.contains("v")) {
            hString = hString.substring(0, hString.indexOf('v'));
        }

        int h = Integer.parseInt(hString);

        return new TextureRegion(this, x, y, w, h);
    }

    private static String convertCoords(String coords) {
        if (coords.contains("h")) {
            StringTokenizer stringTokenizer = new StringTokenizer(coords, "h");
            String firstToken = stringTokenizer.nextToken().trim();
            int count = parseInt(stringTokenizer.nextToken().trim());
            int[] coordsInts = get4Values(firstToken);
            StringBuilder stringBuilder = new StringBuilder();
            int x = coordsInts[0];
            int y = coordsInts[1];
            int width = coordsInts[2];
            int height = coordsInts[3];

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

            coords = stringBuilder.toString();
        } else if (coords.contains("v")) {
            StringTokenizer stringTokenizer = new StringTokenizer(coords, "v");
            String firstToken = stringTokenizer.nextToken().trim();
            int count = parseInt(stringTokenizer.nextToken().trim());
            int[] coordsInts = get4Values(firstToken);
            StringBuilder stringBuilder = new StringBuilder();
            int x = coordsInts[0];
            int y = coordsInts[1];
            int width = coordsInts[2];
            int height = coordsInts[3];

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

            coords = stringBuilder.toString();
        }

        return coords;
    }

    /**
     * 16,16,48,48; 32,16,48,48; 48,16,48,48
     * or
     * 16,16,48,48h3
     */
    public TextureRegion[] createTextureRegions(String coords) {
        coords = convertCoords(coords);
        if (coords.endsWith(";")) {
            coords = coords.substring(0, coords.length() - 2);
        }
        StringTokenizer stringTokenizer = new StringTokenizer(coords, ";");
        TextureRegion[] textureRegions = new TextureRegion[stringTokenizer.countTokens()];
        for (int i = 0; i < textureRegions.length; i++) {
            textureRegions[i] = createTextureRegion(stringTokenizer.nextToken().trim());
        }

        return textureRegions;
    }

    public TextureRegion[] createTextureRegionsHor(int x, int y, int w, int h, int count) {
        TextureRegion[] result = new TextureRegion[count];

        for (int i = 0; i < result.length; i++) {
            result[i] = new TextureRegion(this, x + (i * w), y, w, h);
        }

        return result;
    }

    public TextureRegion[] createTextureRegionsVert(int x, int y, int w, int h, int count) {
        TextureRegion[] result = new TextureRegion[count];

        for (int i = 0; i < result.length; i++) {
            result[i] = new TextureRegion(this, x, y + (i * h), w, h);
        }

        return result;
    }

    private static int[] get4Values(String coords) {
        String[] split = coords.split(",");
        return new int[]{parseInt(split[0]), parseInt(split[1]), parseInt(split[2]), parseInt(split[3]),};
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

    @Override
    public void dispose() {
        D2D2.textureManager().unloadTexture(this);
    }

    public boolean isDisposed() {
        return !D2D2.textureManager().containsTexture(this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "id=" + id + ", width=" + width + ", height=" + height + ", disposed=" + isDisposed() + '}';
    }

}






























