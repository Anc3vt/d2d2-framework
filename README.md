![Logo](https://raw.githubusercontent.com/Anc3vt/d2d2-core/9dc0b2adc6d279dc394db609ec4a829c58277c59/img/logo.png
)


![Apache License 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

# About
D2D2 is a fast and user-friendly 2D Java framework with a simple DSL, a display graph for rendered screen objects, and an event model akin to JavaScript and ActionScript (addEventListener). It benefits from accelerated hardware rendering for enhanced performance.

The goal of D2D2 is to create an easy-to-use framework for rapid development of 2D games and applications in the Java language. I aim to provide developers with tools that allow them to focus on the creative process and achieve desired results without unnecessary difficulties.
- [Project goal](#project-goal)
- [Features](#features)
- [Dependency](#dependency)
- [Usage](#usage)
  - [General basic example](#general-basic-example)
  - [Framework Initialization](#framework-initialization)
- [Demo videos](#demo-videos)
- [Contribution](#contribution)

# Features

- **Display Graph:** D2D2 provides classes for working with screen objects (DisplayObject), containers, as well as methods like addChild and removeChild for managing object display. All DisplayObject and their descendants retain basic properties and methods from ActionScript 3.0, such as x, y, rotation, alpha, scaleX, scaleY, visible, and others. This ensures a familiar interface for controlling the position, rotation, transparency, and scaling of objects on the scene.

- **Event Model:** Support for adding and removing event handlers via addEventListener and removeEventListener methods, simplifying the organization of event logic.

- **Screen Text:** Ability to display text using TrueType fonts, providing flexibility in formatting text elements.

- **User Input Handling:** The framework provides tools for handling user input through computer input devices, making it easy to create interactive applications.

- **Additional Tools and Utilities:** Additional tools are provided within the project, including working with textures, sound effects, and other features inspired by ActionScript 3.0 functionality.

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

# Usage

## General basic example
```java
public static void main(String[] args) {
    // Initializing the framework
    Stage stage = D2D2.init(new LWJGLBackend(800, 600, "Window title"));
    
    // Creating a sprite
    Sprite sprite = new Sprite("image-source");
    
    // Adding an event listener triggered on each frame to rotate the sprite
    sprite.addEventListener(Event.ENTER_FRAME, e -> {
        sprite.rotate(0.01f);
    });
    
    // Setting the position of the sprite
    sprite.setXY(400, 300);
    
    // Adding the sprite to the stage
    stage.add(sprite);
    
    // Starting the rendering and event handling loop
    D2D2.loop();
}
```
# Demo videos
### Multiplayer game (Click on the picture to watch the video)
[![Multiplayer game](https://raw.githubusercontent.com/Anc3vt/d2d2-core/9dc0b2adc6d279dc394db609ec4a829c58277c59/img/game_preview.png)](https://www.youtube.com/watch?v=YrSkHELR89w)
### Window manager (Click on the picture to watch the video)
[![Window manager](https://raw.githubusercontent.com/Anc3vt/d2d2-core/9dc0b2adc6d279dc394db609ec4a829c58277c59/img/window_manager.gif)](https://www.youtube.com/watch?v=P3SNHOAOBMo)



# Contribution
Contributions to the D2D2 project are welcome. If you have ideas, suggestions, or bug fixes, please open a new issue or create a pull request in my GitHub repository.
