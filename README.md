![Logo](https://raw.githubusercontent.com/Anc3vt/d2d2-core/4c64630309467b85978e933a7d12da2bca138e48/img/logo.png
)

![Apache License 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

# About

D2D2 is a fast and user-friendly 2D Java framework with a simple DSL, a display graph for rendered display objects, and
an event model akin to JavaScript and ActionScript (addEventListener). It benefits from accelerated hardware rendering
for enhanced performance.

The goal of D2D2 is to create an easy-to-use framework for rapid development of 2D games and applications in the Java
language. I aim to provide developers with tools that allow them to focus on the creative process and achieve desired
results without unnecessary difficulties.

- [Project goal](#project-goal)
- [Features](#features)
- [Dependency](#dependency)
- [Usage & examples](#usage--examples)
    - [Framework Initialization](#framework-initialization)
    - [Sprite](#sprite)
    - [TextureManager](#texturemanager)
    - [BitmapText](#bitmaptext)
    - [Container](#container)
    - [Event dispatching & listening](#event-dispatching--listening)
    - [Interactive display objects](#interactive-display-objects)
    - [Animated display objects & loading multiple textures in a row](#animated-display-objects--loading-multiple-textures-in-a-row)
- [Some demo videos](#some-demo-videos)
- [Contribution](#contribution)
- [Contact](#contact)

# Features

- **Display Graph:** D2D2 provides classes for working with display objects (DisplayObject), containers, as well as
  methods like addChild and removeChild for managing object display. All DisplayObject and their descendants retain
  basic properties and methods from ActionScript 3.0, such as x, y, rotation, alpha, scaleX, scaleY, visible, and
  others. This ensures a familiar interface for controlling the position, rotation, transparency, and scaling of objects
  on the scene.

- **Event Model:** Support for adding and removing event handlers via addEventListener and removeEventListener methods,
  simplifying the organization of event logic.

- **Screen Text:** Ability to display text using TrueType fonts, providing flexibility in formatting text elements.

- **User Input Handling:** The framework provides tools for handling user input through computer input devices, making
  it easy to create interactive applications.

- **Additional Tools and Utilities:** Additional tools are provided within the project, including working with textures,
  sound effects, and other features inspired by ActionScript 3.0 functionality.

# Dependency

To include the D2D2 library in your Maven project, add the following to your `pom.xml`:

In the `<repositories>` section:

```xml

<repository>
    <id>ancevt</id>
    <url>https://packages.ancevt.com/releases</url>
    <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
    </snapshots>
</repository>
<repository>
<id>ancevt-snapshot</id>
<url>https://packages.ancevt.com/snapshots</url>
<snapshots>
    <enabled>true</enabled>
    <updatePolicy>always</updatePolicy>
</snapshots>
</repository>
```

And in the `<dependencies>` section:

```xml

<dependency>
    <groupId>com.ancevt.d2d2</groupId>
    <artifactId>d2d2-core</artifactId>
    <version>0.1.6.0-beta</version>
</dependency>
```

# Usage & examples

## Framework initialization

After connecting the necessary dependency (d2d2-core) to your project, it is necessary to add a configuration file d2d2.properties to the resources with the following contents::

```properties
d2d2.backend=com.ancevt.d2d2.backend.lwjgl.LwjglBackend
d2d2.window.width=800
d2d2.window.height=600
d2d2.window.title=Window title
```

- `d2d2.backend` determines which backend implementation should be used for graphics and input processing. Currently, in the standard D2D2 distribution, there are two backend implementations: `LwjglBackend`, which uses the LWJGL2 library for rendering on the client side, and `NoRenderBackend`, which does not render anything but can be useful for implementing the server-side of D2D2 applications.
- `d2d2.window.title` specifies the title of the application window.
- `d2d2.window.width` and `d2d2.window.height` determine the initial dimensions of the application window.
- `d2d2.window.title` defines the title of the application window.

To initialize the framework, you need to extend the `D2D2Main` class and call the `D2D2.init(YourMainClass.class)` method, passing your class into it. This class will be the entry point of your application. Then, you need to override the `onCreate` and `onDispose` methods. All the main logic of the application in the D2D2 paradigm should be defined in the `onCreate` method, as shown in the example below:

```java
public class FrameworkInitDemo extends D2D2Main {

    public static void main(String[] args) {
        D2D2.init(FrameworkInitDemo.class);
    }

    @Override
    public void onCreate(Stage stage) {
        // Your application code goes here
    }

    @Override
    public void onDispose() {
        // Dispose resources & save state
    }

}
```

The `onCreate` method is called immediately after the framework initialization. The `stage` is the root container for the entire application and will contain all displayed display objects with the entire hierarchy of nested containers within each other.

`onDispose` method is automatically called when the application is closed by the operating system's standard means
or after calling `D2D2.exit()` Here, you should program resource cleanup, saving various states, etc.

## Sprite

`Sprite` is one of the primary types of display objects, representing a static customizable image. The example below demonstrates how to create, configure, and add it to the scene. Since `Sprite` inherits methods from `IDisplayObject`, we can customize it using them.

Resource files like the one shown in the example below, `flowers.png`, should be located in the subdirectory of the classpath `assets/`. Consequently, by default in a Maven project, this would be `src/resources/assets/`.

```java

@Override
public void onCreate(Stage stage) {
    Sprite sprite = new Sprite("flower.png");

    // Set the properties of the sprite
    sprite.setXY(100, 100);
    sprite.setAlpha(0.75f);
    sprite.setRotation(45);
    sprite.setRepeatX(5);
    sprite.setScale(0.5f, 0.5f);

    // Add the sprite to the stage
    stage.add(sprite);
}
```

Suppose the source resource file `flower.png` looks like this:

![flower.png](https://raw.githubusercontent.com/Anc3vt/d2d2-core/4c64630309467b85978e933a7d12da2bca138e48/img/flower.png)

Upon running the application, the `Sprite` will be displayed with all the properties assigned to it, including position, opacity, rotation, X-axis repetition, and scaling.

![Sprite demo](https://raw.githubusercontent.com/Anc3vt/d2d2-core/4c64630309467b85978e933a7d12da2bca138e48/img/SimpleSpriteDemo.png)

Теперь в stage содержится один экранный объект, это наш `Sprite`.

`Sprite` provides many other useful methods. Additionally, for creating sprites, you can use the `SpriteFactory` class.

## TextureManager

There's a more detailed and flexible way to manage texture resources - `TextureManager`. You can obtain the `TextureManager` by calling the static method `D2D2.textureManager()`. It contains all the loaded texture atlases. Texture atlases allow you to create textures from source resource files where images are combined.

This way, you can store multiple images in a single PNG file, extracting the textures we need from it based on specified coordinates on the atlas.

For example, in the `assets/` directory of our project, there's a source resource file `d2d2-samples-tileset.png`, which looks like this:

![Sprite demo](https://raw.githubusercontent.com/Anc3vt/d2d2-core/4c64630309467b85978e933a7d12da2bca138e48/img/d2d2-samples-tileset.png)

Suppose we need a sprite that will display only the large letter 'D' from this atlas. We can load the resource file, creating a `TextureAtlas`. The example below demonstrates how to "cut out" only the part of the image we need, creating a `Texture`, and apply it to an instance of `Sprite`, for subsequent placement on the scene.

```java

@Override
public void onCreate(Stage stage) {
    // Get the texture manager from D2D2
    TextureManager textureManager = D2D2.textureManager();
    // Load the texture atlas from src/resources/assets/
    TextureAtlas textureAtlas = textureManager.loadTextureAtlas("d2d2-samples-tileset.png");
    // Create a texture from the atlas with the specified coordinates and dimensions
    Texture texture = textureAtlas.createTexture(256, 0, 144, 128);
    // Create a sprite using the created texture
    Sprite sprite = new Sprite(texture);

    // Add the sprite to the stage
    stage.add(sprite);
    // Center the sprite on the stage
    sprite.center();
}
```

In the example above, pay attention to the method call `textureAtlas.createTexture(256, 0, 144, 128)`, where the coordinates of the required texture on the texture atlas and its size in pixels are passed.

`256,0` - are the coordinates of the top-left corner of the texture on the atlas, and `144,128` - is the size of the texture.
> Please note that the image has been scaled for convenience in the diagram.

![TextureManager](https://raw.githubusercontent.com/Anc3vt/d2d2-core/09a1f6658d3f0ea4219e5cd3f16c3e3ed6f75937/img/textureManagerScheme.png)

To ensure optimal use of video memory and texture performance, it's advisable to make the dimensions of textures on the atlas power-of-two multiples. Instead of using multiples of 8, 16, 32, etc., which are suitable for specific architectures, a good practice is to choose sizes that are multiples of 2.

Here are some commonly used sizes that meet this requirement:

    2x2
    4x4
    8x8
    16x16
    32x32
    64x64
    128x128
    256x256
    and so on

Use these sizes to create texture atlases. For example, if you want to create a texture atlas with dimensions of 256x256 pixels, you can place 16 textures of size 64x64 pixels on it, or 64 textures of size 32x32 pixels, and so on.

This approach ensures efficient use of video memory and reduces GPU load.

The procedure for unloading texture atlases is the reverse of loading: `D2D2.textureManager().unloadTextureAtlas(textureAtlas)`.

## BitmapText

Like `Sprite`, `BitmapText` is one of the implementations of the `IDisplayObject` interface, allowing text to be displayed on the scene. D2D2 supports runtime conversion of TrueType fonts into `BitmapFont`, which can be used in `BitmapText`.

### BitmapText with the default BitmapFont

```java

@Override
public void onCreate(Stage stage) {
    BitmapText bitmapText1 = new BitmapText();
    bitmapText1.setText("bitmapText1: Using default bitmap font\nSecond line...\nThird...");
    bitmapText1.setColor(Color.YELLOW);
    bitmapText1.setScale(2, 2);
    stage.add(bitmapText1, 100, 100);
}
```

It will look like this:

![BitmapText1](https://raw.githubusercontent.com/Anc3vt/d2d2-core/09a1f6658d3f0ea4219e5cd3f16c3e3ed6f75937/img/bitmapText1.png)

### BitmapText with the TrueType font, с using `TtfBitmapFontBuilder`

```java
public void onCreate(Stage stage) {
    BitmapFont bitmapFont = new TtfBitmapFontBuilder()
        .fontSize(24)
        .ttfAssetPath("d2d2ttf/FreeSansBold.ttf")
        .textAntialias(true)
        .build();

    BitmapText bitmapText2 = new BitmapText(bitmapFont);
    bitmapText2.setText("bitmapText2: Using TtfBitmapFontBuilder generated bitmap font");
    bitmapText2.setColor(Color.GREEN);
    stage.add(bitmapText2, 100, 200);
}
```

In this example, the TrueType font file is located in the resources as `assets/d2d2fonts/FreeSansBold.ttf`.

It will look like this:

![BitmapText2](https://raw.githubusercontent.com/Anc3vt/d2d2-core/09a1f6658d3f0ea4219e5cd3f16c3e3ed6f75937/img/bitmapText2.png)

### Multicolor BitmapText

```java
public void onCreate(Stage stage) {
    BitmapFont bitmapFont = new TtfBitmapFontBuilder()
        .fontSize(24)
        .ttfAssetPath("d2d2ttf/FreeSansBold.ttf")
        .textAntialias(true)
        .build();

    BitmapText bitmapText3 = new BitmapText(bitmapFont);
    // Enable multicolor
    bitmapText3.setMulticolorEnabled(true);
    // Multicolor text should start with the `#` sign. It will not be rendered and will only signal
    // to the bitmap text that it should be rendered using multicolor
    bitmapText3.setText("#bitmapText3: <FF00FF>multicolor<FF99EE> bitmap<0000FF> text\n" +
        "<AABBEE>The second line of bitmap text");

    // place it on the stage
    stage.add(bitmapText3, 100, 300);
}
```

It will look like this:

![BitmapText3](https://raw.githubusercontent.com/Anc3vt/d2d2-core/09a1f6658d3f0ea4219e5cd3f16c3e3ed6f75937/img/bitmapText3.png)

## Container

`Container` is an object that can contain any display objects, including other containers. By adding containers to each other, a hierarchy of display objects is organized on the `Stage`. The `Stage` itself is also a container since it implements the `IContainer` interface.

Initially, all empty containers are invisible to the user. Therefore, in the example below, we create a gray `BorderedRect` without fill, and create a `Container`, filling it with this `BorderedRect`.

```java

@Override
public void onCreate(Stage stage) {
    BorderedRect borderedRect = new BorderedRect(500, 500, Color.NO_COLOR, Color.DARK_GRAY);

    // Create a container with an instant placement of the frame into itк
    IContainer container = Container.createContainer(borderedRect);

    // Create two sprites
    Sprite sprite1 = new Sprite("flower.png");
    Sprite sprite2 = new Sprite("flower.png");

    container.add(sprite1, 50, 50);
    container.add(sprite2, 200, 200);

    // Rotate the entire container by 10 degrees
    container.rotate(10);

    // Add our container to the stage. In fact, Stage is also a container, since
    // it implements the IContainer interface
    stage.add(container, 100, 100);

}
```

In the example above, when rendered, all content of the container will also rotate along with the container. However, accessing properties of objects placed inside the container will remain unchanged.

Here's how it will look when the application is launched:

![TextureAltas](https://raw.githubusercontent.com/Anc3vt/d2d2-core/09a1f6658d3f0ea4219e5cd3f16c3e3ed6f75937/img/ContainerDemo.png)

> It's important to understand that nested containers and other display objects placed inside containers apply their properties relative to their parent container, not the global coordinate axis of the `Stage`. Thus, if we, for example, rotate the container using the `rotate` or `setRotation` methods, visually, all its contents will also rotate along with it. Of course, this applies not only to rotation but also to other properties of `IDisplayObject`, such as x, y, alpha, scaleX, scaleY, and others.

![TextureAltas](https://raw.githubusercontent.com/Anc3vt/d2d2-core/09a1f6658d3f0ea4219e5cd3f16c3e3ed6f75937/img/containerScheme.png)

All display objects can retrieve their parent container using the `getParent()` method. If a display object is not added to any container, the method will return `null`, so it's a good practice to check for the presence of a parent container using the `hasParent()` method. In turn, containers have methods such as `getChildrenCount()` and `getChild(int index)`. Check out other useful methods in the `IContainer` interface in the documentation and source code.

## Event dispatching & listening

In D2D2, an event model similar to that in JavaScript and ActionScript is implemented. Methods such as `addEventListener`, `removeEventListener`, and `dispatchEvent` are used.

```java

@Override
public void onCreate(Stage stage) {
    // Create a status text for example convenience
    BitmapText statusText = new BitmapText();
    statusText.setScale(3, 3);
    stage.add(statusText, 10, 10);

    // Register a listener for the RESIZE event for the Stage
    // When the window size changes (or when switching to fullscreen mode)
    // our status text will be updated
    stage.addEventListener(Event.RESIZE, event -> {
        float width = stage.getWidth();
        float height = stage.getHeight();

        statusText.setText((int) width + "x" + (int) height);
    });
}
```

In the example above, we subscribed to the `RESIZE` event and implemented an event handler. In this case, we change the status text when the application window is resized.

It will look like this:

![EventListener1](https://raw.githubusercontent.com/Anc3vt/d2d2-core/09a1f6658d3f0ea4219e5cd3f16c3e3ed6f75937/img/EventListenerDemo1.gif)

display objects also dispatch events. There are three events that occur regularly/every frame: before frame rendering `Event.ENTER_FRAME`, after frame rendering `Event.EXIT_FRAME`, and during the next iteration of the global event loop `Event.LOOP_UPDATE`, which can be useful if you need to perform any actions continuously regardless of rendering and FPS.

```java
public void onCreate(Stage stage) {
    Sprite sprite = new Sprite("flower.png");
    sprite.setXY(400, 300);

    sprite.addEventListener(Event.LOOP_UPDATE, event -> {
        sprite.rotate(-5);
    });

    stage.add(sprite);
}
```

It will look like this:

![EventListener2](https://raw.githubusercontent.com/Anc3vt/d2d2-core/daf86c03433c7fe396c01627676ee6633d77b902/img/EventListenerDemo2.gif)

> It's easy to notice that the sprite rotates around its top-left corner, which is the correct behavior for the example above. To make the object rotate around its own center, you need to add it to a container, move it to the left and up by half of its size, and then rotate the container instead of the object itself.

Example of rotation around its own center:

```java

@Override
public void onCreate(Stage stage) {
    IContainer container = Container.createContainer();

    Sprite sprite = new Sprite("flower.png");

    container.add(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);

    container.addEventListener(Event.LOOP_UPDATE, event -> {
        container.rotate(-5);
    });

    stage.add(container, 400, 300);
}

```

It will look like this:

![EventListener3](https://raw.githubusercontent.com/Anc3vt/d2d2-core/daf86c03433c7fe396c01627676ee6633d77b902/img/controt.gif)

Currently, in D2D2, there are many pre-implemented events in `Event` and its subclasses. Additionally, you can implement your own events, dispatch them from your classes inherited from `EventDispatcher`, and register listener functions.

Unlike JavaScript and ActionScript, D2D2 adds the ability to remove all listeners at once or listeners of specific event types by key. For this purpose, `IEventDispatcher` has the following methods:

- `addEventListener(Object key, String type, EventListener listener);`
- `removeEventListener(Object key, String type);`
- `removeAllEventListeners();`
- `removeAllEventListeners(String type);`

## Interactive display objects

Interactive objects are based on the event model described above. Here's an example of creating an interactive container and user interaction:

```java

@Override
public void onCreate(Stage stage) {
    // Create a text object for status display
    BitmapText statusText = new BitmapText();
    // Set the text scale
    statusText.setScale(3, 3);
    // Add the text to the stage and set its position
    stage.add(statusText, 300, 50);

    // Create an interactive container with the sprite "flower.png"
    InteractiveContainer interactiveContainer = InteractiveContainer.createInteractiveContainer(new Sprite("flower.png"));

    // Register a listener for the DOWN event for the container
    interactiveContainer.addEventListener(InteractiveEvent.DOWN, event -> {
        interactiveContainer.move(2, 2);
        statusText.setText("InteractiveEvent.DOWN");
    });

    // Register a listener for the UP event for the container
    interactiveContainer.addEventListener(InteractiveEvent.UP, event -> {
        interactiveContainer.move(-2, -2);
        statusText.setText("InteractiveEvent.UP");
    });

    // Register a listener for the HOVER event for the container
    interactiveContainer.addEventListener(InteractiveEvent.HOVER, event -> {
        interactiveContainer.setAlpha(1);
        statusText.setColor(Color.WHITE);
        statusText.setText("InteractiveEvent.HOVER");
    });

    // Register a listener for the OUT event for the container
    interactiveContainer.addEventListener(InteractiveEvent.OUT, event -> {
        interactiveContainer.setAlpha(0.5f);
        statusText.setColor(Color.GRAY);
        statusText.setText("InteractiveEvent.OUT");
    });

    // Register a listener for the KEY_TYPE event for the container
    interactiveContainer.addEventListener(InteractiveEvent.KEY_TYPE, event -> {
        InteractiveEvent e = event.casted();
        // Get the key type
        String keyType = e.getKeyType();
        // Set the status text
        statusText.setText("InteractiveEvent.KEY_TYPE:\n" + keyType);
    });

    // Add the container to the stage
    stage.add(interactiveContainer);
}
```

It will look like this:

![Interactive](https://raw.githubusercontent.com/Anc3vt/d2d2-core/daf86c03433c7fe396c01627676ee6633d77b902/img/InteractiveDemo.gif)

For the `Stage`, user interaction events are also implemented.

> Unlike the `Interactive` interface, which dispatches `InteractiveEvent`, the `Stage` dispatches `InputEvent`, which is important to consider to avoid "nothing-happens" errors.

## Animated display objects & loading multiple textures in a row

`IAnimated` is an interface that defines common functionality for all animated frame-based display objects. In the current version of D2D2, there are two such display objects implemented: `AnimatedSprite` and `AnimatedContainer`. As you might guess, one extends `Sprite`, and the other extends `Container`.

```java

@Override
public void onCreate(Stage stage) {
    // Set the scene background color
    stage.setBackgroundColor(0x000010);

    // Create some funny background elements
    createSomeBackground();

    // Load the texture atlas and create textures
    Texture[] textures = D2D2.textureManager()
        .loadTextureAtlas("d2d2-samples-tileset.png")
        .createTexturesHor(256, 128, 48, 48, 4);
    
    // Create an animated sprite
    IAnimated anim = new AnimatedSprite(textures);

    // Set the scale of the sprite
    anim.setScale(8, 8);
    // Set the animation slowing factor
    anim.setSlowing(15);
    // Set the animation infinity loop
    anim.setLoop(true);
    // Start playing the animation
    anim.play();

    // Add the animated sprite to the stage
    stage.add(anim, 100, 100);

    // Add an FPS meter to the stage
    stage.add(new FpsMeter());
}
```

It will look like this:

![Animated](https://raw.githubusercontent.com/Anc3vt/d2d2-core/daf86c03433c7fe396c01627676ee6633d77b902/img/AnimatedDemo.gif)

> Pay attention to the method call `createTexturesHor(256, 128, 48, 48, 4)`. It creates multiple textures at once, arranged horizontally on the atlas. The first four arguments are the position of the texture on the atlas and its dimensions, and the fifth argument is the number of repetitions to the right, as shown in the diagram:

![AnimatedScheme](https://raw.githubusercontent.com/Anc3vt/d2d2-core/daf86c03433c7fe396c01627676ee6633d77b902/img/animatedScheme.png)


To create multiple textures vertically, the `TextureAtlas` also has a method called `createTextureVert`.

---

# Some demo videos

### Multiplayer game (Click on the picture to watch the video)

[![Multiplayer game](https://raw.githubusercontent.com/Anc3vt/d2d2-core/4c64630309467b85978e933a7d12da2bca138e48/img/game_preview.png)](https://www.youtube.com/watch?v=YrSkHELR89w)

### Window manager (Click on the picture to watch the video)

[![Window manager](https://raw.githubusercontent.com/Anc3vt/d2d2-core/4c64630309467b85978e933a7d12da2bca138e48/img/window_manager_preview.png)](https://www.youtube.com/watch?v=P3SNHOAOBMo)

# Contribution

Contributions to the D2D2 project are welcome. If you have ideas, suggestions, or bug fixes, please open a new issue or
create a pull request in my GitHub repository.

# Contact

If you have any questions regarding D2D2 or anything else, please feel free to email me at [me@ancevt.com](mailto:me@ancevt.com).