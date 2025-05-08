```java
/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.samples;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Group;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.Sprite;

public class ContainerCenterRotateDemo implements D2D2Application {

    public static void main(String[] args) {
        D2D2.init(ContainerCenterRotateDemo.class, args);
    }

    @Override
    public void onCreate(Root root) {
        Group container = Group.create();
        Sprite sprite = Sprite.create("flower.png");
        container.addChild(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);
        container.addEventListener(NodeEvent.LoopUpdate.class, e -> container.rotate(-5));
        root.addChild(container, 400, 300);
    }
}



/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.samples;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.Sprite;
import com.ancevt.d2d2.scene.text.Text;

public class EventListenerDemo implements D2D2Application {

    public static void main(String[] args) {
        D2D2.init(EventListenerDemo.class, args);
    }

    @Override
    public void onCreate(Root root) {

        // ------------ Example 1 ------------

        // Create a status text for demonstration convenience
        Text statusText = new Text();
        statusText.setScale(3, 3);
        root.addChild(statusText, 10, 10);

        // For example, register a listener for the RESIZE event for the Stage
        // When the window size changes (or when switching to fullscreen mode)
        // our status text will be updated
        root.addEventListener(CommonEvent.Resize.class, event -> {
            float width = root.getWidth();
            float height = root.getHeight();

            statusText.setText((int) width + "x" + (int) height);
        });


        // ------------ Example 2 ------------

        // Create a sprite and set its coordinates
        Sprite sprite = Sprite.create("flower.png");
        sprite.setXY(400, 300);

        // Register a listener for the LOOP_UPDATE event
        // In the code below, we rotate the sprite by 10 degrees counterclockwise at each update
        // of the D2D2 main loop. Thus, our sprite will rotate rapidly
        sprite.addEventListener(NodeEvent.LoopUpdate.class, e -> {
            sprite.rotate(-5);
        });

        // Add the sprite to the stage
        root.addChild(sprite);
    }

}




/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.samples;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Root;

// The main class of the D2D2 application must inherit from D2D2Main
public class FrameworkInitDemo implements D2D2Application {

    // The entry point of the Java application
    public static void main(String[] args) {
        // Framework initialization with properties defined in src/resources/application.properties
        D2D2.init(FrameworkInitDemo.class, args);
    }

    @Override
    public void onCreate(Root root) {
        // Stage is the root container for the entire application
        root.setBackgroundColor(0x002233);

        // D2D2 application code goes here
        // ...

        // Adding an FPS meter to the Stage. Can be useful for debugging
        root.addChild(new FpsMeter());
    }

    @Override
    public void onDispose() {
        // This method is automatically called when the application is closed by the operating system's standard means
        // or after calling D2D2.exit()
        // Here, you should program resource cleanup, saving various states, etc.
    }

}




/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.samples;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Group;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.Sprite;
import com.ancevt.d2d2.scene.shape.BorderedRectangle;

public class GroupDemo implements D2D2Application {

    public static void main(String[] args) {
        D2D2.init(GroupDemo.class, args);
    }

    @Override
    public void onCreate(Root scene) {
        // Initially, empty containers are invisible for user, so for convenience, let's create a rectangle
        // that will serve as a visual frame for our container
        BorderedRectangle borderedRectangle = new BorderedRectangle(500, 500, Color.NO_COLOR, Color.DARK_GRAY);

        // Create a container with an instant placement of the frame into it
        Group container = Group.create();
        container.addChild(borderedRectangle);

        // Create two sprites
        Sprite sprite1 = Sprite.create("flower.png");
        Sprite sprite2 = Sprite.create("flower.png");

        // Place the sprites in the container specifying their positions relative to the container
        // The `add` method overload below allows setting the coordinates of the added display object
        // in the container directly, instead of calling setXY for each sprite separately
        container.addChild(sprite1, 50, 50);
        container.addChild(sprite2, 200, 200);

        // Rotate the entire container by 10 degrees
        // When rendered, all content of the container will also rotate along with the container
        container.rotate(10);

        // Add our container to the stage. In fact, Stage is also a container, since
        // it implements the IContainer interface
        scene.addChild(container, 100, 100);

        // However, accessing properties of objects placed inside the container
        // will remain unchanged
    }

}



/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.samples;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.debug.FpsMeter;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Playable;
import com.ancevt.d2d2.scene.PlayableSprite;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.shape.RectangleShape;

import java.util.Random;

public class PlayableDemo implements D2D2Application {

    public static void main(String[] args) {
        D2D2.init(PlayableDemo.class, args);
    }

    private static final Random random = new Random();

    @Override
    public void onCreate(Root scene) {
        // Set the scene background color
        scene.setBackgroundColor(0x000010);

        // Create some background elements
        createSomeBackground();

        // Create an animated sprite
        Playable anim = new PlayableSprite(
                // Load the texture atlas and create textures
                D2D2.textureManager().loadTexture("d2d2-samples-tileset.png").createTexturesClipsHor(256, 128, 48, 48, 4)
        );

        // Set the scale of the sprite
        anim.setScale(8, 8);
        // Set the animation slowing factor
        anim.setSlowing(15);
        // Set the animation infinity loop
        anim.setLoop(true);
        // Start playing the animation
        anim.play();

        // Add the animated sprite to the stage
        scene.addChild(anim, 100, 100);

        // Add an FPS meter to the stage
        scene.addChild(new FpsMeter());
    }


    private void createSomeBackground() {
        Root root = D2D2.root();

        for (int i = 0; i < root.getHeight() * 2; i++) {
            float size = random.nextFloat(1f, 100f);

            RectangleShape plainRect = new RectangleShape(size, 5, Color.WHITE) {
                @Override
                public void onLoopUpdate() {
                    moveX(-1 * getY() / 50);

                    if (getX() < -getWidth()) {
                        setX(root.getWidth());
                    }
                }
            };

            plainRect.setAlpha(0.05f);

            root.addChild(plainRect, random.nextFloat(root.getWidth()), i / 2);
        }
    }
}


/**
 * Copyright (C) 2025 the original author or authors.
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

package com.ancevt.d2d2.samples;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.Sprite;

public class SimpleSpriteDemo implements D2D2Application {

    public static void main(String[] args) {
        D2D2.init(SimpleSpriteDemo.class, args);
    }

    @Override
    public void onCreate(Root root) {
        // Create a sprite with the image "flower.png".
        // The resource file flower.png should be located in the src/resources/assets/ directory.
        Sprite sprite = Sprite.create("flower.png");

        // Since Sprite inherits properties from IDisplayObject,
        // we can customize it using IDisplayObject methods.

        // Set the coordinates of the sprite
        sprite.setXY(100, 100);
        // Set the transparency of the sprite (value from 0.0f to 1.0f)
        sprite.setAlpha(0.75f);
        // Set the rotation angle of the sprite (degrees, values 0 - 360)
        sprite.setRotation(45);
        // Set the number of repetitions of the sprite along the X-axis
        sprite.setRepeatX(5);
        // Set the scale of the sprite along the X and Y axes. In the example below, we reduce the sprite size by half from the original.
        sprite.setScale(0.5f, 0.5f);

        // Add the sprite to the stage
        root.addChild(sprite);
    }
}



/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.samples;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.text.Font;
import com.ancevt.d2d2.scene.text.Text;
import com.ancevt.d2d2.scene.text.TrueTypeFontBuilder;

public class TextDemo implements D2D2Application {

    public static void main(String[] args) {
        D2D2.init(TextDemo.class, args);
    }

    @Override
    public void onCreate(Root root) {

        // ------------ Example 1 ------------

        // Create the first text with the default font, customize it, and place it on the stage
        Text text1 = new Text();
        text1.setText("text1: Using default font\nSecond line...\nThird...");
        text1.setColor(Color.YELLOW);
        text1.setScale(2, 2);
        root.addChild(text1, 100, 100);


        // ------------ Example 2 ------------

        // Create the second text by generating a font from any TrueType font
        // using TrueTypeFontBuilder
        Font font = new TrueTypeFontBuilder()
                .fontSize(24)
                .assetPath("d2d2ttf/FreeSansBold.ttf")
                .textAntialias(true)
                .build();

        Text text2 = new Text(font);
        text2.setText("text2: Using TrueTypeFontBuilder\n generated font");
        text2.setColor(Color.GREEN);
        // place it on the stage
        root.addChild(text2, 100, 200);


        // ------------ Example 3 ------------

        // Create the third text using a font that has already been created
        // The following code demonstrates the ability to use multicolor in text
        // using hex-RGB tags in the format <FFFFFF>
        Text text3 = new Text(font);
        // Enable multicolor
        text3.setMulticolor(true);
        // Multicolor text should start with the `#` sign. It will not be rendered and will only signal
        // to the text that it should be rendered using multicolor
        text3.setText("text: <FF00FF>multicolor<FF99EE> text<0000FF> text\n" +
                "<AABBEE>The second line of text");

        root.addChild(text3, 100, 300);
        // place it on the stage

    }

}






/**
 * Copyright (C) 2025 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ancevt.d2d2.samples;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.lifecycle.D2D2Application;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.Sprite;
import com.ancevt.d2d2.scene.texture.Texture;
import com.ancevt.d2d2.scene.texture.TextureClip;
import com.ancevt.d2d2.scene.texture.TextureManager;

public class TextureManagerDemo implements D2D2Application {

    public static void main(String[] args) {
        D2D2.init(TextureManagerDemo.class, args);
    }

    @Override
    public void onCreate(Root root) {
        // Get the texture manager from D2D2
        TextureManager textureManager = D2D2.textureManager();
        // Load the texture atlas from src/resources/assets/
        Texture textureAtlas = textureManager.loadTexture("d2d2-samples-tileset.png");
        // Create a texture from the atlas with the specified coordinates and dimensions
        TextureClip textureClip = textureAtlas.createTextureClip(256, 0, 144, 128);
        // Create a sprite using the created texture
        Sprite sprite = Sprite.create(textureClip);

        // Add the sprite to the stage
        root.addChild(sprite);
        // Center the sprite on the stage
        sprite.center();
    }

}



```