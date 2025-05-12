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
import com.ancevt.d2d2.scene.Group;
import com.ancevt.d2d2.scene.Node;
import com.ancevt.d2d2.scene.Root;
import com.ancevt.d2d2.scene.shape.BorderedRectangle;
import com.ancevt.d2d2.scene.shape.RectangleShape;
import com.ancevt.d2d2.scene.text.Text;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DebugDisplayObjectViewer {

    private final List<Node> sceneEntities = new ArrayList<>();

    @Getter
    private List<Class<? extends Node>> typesIncluded = new ArrayList<>();
    @Getter
    private List<Class<? extends Node>> typesExcluded = new ArrayList<>();
    private Group targetGroup;
    private boolean keyListenerEnabled;

    private int depth = -1;

    public DebugDisplayObjectViewer() {

    }

    public DebugDisplayObjectViewer(boolean keyListenerEnabled) {
        setKeyListenerEnabled(keyListenerEnabled);
    }

    public DebugDisplayObjectViewer(Group targetGroup, boolean keyListenerEnabled, int depth) {
        setTargetGroup(targetGroup);
        setKeyListenerEnabled(keyListenerEnabled);
        setDepth(depth);
    }

    public DebugDisplayObjectViewer(Group targetGroup, boolean keyListenerEnabled) {
        setTargetGroup(targetGroup);
        setKeyListenerEnabled(keyListenerEnabled);
    }

    public DebugDisplayObjectViewer setTypesExcluded(List<Class<? extends Node>> typesExcluded) {
        this.typesExcluded = typesExcluded;
        return this;
    }

    public DebugDisplayObjectViewer setTypesIncluded(List<Class<? extends Node>> typesIncluded) {
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

    public DebugDisplayObjectViewer setTargetGroup(Group targetGroup) {
        this.targetGroup = targetGroup;
        return this;
    }

    public Group getTargetGroup() {
        if (targetGroup == null) targetGroup = D2D2.root();
        return targetGroup;
    }

    public void show() {
        RectangleShape rectangleShape = new RectangleShape(D2D2.root().getWidth(), D2D2.root().getHeight(), Color.BLACK);
        rectangleShape.setAlpha(0.5f);
        D2D2.root().addChild(rectangleShape);
        sceneEntities.add(rectangleShape);

        show(getTargetGroup(), -1);
    }

    public void show(Group group, int deep) {
        int counter = 0;

        for (int i = 0; i < group.getNumChildren(); i++) {
            Node o = group.getChild(i);

            if ((typesIncluded.isEmpty() || typesIncluded.contains(o.getClass())) &&
                    (typesExcluded.isEmpty() || !typesExcluded.contains(o.getClass()))) {
                Color color = Color.createVisibleRandomColor();

                BorderedRectangle borderedRectangle = new BorderedRectangle(
                        o.getWidth() * o.getGlobalScaleX(),
                        o.getHeight() * o.getGlobalY(),
                        null,
                        color);
                D2D2.root().addChild(borderedRectangle, o.getGlobalX(), o.getGlobalY());

                Text text = Text.create();
                text.setText(o.getName());
                text.setColor(color);
                text.setRotation(-20);

                D2D2.root().addChild(text, o.getGlobalX(), o.getGlobalY());

                sceneEntities.add(borderedRectangle);
                sceneEntities.add(text);

                borderedRectangle.addEventListener(SceneEvent.PreFrame.class, e -> {
                    borderedRectangle.setSize(o.getWidth() * o.getGlobalScaleX(), o.getHeight() * o.getGlobalScaleY());
                    borderedRectangle.setPosition(o.getGlobalX(), o.getGlobalY());
                    text.setPosition(borderedRectangle.getX(), borderedRectangle.getY());
                });
            }

            if (o instanceof Group oc && (deep >= 1 || deep == -1)) {
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

        Root s = D2D2.root();

        s.removeEventListener(this, InputEvent.KeyDown.class);
        s.removeEventListener(this, InputEvent.KeyUp.class);

        if (b) {
            s.addEventListener(this, InputEvent.KeyDown.class, e -> {
                if (e.isShift() && e.isControl()) {
                    switch (e.getKeyCode()) {
                        case KeyCode.F1 -> {
                            show();
                        }
                    }
                }
            });

            s.addEventListener(this, InputEvent.KeyUp.class, e -> {
                switch (e.getKeyCode()) {
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
