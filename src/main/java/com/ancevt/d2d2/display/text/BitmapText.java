/**
 * Copyright (C) 2023 the original author or authors.
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
package com.ancevt.d2d2.display.text;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.debug.StarletSpace;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.DisplayObject;
import com.ancevt.d2d2.display.IColored;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.event.Event;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class BitmapText extends DisplayObject implements IColored {

    protected static final String DEFAULT_TEXT = "";

    protected static final float DEFAULT_BOUND_WIDTH = 512f;
    protected static final float DEFAULT_BOUND_HEIGHT = 512f;

    protected static final Color DEFAULT_COLOR = Color.WHITE;

    private String text;
    private Color color;

    private BitmapFont bitmapFont;

    private float lineSpacing;
    private float spacing;

    private float width;
    private float height;

    private double textureBleedingFix = 0.0;
    private double vertexBleedingFix = 0.0;
    private boolean multicolorEnabled;
    private ColorTextData colorTextData;
    private boolean autosize;

    private boolean cacheAsSprite;
    private Sprite sprite;

    public BitmapText(final BitmapFont bitmapFont, float width, float height, String text) {
        setBitmapFont(bitmapFont);
        setColor(DEFAULT_COLOR);
        setWidth(width);
        setHeight(height);
        setText(text);
        setName("_" + getClass().getSimpleName() + displayObjectId());
    }

    public BitmapText(final BitmapFont bitmapFont, float boundWidth, float boundHeight) {
        this(bitmapFont, boundWidth, boundHeight, DEFAULT_TEXT);
    }

    public BitmapText(String text) {
        this(BitmapFontManager.getInstance().getDefaultBitmapFont(), DEFAULT_BOUND_WIDTH, DEFAULT_BOUND_HEIGHT, text);
    }

    public BitmapText(final BitmapFont bitmapFont) {
        this(bitmapFont, DEFAULT_BOUND_WIDTH, DEFAULT_BOUND_HEIGHT, DEFAULT_TEXT);
    }

    public BitmapText(float boundWidth, float boundHeight) {
        this(BitmapFontManager.getInstance().getDefaultBitmapFont(), boundWidth, boundHeight, DEFAULT_TEXT);
    }

    public BitmapText() {
        this(BitmapFontManager.getInstance().getDefaultBitmapFont(), DEFAULT_BOUND_WIDTH, DEFAULT_BOUND_HEIGHT, DEFAULT_TEXT);
    }

    public BitmapText cloneBitmapText() {
        BitmapText bitmapText = new BitmapText();
        bitmapText.setXY(getX(), getY());
        bitmapText.setText(getText());
        bitmapText.setSize(getWidth(), getHeight());
        bitmapText.setAlpha(getAlpha());
        bitmapText.setMulticolorEnabled(isMulticolorEnabled());
        bitmapText.setBitmapFont(getBitmapFont());
        bitmapText.setVertexBleedingFix(getVertexBleedingFix());
        bitmapText.setTextureBleedingFix(getTextureBleedingFix());
        bitmapText.setColor(getColor());
        bitmapText.setName(bitmapText.getName() + "_copy_" + getName());
        bitmapText.setSpacing(getSpacing());
        bitmapText.setLineSpacing(getLineSpacing());
        bitmapText.setRotation(getRotation());
        bitmapText.setVisible(isVisible());
        bitmapText.setAutosize(isAutosize());
        bitmapText.setCacheAsSprite(isCacheAsSprite());
        bitmapText.setScale(getScaleX(), getScaleY());
        return bitmapText;
    }

    public void setCacheAsSprite(boolean cacheAsSprite) {
        if (cacheAsSprite == this.cacheAsSprite) return;
        this.cacheAsSprite = cacheAsSprite;

        updateCachedSprite();
    }

    public boolean isCacheAsSprite() {
        return cacheAsSprite;
    }

    public Sprite cachedSprite() {
        return sprite;
    }

    public void disposeOnRemoveFromStage() {
        addEventListener(BitmapText.class, Event.REMOVE_FROM_STAGE, event -> {
            removeEventListener(BitmapText.class, Event.REMOVE_FROM_STAGE);
            if (sprite != null && sprite.getTexture() != null) {
                D2D2.getTextureManager().unloadTextureAtlas(sprite.getTexture().getTextureAtlas());
            }
        });
    }

    private void updateCachedSprite() {
        if (sprite != null && sprite.getTexture() != null) {
            D2D2.getTextureManager().unloadTextureAtlas(sprite.getTexture().getTextureAtlas());
        }

        if (isCacheAsSprite()) sprite = toSprite();
    }

    public void setTextureBleedingFix(double textureBleedingFix) {
        this.textureBleedingFix = textureBleedingFix;
    }

    public double getTextureBleedingFix() {
        return textureBleedingFix;
    }

    public void setVertexBleedingFix(double vertexBleedingFix) {
        this.vertexBleedingFix = vertexBleedingFix;
    }

    public double getVertexBleedingFix() {
        return vertexBleedingFix;
    }

    public void setAutosize(boolean autosize) {
        this.autosize = autosize;
        if (autosize) {
            setSize(getTextWidth(), getTextHeight());
        }
        updateCachedSprite();
    }

    public boolean isAutosize() {
        return autosize;
    }

    public float computeTextWidth(String text) {
        return bitmapFont.computeTextWidth(text, getSpacing());
    }

    public float getTextWidth() {
        if (isEmpty()) return 0;

        final char[] chars = getPlainText().toCharArray();
        int result = 0;

        final BitmapFont font = getBitmapFont();

        int max = 0;

        for (final char c : chars) {
            if (c == '\n' || (getWidth() > 0 && result > getWidth())) result = 0;

            BitmapCharInfo info = font.getCharInfo(c);
            if (info == null) continue;

            result += info.width() + getSpacing();

            if (result > max) max = result;
        }

        return (int) (max - getSpacing() + getBitmapFont().getZeroCharWidth() * 2);
    }

    public float getTextHeight() {
        if (getText() == null) return 0;

        final char[] chars = getPlainText().toCharArray();
        int result = 0;

        final BitmapFont font = getBitmapFont();

        for (final char c : chars) {
            if (c == '\n' || (getWidth() > 0 && result > getWidth())) {
                result += font.getZeroCharHeight() + getLineSpacing();
            }
        }

        return result + font.getZeroCharHeight();
    }

    public Sprite toSprite() {
        Sprite result = new Sprite(D2D2.getTextureManager().bitmapTextToTextureAtlas(this).createTexture());
        result.setXY(getX(), getY());
        result.setScale(getScaleX(), getScaleY());
        result.setRotation(getRotation());
        result.setColor(getColor());
        return result;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        if (multicolorEnabled) {
            colorTextData = new ColorTextData(getText(), color);
        }
        updateCachedSprite();
    }

    @Override
    public void setColor(int rgb) {
        setColor(new Color(rgb));
    }

    @Override
    public Color getColor() {
        return color;
    }

    public void setText(String text) {
        this.text = text;
        if (multicolorEnabled) {
            colorTextData = new ColorTextData(getText(), getColor());
        }
        if (autosize) {
            setSize(getTextWidth(), getTextHeight());
        }

        updateCachedSprite();
    }

    public String getPlainText() {
        if (!multicolorEnabled) return text;

        return getColorTextData().getPlainText();
    }

    public String getText() {
        return text;
    }

    public boolean isEmpty() {
        return text == null || text.length() == 0;
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public void setBitmapFont(BitmapFont bitmapFont) {
        this.bitmapFont = bitmapFont;
        updateCachedSprite();
    }

    public void setLineSpacing(float value) {
        this.lineSpacing = value;
        updateCachedSprite();
    }

    public float getLineSpacing() {
        return lineSpacing;
    }

    public void setSpacing(float value) {
        this.spacing = value;
        updateCachedSprite();
    }

    public float getSpacing() {
        return spacing;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void setWidth(float value) {
        width = value;
        updateCachedSprite();
    }

    public void setHeight(float value) {
        height = value;
        updateCachedSprite();
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        updateCachedSprite();
    }

    public void setMulticolorEnabled(boolean multicolorEnabled) {
        if (multicolorEnabled == isMulticolorEnabled()) return;
        this.multicolorEnabled = multicolorEnabled;
        if (multicolorEnabled) {
            colorTextData = new ColorTextData(getText(), getColor());
        } else {
            colorTextData = null;
        }
        updateCachedSprite();
    }

    public float getCharWidth() {
        return getBitmapFont().getCharInfo('0').width();
    }

    public float getCharHeight() {
        return getBitmapFont().getCharInfo('0').height();
    }

    public boolean isMulticolorEnabled() {
        return multicolorEnabled;
    }

    public ColorTextData getColorTextData() {
        return colorTextData;
    }

    @Override
    public void onEachFrame() {
        // For overriding
    }

    @Override
    public String toString() {
        return "BitmapText{" +
            "text='" + text + '\'' +
            ", color=" + color +
            ", bitmapFont=" + bitmapFont +
            ", lineSpacing=" + lineSpacing +
            ", spacing=" + spacing +
            ", boundWidth=" + width +
            ", boundHeight=" + height +
            '}';
    }

    public static class ColorTextData {

        private Letter[] letters;
        private String plainText;
        private final Color defaultColor;

        private ColorTextData(String text, Color defaultColor) {
            this.defaultColor = defaultColor;
            createData(text);
        }

        private void createData(String text) {
            List<Letter> letterList = new ArrayList<>();
            Color color = defaultColor;

            if (text.isEmpty()) text = " ";

            boolean firstIndexSharp = text.charAt(0) == '#';
            int firstIndexOpen = text.indexOf('<');
            int lastIndexClose = text.lastIndexOf('>');

            StringBuilder stringBuilder = new StringBuilder();

            if (firstIndexSharp && firstIndexOpen < lastIndexClose) {

                for (int i = 1; i < text.length(); i++) {
                    char c = text.charAt(i);

                    try {

                        if (c == '<') {
                            try {
                                String colorString = text.substring(i + 1, i + 8);

                                if (colorString.indexOf('>') >= 0) {
                                    colorString = colorString.substring(0, colorString.indexOf('>'));
                                } else {
                                    continue;
                                }

                                color = Color.of(parseInt(colorString, 16));

                                i += colorString.length() + 1;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            letterList.add(new Letter(c, color));
                            stringBuilder.append(c);
                        }

                    } catch (StringIndexOutOfBoundsException exception) {
                        exception.printStackTrace();
                    }

                }

            } else {
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    letterList.add(new Letter(c, defaultColor));
                    stringBuilder.append(c);
                }
            }

            letters = letterList.toArray(new Letter[0]);
            plainText = stringBuilder.toString();
        }

        public String getPlainText() {
            return plainText;
        }

        public Letter getColoredLetter(int index) {
            return letters[index];
        }

        public int length() {
            return letters.length;
        }

        public static class Letter {
            private final char character;
            private final Color color;

            public Letter(char character, Color color) {

                this.character = character;
                this.color = color;
            }

            public char getCharacter() {
                return character;
            }

            public Color getColor() {
                return color;
            }

            @Override
            public String toString() {
                return "Letter{" +
                    "character=" + character +
                    ", color=" + color +
                    '}';
            }
        }

    }

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, "(floating)"));
        StarletSpace.haveFun();

        stage.setBackgroundColor(Color.GRAY);

        String text = """
            #<0000FF>Hello <FFFF00>D2D2 <0000FF>world
            <FFFFFF>Second line
                            
            ABCDEFGHIJKLMNOPQRSTUWYXYZ
            abcdefghijklmnopqrstuvwxyz""";

        BitmapText bitmapText = new BitmapText(BitmapFontManager.getInstance().loadBitmapFont("terminus/Terminus-16-Bold"));
        bitmapText.setMulticolorEnabled(true);
        bitmapText.setText(text);
        bitmapText.setScale(1, 1);
        stage.add(bitmapText, 100, 250);

        BitmapText bitmapText1 = new BitmapText(BitmapFontManager.getInstance().loadBitmapFont("terminus/Terminus-16-Bold"));
        bitmapText1.setText("AUTOSIZE BITMAP TEXT");
        bitmapText1.setAutosize(true);
        bitmapText1.setScale(5, 5);
        stage.add(bitmapText1, 100, 450);

        FpsMeter fpsMeter = new FpsMeter();
        fpsMeter.setBitmapFont(BitmapFontManager.getInstance().loadBitmapFont("terminus/Terminus-16-Bold"));
        stage.add(fpsMeter);

        D2D2.loop();
    }


}

