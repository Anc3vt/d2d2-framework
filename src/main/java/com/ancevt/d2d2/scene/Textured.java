package com.ancevt.d2d2.scene;

import com.ancevt.d2d2.scene.texture.TextureRegion;

public interface Textured {

    void setTextureRegion(TextureRegion textureRegion);

    TextureRegion getTextureRegion();

    void setTextureUVRepeat(float uRepeat, float vRepeat);

    float getTextureURepeat();

    float getTextureVRepeat();

    void setTextureURepeat(float repeat);

    void setTextureVRepeat(float repeat);

    void setTextureRotation(float radians);

    float getTextureRotation() ;

    void setTextureScale(float scaleX, float scaleY);

    float getTextureScaleX() ;

    float getTextureScaleY() ;
}
