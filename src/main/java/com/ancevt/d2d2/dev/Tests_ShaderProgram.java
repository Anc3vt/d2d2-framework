/**
 * Copyright (C) 2022 the original author or authors.
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

package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.ShaderProgram;
import com.ancevt.d2d2.display.Sprite;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public class Tests_ShaderProgram {


    @SneakyThrows
    public static void main(String[] args) {
        Root root = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        root.add(new FpsMeter());

        Sprite sprite = new Sprite("satellite");
        sprite.setScale(5f, 5f);

        String vertexShader = Files.readString(Path.of("shader0.vert"));
        String fragmentShader = Files.readString(Path.of("shader0.frag"));

        sprite.setShaderProgram(new ShaderProgram(vertexShader, fragmentShader));

        root.add(sprite, 0, 0);
        root.setBackgroundColor(Color.DARK_BLUE);

        root.add(new Sprite("satellite"), 100, 100);

        D2D2.loop();
    }
}
