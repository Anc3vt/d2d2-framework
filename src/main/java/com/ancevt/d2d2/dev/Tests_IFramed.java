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
package com.ancevt.d2d2.dev;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.backend.lwjgl.LWJGLBackend;
import com.ancevt.d2d2.display.FramedDisplayObjectContainer;
import com.ancevt.d2d2.display.FramedSprite;
import com.ancevt.d2d2.display.IFramedDisplayObject;
import com.ancevt.d2d2.display.ISprite;
import com.ancevt.d2d2.display.Sprite;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.texture.Texture;
import com.ancevt.d2d2.display.texture.TextureManager;

public class Tests_IFramed {

    public static void main(String[] args) {
        Stage stage = D2D2.init(new LWJGLBackend(800, 600, Tests_IFramed.class.getName() + " (floating)"));

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
        stage.add(framedDisplayObject);

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
        stage.add(framedDisplayObject, 0, 16);

        D2D2.loop();
    }
}
