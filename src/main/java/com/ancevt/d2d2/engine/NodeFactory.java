package com.ancevt.d2d2.engine;

import com.ancevt.d2d2.scene.*;
import com.ancevt.d2d2.scene.interactive.InteractiveGroup;
import com.ancevt.d2d2.scene.interactive.InteractiveSprite;
import com.ancevt.d2d2.scene.shape.*;
import com.ancevt.d2d2.scene.texture.Texture;
import com.ancevt.d2d2.scene.texture.TextureRegion;

public interface NodeFactory {

    BitmapCanvas createBitmapCanvas(int width, int height);

    Group createGroup();

    Sprite createSprite();

    Sprite createSprite(TextureRegion textureRegion);

    Sprite createSprite(Texture texture);

    Sprite createSprite(String assetFilename);

    Sprite createSprite(String assetFilename,
                        int regionX,
                        int regionY,
                        int regionWidth,
                        int regionHeight);

    InteractiveSprite createInteractiveSprite();

    InteractiveSprite createInteractiveSprite(TextureRegion textureRegion);

    InteractiveSprite createInteractiveSprite(Texture texture);

    InteractiveSprite createInteractiveSprite(String assetFilename);

    InteractiveSprite createInteractiveSprite(String assetFilename,
                                   int regionX,
                                   int regionY,
                                   int regionWidth,
                                   int regionHeight);

    InteractiveGroup createInteractiveGroup(int width, int height);

    AnimatedSprite createAnimatedSprite();

    AnimatedSprite createAnimatedSprite(TextureRegion[] frames);

    AnimatedGroup createAnimatedGroup();

    AnimatedGroup createAnimatedGroup(Sprite[] frames);

    RectangleShape createRectangle(float width, float height);

    RectangleShape createRectangle(float width, float height, Color color);

    BorderedRectangleShape createBorderedRectangle(float width, float height);

    BorderedRectangleShape createBorderedRectangle(float width, float height, Color fillColor);

    BorderedRectangleShape createBorderedRectangle(float width, float height, Color fillColor, Color borderColor);

    LineBatch createLineBatch();

    LineBatch createLineBatch(Color color);

    FreeShape createFreeShape();

    FreeShape createFreeShape(Color color);

    CircleShape createCircleShape(float radius, int numVertices);

    CircleShape createCircleShape(float radius, int numVertices, Color color);

    RoundedCornerShape createRoundCornerShape(float width, float height, float radius, int segments);
}
