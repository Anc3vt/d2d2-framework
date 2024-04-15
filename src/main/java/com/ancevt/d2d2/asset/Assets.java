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
package com.ancevt.d2d2.asset;

import com.ancevt.d2d2.exception.AssetException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public class Assets {

    private static final String ASSETS_DIR = "assets/";

    private Assets() {
    }

    public static InputStream getAssetAsStream(String assetPath) {
        final ClassLoader classLoader = Assets.class.getClassLoader();

        InputStream result = classLoader.getResourceAsStream(ASSETS_DIR + assetPath);

        if (result == null) throw new AssetException("resource " + assetPath + " not found");

        return result;
    }

    public static BufferedReader getAssetAsBufferedReader(InputStream inputStream) {
        return getAssetAsBufferedReader(inputStream, StandardCharsets.UTF_8.name());
    }

    public static BufferedReader getAssetAsBufferedReader(InputStream inputStream, String charsetName) {
        try {
            return new BufferedReader(new InputStreamReader(inputStream, charsetName));
        } catch (UnsupportedCharsetException | IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static BufferedReader getAssetAsBufferedReader(String assetPath) {
        return getAssetAsBufferedReader(getAssetAsStream(assetPath), StandardCharsets.UTF_8.name());
    }

    public static BufferedReader getAssetAsBufferedReader(String assetPath, String charsetName) {
        return getAssetAsBufferedReader(getAssetAsStream(assetPath), charsetName);
    }

}
