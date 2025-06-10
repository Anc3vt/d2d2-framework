package com.ancevt.d2d2.asset;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.exception.AssetException;
import com.ancevt.d2d2.scene.text.BitmapFont;
import com.ancevt.d2d2.scene.texture.Texture;
import com.ancevt.d2d2.sound.Sound;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;

@RequiredArgsConstructor
public class Asset implements AutoCloseable {

    private static final String ASSETS_DIR = "assets/";
    @Getter
    private final InputStream inputStream;

    public static Texture loadTexture(String assetPath) {
        return D2D2.getTextureManager().loadTexture(assetPath);
    }

    public static Texture loadTexture(InputStream inputStream) {
        return D2D2.getTextureManager().loadTexture(inputStream);
    }

    public static BitmapFont loadBitmapFont(String pngAssetPath, String bmfAssetPath) {
        return D2D2.getBitmapFontManager().loadBitmapFont(pngAssetPath, bmfAssetPath);
    }

    public static BitmapFont loadBitmapFont(InputStream pngInputStream, InputStream bmfInputStream, String name) {
        return D2D2.getBitmapFontManager().loadBitmapFont(pngInputStream, bmfInputStream, name);
    }

    public static Sound loadSound(String assetPath) {
        return D2D2.getSoundManager().loadSound(assetPath);
    }

    public static Sound loadSound(InputStream inputStream) {
        return D2D2.getSoundManager().loadSound(inputStream);
    }

    public static Asset getAsset(String assetPath) {
        final ClassLoader classLoader = Asset.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(Asset.ASSETS_DIR + assetPath);
        if (inputStream == null) throw new AssetException("resource " + assetPath + " not found");
        return new Asset(inputStream);
    }

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
