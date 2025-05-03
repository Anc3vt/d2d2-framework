package com.ancevt.d2d2;

import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.scene.Sprite;
import com.ancevt.d2d2.scene.SpriteImpl;

public class Dev {
    public static void main(String[] args) {
        Sprite sprite = new SpriteImpl();
        sprite.addEventListener(CommonEvent.Action.class, e->{
            e.getTarget();
        });
    }
}
