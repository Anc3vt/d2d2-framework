package com.ancevt.d2d2.util;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ancevt.d2d2.D2D2.log;

public class InputStreamFork implements Closeable {

    @Getter
    private final List<InputStream> inputStreams = Collections.synchronizedList(new ArrayList<>());

    public int size() {
        return inputStreams.size();
    }

    public InputStream left() {
        if (inputStreams.isEmpty()) {
            throw new IllegalStateException("No input streams available in the fork.");
        }
        return inputStreams.get(0);
    }

    public InputStream right() {
        if (inputStreams.size() > 1) {
            return inputStreams.get(1);
        }
        throw new IllegalStateException("Right input stream is not available in the fork.");
    }

    public static InputStreamFork fork(InputStream inputStream) {
        return fork(inputStream, 2);
    }

    public static InputStreamFork fork(InputStream inputStream, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Fork count must be at least 1.");
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            byte[] data = baos.toByteArray();
            InputStreamFork result = new InputStreamFork();

            for (int i = 0; i < count; i++) {
                result.inputStreams.add(new ByteArrayInputStream(data));
            }

            return result;

        } catch (IOException e) {
            log.error(InputStreamFork.class, "Failed to fork input stream", e);
            throw new UncheckedIOException("Error during input stream forking", e);
        }
    }

    @Override
    public void close() {
        for (InputStream is : inputStreams) {
            try {
                is.close();
            } catch (IOException e) {
                log.error(getClass(), "Failed to close an input stream: {}");
            }
        }
    }
}
