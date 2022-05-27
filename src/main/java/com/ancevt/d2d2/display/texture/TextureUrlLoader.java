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
/*
 *     Copyright 2015-2022 Ancevt (me@ancevt.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "LICENSE");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.display.texture;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.EventDispatcher;
import com.ancevt.d2d2.event.TextureUrlLoaderEvent;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TextureUrlLoader extends EventDispatcher {

    private String url;
    private TextureAtlas lastLoadedTextureAtlas;

    public TextureUrlLoader(String url) {
        this.url = url;
    }

    public TextureUrlLoader() {
    }

    public void load() {
        if (url == null) throw new NullPointerException();
        dispatchEvent(TextureUrlLoaderEvent.builder()
                .type(TextureUrlLoaderEvent.TEXTURE_LOAD_START)
                .build());
        loadBytes(url);
    }

    public void load(String url) {
        setUrl(url);
        load();
    }

    private void loadBytes(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUrl()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .thenApply(this::createTexture);
    }

    private byte[] createTexture(byte[] bytes) {
        this.lastLoadedTextureAtlas = D2D2.getTextureManager().loadTextureAtlas(new ByteArrayInputStream(bytes));
        dispatchEvent(TextureUrlLoaderEvent.builder()
                .type(TextureUrlLoaderEvent.TEXTURE_LOAD_COMPLETE)
                .bytes(bytes)
                .textureAtlas(lastLoadedTextureAtlas)
                .build());
        return bytes;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public TextureAtlas getLastLoadedTextureAtlas() {
        return lastLoadedTextureAtlas;
    }


}
