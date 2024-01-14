package com.ancevt.d2d2.display.texture;

import com.ancevt.d2d2.D2D2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TextureFactory {

    private final static Map<String, Texture> textureCacheFiles = new HashMap<>();

    public static Texture getTexture(String asset, int textureX, int textureY, int textureWidth, int textureHeight) {
        return
            textureCacheFiles.computeIfAbsent(
                "%s_%d".formatted(
                    asset,
                    Objects.hash(
                        asset,
                        textureX,
                        textureY,
                        textureWidth,
                        textureHeight
                    )),
                key -> D2D2.getTextureManager()
                    .loadTextureAtlas(asset)
                    .createTexture(textureX, textureY, textureWidth, textureHeight)
            );
    }

    public static void clearCache() {
        textureCacheFiles.clear();
    }
}
