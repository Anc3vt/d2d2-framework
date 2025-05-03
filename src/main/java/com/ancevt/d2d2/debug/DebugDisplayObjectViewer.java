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


package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.input.KeyCode;
import com.ancevt.d2d2.scene.Color;
import com.ancevt.d2d2.scene.Container;
import com.ancevt.d2d2.scene.Scene;
import com.ancevt.d2d2.scene.SceneEntity;
import com.ancevt.d2d2.scene.shape.BorderedRectangle;
import com.ancevt.d2d2.scene.shape.RectangleShape;
import com.ancevt.d2d2.scene.text.Text;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DebugDisplayObjectViewer {

    private final List<SceneEntity> sceneEntities = new ArrayList<>();

    @Getter
    private List<Class<? extends SceneEntity>> typesIncluded = new ArrayList<>();
    @Getter
    private List<Class<? extends SceneEntity>> typesExcluded = new ArrayList<>();
    private Container targetContainer;
    private boolean keyListenerEnabled;

    private int depth = -1;

    public DebugDisplayObjectViewer() {

    }

    public DebugDisplayObjectViewer(boolean keyListenerEnabled) {
        setKeyListenerEnabled(keyListenerEnabled);
    }

    public DebugDisplayObjectViewer(Container targetContainer, boolean keyListenerEnabled, int depth) {
        setTargetContainer(targetContainer);
        setKeyListenerEnabled(keyListenerEnabled);
        setDepth(depth);
    }

    public DebugDisplayObjectViewer(Container targetContainer, boolean keyListenerEnabled) {
        setTargetContainer(targetContainer);
        setKeyListenerEnabled(keyListenerEnabled);
    }

    public DebugDisplayObjectViewer setTypesExcluded(List<Class<? extends SceneEntity>> typesExcluded) {
        this.typesExcluded = typesExcluded;
        return this;
    }

    public DebugDisplayObjectViewer setTypesIncluded(List<Class<? extends SceneEntity>> typesIncluded) {
        this.typesIncluded = typesIncluded;
        return this;
    }

    public DebugDisplayObjectViewer setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public int getDepth() {
        return depth;
    }

    public DebugDisplayObjectViewer setTargetContainer(Container targetContainer) {
        this.targetContainer = targetContainer;
        return this;
    }

    public Container getTargetContainer() {
        if (targetContainer == null) targetContainer = D2D2.stage();
        return targetContainer;
    }

    public void show() {
        RectangleShape rectangleShape = new RectangleShape(D2D2.stage().getWidth(), D2D2.stage().getHeight(), Color.BLACK);
        rectangleShape.setAlpha(0.5f);
        D2D2.stage().addChild(rectangleShape);
        sceneEntities.add(rectangleShape);

        show(getTargetContainer(), -1);
    }

    public void show(Container container, int deep) {
        int counter = 0;

        for (int i = 0; i < container.getNumChildren(); i++) {
            SceneEntity o = container.getChild(i);

            if ((typesIncluded.isEmpty() || typesIncluded.contains(o.getClass())) &&
                    (typesExcluded.isEmpty() || !typesExcluded.contains(o.getClass()))) {
                Color color = Color.createVisibleRandomColor();

                BorderedRectangle borderedRectangle = new BorderedRectangle(
                        o.getWidth() * o.getAbsoluteScaleX(),
                        o.getHeight() * o.getAbsoluteY(),
                        null,
                        color);
                D2D2.stage().addChild(borderedRectangle, o.getAbsoluteX(), o.getAbsoluteY());

                Text text = new Text(o.getName());
                text.setColor(color);
                text.setRotation(-20);

                D2D2.stage().addChild(text, o.getAbsoluteX(), o.getAbsoluteY());

                sceneEntities.add(borderedRectangle);
                sceneEntities.add(text);

                borderedRectangle.addEventListener(SceneEvent.EnterFrame.class, e -> {
                    borderedRectangle.setSize(o.getWidth() * o.getAbsoluteScaleX(), o.getHeight() * o.getAbsoluteScaleY());
                    borderedRectangle.setXY(o.getAbsoluteX(), o.getAbsoluteY());
                    text.setXY(borderedRectangle.getX(), borderedRectangle.getY());
                });
            }

            if (o instanceof Container oc && (deep >= 1 || deep == -1)) {
                show(oc, deep - 1);
            }

            counter++;
            if (counter >= 100) return;
        }
    }

    public void clear() {
        while (!sceneEntities.isEmpty()) {
            sceneEntities.remove(0).removeFromParent();
        }
    }

    public DebugDisplayObjectViewer setKeyListenerEnabled(boolean b) {
        if (keyListenerEnabled == b) return this;
        keyListenerEnabled = b;

        Scene s = D2D2.stage();

        s.removeEventListener(this, InputEvent.KeyDown.class);
        s.removeEventListener(this, InputEvent.KeyUp.class);

        if (b) {
            s.addEventListener(this, InputEvent.KeyDown.class, e -> {
                if (e.shift() && e.control()) {
                    switch (e.keyCode()) {
                        case KeyCode.F1 -> {
                            show();
                        }
                    }
                }
            });

            s.addEventListener(this, InputEvent.KeyUp.class, e -> {
                switch (e.keyCode()) {
                    case KeyCode.F1 -> {
                        clear();
                    }
                }
            });
        }
        return this;
    }

    public boolean isKeyListenerEnabled() {
        return keyListenerEnabled;
    }
}
