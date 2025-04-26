/**
 * Copyright (C) 2025 the original author or authors.
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

package com.ancevt.d2d2.scene.shape;

import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Container;
import com.ancevt.d2d2.scene.ContainerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ComplexShapeParser {

    public static Container parseAsset(String assetPath) {
        return parse(Assets.getAsset(assetPath));
    }

    public static Container parse(InputStream inputStream) {
        try {
            return parse(convertInputStreamToString(inputStream));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Container parse(String string) {
        Container result = new ContainerImpl();

        AtomicReference<FreeShape> shape = new AtomicReference<>();

        string.lines().forEach(line -> {

            if (line.startsWith("#")) {
                if (shape.get() != null) {
                    result.addChild(shape.get());
                }

                line = line.substring(1);

                Color color = Color.of(line);

                FreeShape s = new FreeShape();
                s.setColor(color);
                shape.set(s);
            } else if (line.contains(" ")) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
                float x = Float.parseFloat(stringTokenizer.nextToken());
                float y = Float.parseFloat(stringTokenizer.nextToken());
                shape.get().addVertex(x, y);
            }
        });

        result.addChild(shape.get());

        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
