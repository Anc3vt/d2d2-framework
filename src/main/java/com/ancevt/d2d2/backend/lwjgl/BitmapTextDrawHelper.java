package com.ancevt.d2d2.backend.lwjgl;

import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.text.BitmapCharInfo;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import org.jetbrains.annotations.NotNull;

public class BitmapTextDrawHelper {

    public static void draw(BitmapText bitmapText,
                            float alpha,
                            float scaleX,
                            float scaleY,
                            DrawCharFunction drawCharFunction,
                            ApplyColorFunction applyColorFunction) {

        BitmapFont bitmapFont = bitmapText.getBitmapFont();
        TextureAtlas textureAtlas = bitmapFont.getTextureAtlas();

        int textureWidth = textureAtlas.getWidth();
        int textureHeight = textureAtlas.getHeight();

        float lineSpacing = bitmapText.getLineSpacing();
        float spacing = bitmapText.getSpacing();

        float boundWidth = bitmapText.getWidth() * scaleX + bitmapFont.getCharInfo('0').width() * 3;
        float boundHeight = bitmapText.getHeight() * scaleY;

        float drawX = 0;
        float drawY = bitmapFont.getPaddingTop() * scaleY;

        double textureBleedingFix = bitmapText.getTextureBleedingFix();
        double vertexBleedingFix = bitmapText.getVertexBleedingFix();

        String text = bitmapText.getText();

        if (bitmapText.isMulticolorEnabled()) {

            BitmapText.ColorTextData colorTextData = bitmapText.getColorTextData();

            for (int i = 0; i < colorTextData.length(); i++) {
                BitmapText.ColorTextData.Letter letter = colorTextData.getColoredLetter(i);

                char c = letter.getCharacter();

                BitmapCharInfo charInfo = bitmapFont.getCharInfo(c);

                if (charInfo == null) continue;

                Color letterColor = letter.getColor();

                if (applyColorFunction != null) {
                    applyColorFunction.applyColor(
                        letterColor.getR() / 255f,
                        letterColor.getG() / 255f,
                        letterColor.getB() / 255f,
                        alpha
                    );
                }

                float charWidth = charInfo.width();
                float charHeight = charInfo.height();

                if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth * 5)) {
                    drawX = 0;
                    drawY += (charHeight + lineSpacing) * scaleY;

                    if (boundHeight != 0 && drawY > boundHeight - charHeight) {
                        break;
                    }
                }

                drawCharFunction.drawChar(
                    textureAtlas,
                    c,
                    letter,
                    drawX,
                    (drawY + scaleY * charHeight),
                    textureWidth,
                    textureHeight,
                    charInfo,
                    scaleX,
                    scaleY,
                    textureBleedingFix,
                    vertexBleedingFix
                );

                drawX += (charWidth + (c != '\n' ? spacing : 0)) * scaleX;
            }

        } else {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);

                BitmapCharInfo charInfo = bitmapFont.getCharInfo(c);

                if (charInfo == null) {
                    continue;
                }

                float charWidth = charInfo.width();
                float charHeight = charInfo.height();

                if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth * 5)) {
                    drawX = 0;
                    drawY += (charHeight + lineSpacing) * scaleY;

                    if (boundHeight != 0 && drawY > boundHeight - charHeight) {
                        break;
                    }
                }

                drawCharFunction.drawChar(
                    textureAtlas,
                    c,
                    null,
                    drawX,
                    (drawY + scaleY * charHeight),
                    textureWidth,
                    textureHeight,
                    charInfo,
                    scaleX,
                    scaleY,
                    textureBleedingFix,
                    vertexBleedingFix
                );

                drawX += (charWidth + (c != '\n' ? spacing : 0)) * scaleX;
            }
        }

    }


    @FunctionalInterface
    public interface DrawCharFunction {

        void drawChar(
            TextureAtlas atlas,
            char c,
            BitmapText.ColorTextData.Letter letter,
            float x,
            float y,
            int textureAtlasWidth,
            int textureAtlasHeight,
            @NotNull BitmapCharInfo charInfo,
            float scX,
            float scY,
            double textureBleedingFix,
            double vertexBleedingFix);
    }

    @FunctionalInterface
    public interface ApplyColorFunction {
        void applyColor(float r, float g, float b, float alpha);
    }
}
