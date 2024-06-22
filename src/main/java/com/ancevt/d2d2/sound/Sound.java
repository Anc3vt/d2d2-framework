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
package com.ancevt.d2d2.sound;

import com.ancevt.d2d2.asset.Assets;

import java.io.InputStream;

public interface Sound {

    void play();

    void asyncPlay();

    void stop();

    void setVolume(float volume);

    float getVolume();

    void setPan(float pan);

    float getPan();

    static Sound loadSound(InputStream inputStream) {
        return new SampledSound(inputStream);
    }


    static Sound lookupSound(String path) {
        Sound sound = SoundCache.sounds.get(path);
        if (sound == null) {
            sound = new SampledSound(path);
            SoundCache.sounds.put(path, sound);
        }
        return sound;
    }

    static Sound lookupSoundAsset(String path) {
        Sound sound = SoundCache.sounds.get(':' + path);
        if (sound == null) {
            sound = new SampledSound(Assets.getAsset(path));
            SoundCache.sounds.put(':' + path, sound);
        }
        return sound;
    }

    static void clearCache() {
        SoundCache.sounds.clear();
    }

}
