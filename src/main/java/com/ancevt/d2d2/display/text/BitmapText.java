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
package com.ancevt.d2d2.display.text;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.DisplayObject;
import com.ancevt.d2d2.display.IColored;
import com.ancevt.d2d2.display.IResizable;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.event.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class BitmapText extends DisplayObject implements IColored, IResizable {

    protected static final String DEFAULT_TEXT = "";

    protected static final float DEFAULT_WIDTH = 256f;
    protected static final float DEFAULT_HEIGHT = 128f;
    protected static final Color DEFAULT_COLOR = Color.WHITE;

    private String text;
    private Color color;

    private BitmapFont bitmapFont;

    private float lineSpacing;
    private float spacing;

    private float width;
    private float height;

    @Getter
    private float maxWidth = 1024f;

    @Getter
    private float maxHeight = 512f;

    @Getter
    @Setter
    private double textureBleedingFix = 0.0;
    @Getter
    @Setter
    private double vertexBleedingFix = 0.0;
    @Getter
    private boolean multicolor;
    @Getter
    private ColorTextData colorTextData;
    @Getter
    private boolean autosize = true;
    private boolean cacheAsSprite;

    @Getter
    private boolean multiline = true;

    @Getter
    private boolean wordWrap = true;
    private Sprite sprite;

    public BitmapText(final BitmapFont bitmapFont, float width, float height, String text) {
        setBitmapFont(bitmapFont);
        setColor(DEFAULT_COLOR);
        setWidth(width);
        setHeight(height);
        setText(text);
        setName("_" + getClass().getSimpleName() + getDisplayObjectId());
    }

    public BitmapText(final BitmapFont bitmapFont, float boundWidth, float boundHeight) {
        this(bitmapFont, boundWidth, boundHeight, DEFAULT_TEXT);
    }

    public BitmapText(String text) {
        this(D2D2.bitmapFontManager().getDefaultBitmapFont(), DEFAULT_WIDTH, DEFAULT_HEIGHT, text);
    }

    public BitmapText(final BitmapFont bitmapFont) {
        this(bitmapFont, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TEXT);
    }

    public BitmapText(float boundWidth, float boundHeight) {
        this(D2D2.bitmapFontManager().getDefaultBitmapFont(), boundWidth, boundHeight, DEFAULT_TEXT);
    }

    public BitmapText() {
        this(D2D2.bitmapFontManager().getDefaultBitmapFont(), DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TEXT);
    }

    public BitmapText cloneBitmapText() {
        BitmapText bitmapText = new BitmapText();
        bitmapText.setXY(getX(), getY());
        bitmapText.setText(getText());
        bitmapText.setSize(getWidth(), getHeight());
        bitmapText.setAlpha(getAlpha());
        bitmapText.setMulticolor(isMulticolor());
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
        bitmapText.setMultiline(isMultiline());
        bitmapText.setWordWrap(isWordWrap());
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
                D2D2.textureManager().unloadTextureAtlas(sprite.getTexture().getTextureAtlas());
            }
        });
    }

    private void updateCachedSprite() {
        if (sprite != null && sprite.getTexture() != null) {
            D2D2.textureManager().unloadTextureAtlas(sprite.getTexture().getTextureAtlas());
        }

        if (isCacheAsSprite()) sprite = toSprite();
    }

    public void setAutosize(boolean autosize) {
        this.autosize = autosize;
        if (autosize) {
            setSize(getTextWidth(), getTextHeight());
        }
        updateCachedSprite();
    }

    public void setMaxWidth(float value) {
        this.maxWidth = value;
        setWidth(width);
    }

    public void setMaxHeight(float value) {
        this.maxHeight = value;
        setHeight(height);
    }

    public void setMaxSize(float maxWidth, float maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        setSize(width, height);
    }

    public float computeTextWidth(String text) {
        return bitmapFont.computeTextWidth(text, getSpacing());
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
        updateCachedSprite();
    }

    public Sprite toSprite() {
        Sprite result = new Sprite(D2D2.textureManager().bitmapTextToTextureAtlas(this).createTexture());
        result.setXY(getX(), getY());
        result.setScale(getScaleX(), getScaleY());
        result.setRotation(getRotation());
        result.setColor(getColor());
        return result;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        if (multicolor) {
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
        if (multicolor) {
            colorTextData = new ColorTextData(getText(), getColor());
        }
        if (autosize) {
            setSize(getTextWidth(), getTextHeight());
        }

        updateCachedSprite();
    }

    public String getPlainText() {
        if (!multicolor) return text;

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

    public float getTextWidth() {
        if (isEmpty()) return 0;

        final char[] chars = getPlainText().toCharArray();
        float w = 0;

        final BitmapFont font = getBitmapFont();

        float max = 0;

        for (final char c : chars) {
            if (c == '\n' || (width > 0 && w > getMaxWidth())) {
                if (!isMultiline()) return w;
                w = 0;
            }

            BitmapCharInfo info = font.getCharInfo(c);
            if (info == null) continue;

            w += (int) (info.width() + getSpacing());

            if (w > max) max = w;
        }

        return max - getSpacing();
    }

    public float getTextHeight() {
        if (isEmpty()) return 0;

        final char[] chars = getPlainText().toCharArray();
        float w = 0;

        final BitmapFont font = getBitmapFont();

        float max = 0;

        float h = 0;

        for (final char c : chars) {
            if (c == '\n' || (width > 0 && w > getMaxWidth())) {
                h += (int) (font.getZeroCharHeight() + getLineSpacing());
                w = 0;
            }

            BitmapCharInfo info = font.getCharInfo(c);
            if (info == null) continue;

            w += info.width() + getSpacing();

            if (w > max) max = w;
        }

        return h + font.getZeroCharHeight();
    }

    @Override
    public float getWidth() {
        if (isAutosize()) {
            return getTextWidth();
        }

        if (width > maxWidth) return maxWidth;

        return width;
    }

    @Override
    public float getHeight() {
        if (!isMultiline()) {
            return bitmapFont.getZeroCharHeight();
        }

        if (isAutosize()) {
            return getTextHeight();
        }

        if (height > maxHeight) return maxHeight;

        return height;
    }

    @Override
    public void setWidth(float value) {
        width = value;

        if (width > maxWidth) {
            width = maxWidth;
        }

        updateCachedSprite();
    }

    @Override
    public void setHeight(float value) {
        height = value;

        if (height > maxHeight) {
            height = maxHeight;
        }

        updateCachedSprite();
    }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;

        if (this.width > maxWidth) {
            this.width = maxWidth;
        }

        if (this.height > maxHeight) {
            this.height = maxHeight;
        }

        updateCachedSprite();
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
        updateCachedSprite();
    }

    public void setMulticolor(boolean multicolor) {
        if (multicolor == isMulticolor()) return;
        this.multicolor = multicolor;
        if (multicolor) {
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

    @Override
    public void onExitFrame() {
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

            int firstIndexOpen = text.indexOf("<");
            int lastIndexClose = text.lastIndexOf('>');

            StringBuilder stringBuilder = new StringBuilder();

            if (firstIndexOpen < lastIndexClose) {

                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);

                    try {

                        if (c == '<') {
                            try {
                                int startIndex = i + 1;
                                int endIndex = i + 8;

                                String colorString = text.substring(startIndex, endIndex);

                                if (colorString.indexOf('>') >= 0) {
                                    colorString = colorString.substring(0, colorString.indexOf('>'));
                                } else {
                                    continue;
                                }
                                color = Color.of(parseInt(colorString, 16));
                                i += colorString.length() + 1;
                            } catch (Exception e) {
                                System.err.println(text);
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
}
