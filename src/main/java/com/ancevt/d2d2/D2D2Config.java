package com.ancevt.d2d2;

import com.ancevt.d2d2.asset.Assets;
import com.ancevt.d2d2.engine.Engine;
import com.ancevt.d2d2.util.Args;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class D2D2Config {

    public static final String ENGINE = "d2d2.engine";
    public static final String TITLE = "d2d2.title";
    public static final String WIDTH = "d2d2.width";
    public static final String HEIGHT = "d2d2.height";
    public static final String NO_SCALE_MODE = "d2d2.noscalemode";
    public static final String FULLSCREEN = "d2d2.fullscreen";
    public static final String ALWAYS_ON_TOP = "d2d2.alwaysontop";
    public static final String BITMAPFONT_SAVEBMF = "d2d2.bitmapfont.savebmf";

    private final Map<String, String> properties = new HashMap<>();

    private Args args;

    private String[] arguments;

    public Args getArgs() {
        if (args == null) {
            if (arguments == null) {
                throw new IllegalStateException("No arguments set");
            }
            args = Args.of(arguments);
        }
        return args;
    }

    public Map<String, String> asMap() {
        return new HashMap<>(properties);
    }

    public D2D2Config prop(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public D2D2Config engine(String engineClassName) {
        return prop(ENGINE, engineClassName);
    }

    public D2D2Config engine(Class<? extends Engine> engineClass) {
        return engine(engineClass.getName());
    }

    public D2D2Config width(int width) {
        return prop(WIDTH, String.valueOf(width));
    }

    public D2D2Config height(int height) {
        return prop(HEIGHT, String.valueOf(height));
    }

    public D2D2Config size(int width, int height) {
        return width(width).height(height);
    }

    public D2D2Config title(String title) {
        return prop(TITLE, title);
    }

    public D2D2Config noScaleMode(boolean noScaleMode) {
        return prop(NO_SCALE_MODE, String.valueOf(noScaleMode));
    }

    /*
    public D2D2Config fullscreen(boolean fullscreen) {
        return prop(FULLSCREEN, String.valueOf(fullscreen));
    }

     */

    public D2D2Config alwaysOnTop(boolean onTop) {
        return prop(ALWAYS_ON_TOP, String.valueOf(onTop));
    }

    public D2D2Config fromAssets(String propertiesFilename) {
        InputStream inputStream = Assets.getAsset(propertiesFilename).getInputStream();
        try {
            String propertiesString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            propertiesString.lines().forEach(line -> {
                String[] split = line.split("=");
                if (split.length != 2) {
                    return;
                }
                prop(split[0].trim(), split[1].trim());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public D2D2Config fromAssets() {
        return fromAssets("d2d2.properties");
    }

    public D2D2Config fromArgs(String[] args) {
        this.arguments = args;
        for (String arg : args) {
            String[] split;
            if (arg.startsWith("-D") && arg.contains("=")) {
                split = arg.substring(2).split("=", 2);
            } else if (arg.startsWith("--") && arg.contains("=")) {
                split = arg.substring(2).split("=", 2);
            } else {
                continue;
            }
            prop(split[0], split[1]);
        }
        return this;
    }

    public String getOrDefault(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }

    public boolean getOrDefault(String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getOrDefault(key, String.valueOf(defaultValue)));
    }

    public int getOrDefault(String key, int defaultValue) {
        return Integer.parseInt(properties.getOrDefault(key, String.valueOf(defaultValue)));
    }

}
