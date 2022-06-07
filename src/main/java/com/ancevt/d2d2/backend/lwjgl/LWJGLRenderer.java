/**
 * Copyright (C) 2022 the original author or authors.
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
package com.ancevt.d2d2.backend.lwjgl;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.debug.DebugPanel;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.IFramedDisplayObject;
import com.ancevt.d2d2.display.IRenderer;
import com.ancevt.d2d2.display.ISprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapCharInfo;
import com.ancevt.d2d2.display.text.BitmapFont;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.EventPool;
import com.ancevt.d2d2.input.Mouse;
import com.ancevt.localstorage.QuickLocalStorageFactory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

import static java.lang.Math.round;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

public class LWJGLRenderer implements IRenderer {

    private final Stage stage;
    private final LWJGLBackend lwjglBackend;
    boolean smoothMode = false;
    private LWJGLTextureEngine textureEngine;
    private int zOrderCounter;

    public LWJGLRenderer(Stage stage, LWJGLBackend lwjglStarter) {
        this.stage = stage;
        this.lwjglBackend = lwjglStarter;
    }

    @Override
    public void init(long windowId) {
        GL20.glEnable(GL_BLEND);
        GL20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
        GL20.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);

        GL20.glMatrixMode(GL20.GL_MODELVIEW);
    }

    @Override
    public void reshape(int width, int height) {
        GL20.glViewport(0, 0, width, height);
        GL20.glMatrixMode(GL20.GL_PROJECTION);
        GL20.glLoadIdentity();
        GLU.gluOrtho2D(0, width, height, 0);
        GL20.glMatrixMode(GL20.GL_MODELVIEW);
        GL20.glLoadIdentity();
    }

    @Override
    public void renderFrame() {
        textureEngine.loadTextureAtlases();

        clear();

        GL20.glLoadIdentity();

        zOrderCounter = 0;

        renderDisplayObject(stage,
                0,
                stage.getX(),
                stage.getY(),
                stage.getScaleX(),
                stage.getScaleY(),
                stage.getRotation(),
                stage.getAlpha());

        IDisplayObject cursor = D2D2.getCursor();
        if (cursor != null) {
            renderDisplayObject(cursor,
                    0,
                    cursor.getX(),
                    cursor.getY(),
                    cursor.getScaleX(),
                    cursor.getScaleY(),
                    cursor.getRotation(),
                    cursor.getAlpha());
        }

        textureEngine.unloadTextureAtlases();

        GLFW.glfwGetCursorPos(lwjglBackend.windowId, mouseX, mouseY);

        Mouse.setXY((int) mouseX[0], (int) mouseY[0]);
    }

    private final double[] mouseX = new double[1];
    private final double[] mouseY = new double[1];

    private void clear() {
        Color backgroundColor = stage.getBackgroundColor();
        float backgroundColorRed = backgroundColor.getR() / 255.0f;
        float backgroundColorGreen = backgroundColor.getG() / 255.0f;
        float backgroundColorBlue = backgroundColor.getB() / 255.0f;
        GL20.glClearColor(backgroundColorRed, backgroundColorGreen, backgroundColorBlue, 1.0f);
        GL20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private synchronized void renderDisplayObject(@NotNull IDisplayObject displayObject,
                                                  int level,
                                                  float toX,
                                                  float toY,
                                                  float toScaleX,
                                                  float toScaleY,
                                                  float toRotation,
                                                  float toAlpha) {

        if (!displayObject.isVisible()) return;

        zOrderCounter++;
        displayObject.setAbsoluteZOrderIndex(zOrderCounter);
        displayObject.onEachFrame();
        displayObject.dispatchEvent(EventPool.simpleEventSingleton(Event.EACH_FRAME, displayObject));

        float scX = displayObject.getScaleX() * toScaleX;
        float scY = displayObject.getScaleY() * toScaleY;
        float r = displayObject.getRotation() + toRotation;

        float x = toScaleX * displayObject.getX();
        float y = toScaleY * displayObject.getY();

        float a = displayObject.getAlpha() * toAlpha;

        x = round(x);
        y = round(y);

        GL20.glPushMatrix();
        GL20.glTranslatef(x, y, 0);
        GL20.glRotatef(r, 0, 0, 1);

        if (displayObject instanceof IContainer) {
            IContainer container = (IContainer) displayObject;
            for (int i = 0; i < container.getChildCount(); i++) {
                renderDisplayObject(container.getChild(i), level + 1, x + toX, y + toY, scX, scY, 0, a);
            }

        } else if (displayObject instanceof ISprite s) {
            renderSprite(s, a, scX, scY);
        } else if (displayObject instanceof BitmapText) {
            BitmapText btx = (BitmapText) displayObject;
            renderBitmapText(btx, a, scX, scY);
        }

        if (displayObject instanceof IFramedDisplayObject) {
            IFramedDisplayObject f = (IFramedDisplayObject) displayObject;
            f.processFrame();
        }

        GL20.glPopMatrix();
    }

    private void renderSprite(@NotNull ISprite sprite, float alpha, float scaleX, float scaleY) {
        Texture texture = sprite.getTexture();

        if (texture == null) return;
        if (texture.getTextureAtlas().isDisposed()) return;

        TextureAtlas textureAtlas = texture.getTextureAtlas();

        //textureParamsHandle();

        GL20.glEnable(GL_BLEND);
        GL20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        boolean bindResult = D2D2.getTextureManager().getTextureEngine().bind(textureAtlas);

        if (!bindResult) {
            return;
        }

        D2D2.getTextureManager().getTextureEngine().enable(textureAtlas);

        final Color color = sprite.getColor();

        if (color != null) {
            GL20.glColor4f(
                    color.getR() / 255f,
                    color.getG() / 255f,
                    color.getB() / 255f,
                    alpha
            );
        }

        int tX = texture.x();
        int tY = texture.y();
        int tW = texture.width();
        int tH = texture.height();

        float totalW = textureAtlas.getWidth();
        float totalH = textureAtlas.getHeight();

        float x = tX / totalW;
        float y = tY / totalH;
        float w = tW / totalW;
        float h = tH / totalH;

        float repeatX = sprite.getRepeatX();
        float repeatY = sprite.getRepeatY();

        double vertexBleedingFix = sprite.getVertexBleedingFix();
        double textureBleedingFix = sprite.getTextureBleedingFix();

        for (int rY = 0; rY < repeatY; rY++) {
            for (int rX = 0; rX < repeatX; rX++) {
                float px = round(rX * tW * scaleX);
                float py = round(rY * tH * scaleY);

                GL20.glBegin(GL20.GL_QUADS);

                // L
                GL20.glTexCoord2d(x + textureBleedingFix, (h + y) - textureBleedingFix);
                GL20.glVertex2d(px - vertexBleedingFix, py + tH * scaleY + vertexBleedingFix);

                // _|
                GL20.glTexCoord2d((w + x) - textureBleedingFix, (h + y) - textureBleedingFix);
                GL20.glVertex2d(px + tW * scaleX + vertexBleedingFix, py + tH * scaleY + vertexBleedingFix);

                // ^|
                GL20.glTexCoord2d((w + x) - textureBleedingFix, y + textureBleedingFix);
                GL20.glVertex2d(px + tW * scaleX + vertexBleedingFix, py - vertexBleedingFix);

                // Г
                GL20.glTexCoord2d(x + textureBleedingFix, y + textureBleedingFix);
                GL20.glVertex2d(px - vertexBleedingFix, py - vertexBleedingFix);

                GL20.glEnd();
            }
        }

        GL20.glDisable(GL_BLEND);
        D2D2.getTextureManager().getTextureEngine().disable(textureAtlas);
    }

    private void renderBitmapText(@NotNull BitmapText bitmapText, float alpha, float scaleX, float scaleY) {
        if (bitmapText.isEmpty()) return;

        BitmapFont bitmapFont = bitmapText.getBitmapFont();
        TextureAtlas textureAtlas = bitmapFont.getTextureAtlas();

        GL20.glEnable(GL_BLEND);
        GL20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        boolean bindResult = D2D2.getTextureManager().getTextureEngine().bind(textureAtlas);

        if (!bindResult) return;

        D2D2.getTextureManager().getTextureEngine().enable(textureAtlas);

        Color color = bitmapText.getColor();

        GL20.glColor4f(
                (float) color.getR() / 255f,
                (float) color.getG() / 255f,
                (float) color.getB() / 255f,
                alpha
        );

        String text = bitmapText.getText();

        int textureWidth = textureAtlas.getWidth();
        int textureHeight = textureAtlas.getHeight();

        float lineSpacing = bitmapText.getLineSpacing();
        float spacing = bitmapText.getSpacing();

        float boundWidth = bitmapText.getWidth() * bitmapText.getAbsoluteScaleX();
        float boundHeight = bitmapText.getHeight() * bitmapText.getAbsoluteScaleY();

        float drawX = 0;
        float drawY = 0;

        double textureBleedingFix = bitmapText.getTextureBleedingFix();
        double vertexBleedingFix = bitmapText.getVertexBleedingFix();


        GL20.glBegin(GL20.GL_QUADS);

        if (bitmapText.isMulticolorEnabled()) {

            BitmapText.ColorTextData colorTextData = bitmapText.getColorTextData();

            for (int i = 0; i < colorTextData.length(); i++) {
                BitmapText.ColorTextData.Letter letter = colorTextData.getColoredLetter(i);

                char c = letter.getCharacter();

                BitmapCharInfo charInfo = bitmapFont.getCharInfo(c);

                if (charInfo == null) {
                    continue;
                }

                Color letterColor = letter.getColor();

                GL20.glColor4f(
                        (float) letterColor.getR() / 255f,
                        (float) letterColor.getG() / 255f,
                        (float) letterColor.getB() / 255f,
                        alpha
                );

                float charWidth = charInfo.width();
                float charHeight = charInfo.height();

                if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth)) {
                    drawX = 0;
                    drawY += (charHeight + lineSpacing) * scaleY;

                    if (boundHeight != 0 && drawY > boundHeight) {
                        break;
                    }
                }

                drawChar(drawX,
                        (drawY + scaleY * charHeight),
                        textureWidth,
                        textureHeight,
                        charInfo,
                        scaleX,
                        scaleY,
                        textureBleedingFix,
                        vertexBleedingFix);

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

                if (c == '\n' || (boundWidth != 0 && drawX >= boundWidth - charWidth)) {
                    drawX = 0;
                    drawY += (charHeight + lineSpacing) * scaleY;

                    if (boundHeight != 0 && drawY > boundHeight) {
                        break;
                    }
                }

                drawChar(drawX,
                        (drawY + scaleY * charHeight),
                        textureWidth,
                        textureHeight,
                        charInfo,
                        scaleX,
                        scaleY,
                        textureBleedingFix,
                        vertexBleedingFix);

                drawX += (charWidth + (c != '\n' ? spacing : 0)) * scaleX;
            }
        }

        GL20.glEnd();

        GL20.glDisable(GL_BLEND);
        D2D2.getTextureManager().getTextureEngine().disable(textureAtlas);
    }

    private static float nextHalf(float v) {
        return (float) (Math.ceil(v * 2) / 2);
    }

    private void drawChar(
            float x,
            float y,
            int textureAtlasWidth,
            int textureAtlasHeight,
            @NotNull BitmapCharInfo charInfo,
            float scX,
            float scY,
            double textureBleedingFix,
            double vertexBleedingFix) {

        //scX = nextHalf(scX);
        scY = nextHalf(scY);

        float charWidth = charInfo.width();
        float charHeight = charInfo.height();

        float xOnTexture = charInfo.x();
        float yOnTexture = charInfo.y() + charHeight;

        float cx = xOnTexture / textureAtlasWidth;
        float cy = -yOnTexture / textureAtlasHeight;
        float cw = charWidth / textureAtlasWidth;
        float ch = -charHeight / textureAtlasHeight;

        GL20.glTexCoord2d(cx, -cy);
        GL20.glVertex2d(x - vertexBleedingFix, y + vertexBleedingFix);

        GL20.glTexCoord2d(cx + cw, -cy);
        GL20.glVertex2d(charWidth * scX + x + vertexBleedingFix, y + vertexBleedingFix);

        GL20.glTexCoord2d(cx + cw, -cy + ch);
        GL20.glVertex2d(charWidth * scX + x + vertexBleedingFix, charHeight * -scY + y - vertexBleedingFix);

        GL20.glTexCoord2d(cx, -cy + ch);
        GL20.glVertex2d(x - vertexBleedingFix, charHeight * -scY + y - vertexBleedingFix);
    }

    public void setLWJGLTextureEngine(LWJGLTextureEngine textureEngine) {
        this.textureEngine = textureEngine;
    }

}
