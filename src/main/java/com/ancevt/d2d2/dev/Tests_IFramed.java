
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.display.FramedDisplayObjectContainer;
import com.ancevt.d2d2.display.FramedSprite;
import com.ancevt.d2d2.display.IFramedDisplayObject;
import com.ancevt.d2d2.display.ISprite;
import com.ancevt.d2d2.display.Root;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureManager;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;

public class Tests_IFramed {

    public static void main(String[] args) {
        D2D2.init(new LWJGLBackend(800, 600, Tests_IFramed.class.getName() + " (floating)"));
        Root root = D2D2.getStage().getRoot();

        ISprite[] frames = new ISprite[]{
                new Sprite("frame0"),
                new Sprite("frame1"),
                new Sprite("frame2"),
                new Sprite("frame3"),
                new Sprite("frame4"),
                new Sprite("frame5"),
                new Sprite("frame6"),
                new Sprite("frame7"),
                new Sprite("frame8"),
                new Sprite("frame9"),
                new Sprite("frame10"),
                new Sprite("frame11"),
        };

        IFramedDisplayObject framedDisplayObject = new FramedDisplayObjectContainer(frames, true);
        framedDisplayObject.setLoop(true);
        framedDisplayObject.play();
        root.add(framedDisplayObject);

        TextureManager tm = D2D2.getTextureManager();

        Texture[] frameTextures = new Texture[]{
                tm.getTexture("frame0"),
                tm.getTexture("frame1"),
                tm.getTexture("frame2"),
                tm.getTexture("frame3"),
                tm.getTexture("frame4"),
                tm.getTexture("frame5"),
                tm.getTexture("frame6"),
                tm.getTexture("frame7"),
                tm.getTexture("frame8"),
                tm.getTexture("frame9"),
                tm.getTexture("frame10"),
                tm.getTexture("frame11"),
        };

        framedDisplayObject = new FramedSprite(frameTextures);
        framedDisplayObject.setLoop(true);
        framedDisplayObject.play();
        framedDisplayObject.setSlowing(6);
        root.add(framedDisplayObject, 0, 16);

        D2D2.loop();
    }
}
