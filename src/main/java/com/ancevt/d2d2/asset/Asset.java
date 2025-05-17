package com.ancevt.d2d2.asset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;

@RequiredArgsConstructor
public class Asset implements AutoCloseable {

    @Getter
    private final InputStream inputStream;

    /**
     * Reads the entire content as a byte array.
     */
    public byte[] readAllBytes() {
        try {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read bytes from asset", e);
        }
    }

    /**
     * Reads the content as a single string (UTF-8).
     */
    public String readAsString() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read asset as a string", e);
        }
    }

    /**
     * Checks if the asset stream is available.
     */
    public boolean isAvailable() {
        return inputStream != null;
    }

    /**
     * Attempts to get the size of the asset in bytes.
     */
    public long size() {
        try {
            if (inputStream.available() > 0) {
                return inputStream.available();
            }
        } catch (IOException e) {
            return -1;
        }
        return -1;
    }

    /**
     * Closes the asset stream.
     */
    @Override
    public void close() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to close asset", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{" + "available=" + isAvailable() + ", size=" + size() + " bytes}";
    }
}
