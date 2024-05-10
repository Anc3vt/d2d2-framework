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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;


public class SampledSound implements Sound {
    private final ByteArrayOutputStream byteArrayOutputStream;
    private boolean playing;
    private float volume = 1f;
    private float pan = 0f;

    public SampledSound(InputStream inputStream) {
        byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public SampledSound(String path) {
        byteArrayOutputStream = new ByteArrayOutputStream();
        try (FileInputStream is = new FileInputStream(path)) {
            byteArrayOutputStream.write(is.readAllBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void setVolume(float volume) {
        if (volume < -80f) volume = -80f;
        if (volume >= 6f) volume = 6f;
        this.volume = volume;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public void setPan(float pan) {
        if (pan < -1f) {
            pan = -1f;
        } else if (pan > 1f) {
            pan = 1f;
        }
        this.pan = pan;
    }

    @Override
    public float getPan() {
        return pan;
    }

    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void stop() {
        playing = false;
    }

    @Override
    public void play() {
        if (!SoundSystem.isEnabled() || volume < -15f) return;

        if (isPlaying()) stop();

        playing = true;

        try (final AudioInputStream in = AudioSystem.getAudioInputStream(getInputStream())) {
            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

            try (final SourceDataLine line =
                     (SourceDataLine) AudioSystem.getLine(info)) {

                if (line != null) {
                    line.open(outFormat);
                    line.start();
                    stream(AudioSystem.getAudioInputStream(outFormat, in), line);
                    line.drain();
                    line.stop();
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void asyncPlay() {
        CompletableFuture.runAsync(this::play);
    }

    private InputStream getInputStream() {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line) throws IOException {
        final byte[] buffer = new byte[4096];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            if (line.isControlSupported(FloatControl.Type.PAN)) {
                FloatControl floatControlPan = (FloatControl) line.getControl(FloatControl.Type.PAN);
                floatControlPan.setValue(pan);
            }
            FloatControl floatControlVolume = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            floatControlVolume.setValue(volume);
            line.write(buffer, 0, n);
            if (!playing) break;
        }
    }

}
