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
package com.ancevt.d2d2.media;

import com.ancevt.commons.concurrent.Lock;
import com.ancevt.d2d2.asset.Assets;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface Media {

    Map<String, Media> medias = new HashMap<>();

    void play();
    void stop();

    void setVolume(float vaolume);
    float getVolume();
    void setPan(float pan);
    float getPan();

    static @NotNull Media lookupSound(String path) {
        Media media = medias.get(path);
        if (media == null) {
            media = new BlockingSound(path);
            medias.put(path, media);
        }
        return media;
    }

    static @NotNull Media lookupSoundAsset(String path) {
        Media media = medias.get(path);
        if (media == null) {
            media = new BlockingSound(Assets.getAssetAsStream(path));
            medias.put(':' + path, media);
        }
        return media;
    }

    static void main(String[] args) {
        while (true) {
            Media media = Media.lookupSound("/home/ancevt/workspace/ancevt/d2d2/d2d2-world-arena-server/data/mapkits/builtin-mapkit/character-damage.ogg");
            media.play();
            new Lock().lock(250, TimeUnit.MILLISECONDS);
        }
    }
}
