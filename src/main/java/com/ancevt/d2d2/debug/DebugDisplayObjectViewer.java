/**
 * Copyright (C) 2024 the original author or authors.
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
package com.ancevt.d2d2.debug;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.common.BorderedRect;
import com.ancevt.d2d2.common.PlainRect;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.IContainer;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.Stage;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.event.Event;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.input.KeyCode;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DebugDisplayObjectViewer {

    private final List<IDisplayObject> displayObjects = new ArrayList<>();

    @Getter
    private List<Class<? extends IDisplayObject>> typesIncluded = new ArrayList<>();
    @Getter
    private List<Class<? extends IDisplayObject>> typesExcluded = new ArrayList<>();
    private IContainer targetContainer;
    private boolean keyListenerEnabled;

    @Builder.Default
    private int depth = -1;

    public DebugDisplayObjectViewer() {

    }

    public DebugDisplayObjectViewer(boolean keyListenerEnabled) {
        setKeyListenerEnabled(keyListenerEnabled);
    }

    public DebugDisplayObjectViewer(IContainer targetContainer, boolean keyListenerEnabled, int depth) {
        setTargetContainer(targetContainer);
        setKeyListenerEnabled(keyListenerEnabled);
        setDepth(depth);
    }

    public DebugDisplayObjectViewer(IContainer targetContainer, boolean keyListenerEnabled) {
        setTargetContainer(targetContainer);
        setKeyListenerEnabled(keyListenerEnabled);
    }

    public DebugDisplayObjectViewer setTypesExcluded(List<Class<? extends IDisplayObject>> typesExcluded) {
        this.typesExcluded = typesExcluded;
        return this;
    }

    public DebugDisplayObjectViewer setTypesIncluded(List<Class<? extends IDisplayObject>> typesIncluded) {
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

    public DebugDisplayObjectViewer setTargetContainer(IContainer targetContainer) {
        this.targetContainer = targetContainer;
        return this;
    }

    public IContainer getTargetContainer() {
        if (targetContainer == null) targetContainer = D2D2.stage();
        return targetContainer;
    }

    public void show() {
        PlainRect plainRect = new PlainRect(D2D2.stage().getWidth(), D2D2.stage().getHeight(), Color.BLACK);
        plainRect.setAlpha(0.5f);
        D2D2.stage().add(plainRect);
        displayObjects.add(plainRect);

        show(getTargetContainer(), -1);
    }

    public void show(IContainer container, int deep) {
        int counter = 0;

        for (int i = 0; i < container.getNumberOfChildren(); i++) {
            IDisplayObject o = container.getChild(i);

            if ((typesIncluded.isEmpty() || typesIncluded.contains(o.getClass())) &&
                (typesExcluded.isEmpty() || !typesExcluded.contains(o.getClass()))) {
                Color color = Color.createVisibleRandomColor();

                BorderedRect borderedRect = new BorderedRect(
                    o.getWidth() * o.getAbsoluteScaleX(),
                    o.getHeight() * o.getAbsoluteY(),
                    null,
                    color);
                D2D2.stage().add(borderedRect, o.getAbsoluteX(), o.getAbsoluteY());

                BitmapText bitmapText = new BitmapText(o.getName());
                bitmapText.setColor(color);
                bitmapText.setRotation(-20);

                D2D2.stage().add(bitmapText, o.getAbsoluteX(), o.getAbsoluteY());

                displayObjects.add(borderedRect);
                displayObjects.add(bitmapText);

                borderedRect.addEventListener(Event.ENTER_FRAME, event -> {
                    borderedRect.setSize(o.getWidth() * o.getAbsoluteScaleX(), o.getHeight() * o.getAbsoluteScaleY());
                    borderedRect.setXY(o.getAbsoluteX(), o.getAbsoluteY());
                    bitmapText.setXY(borderedRect.getX(), borderedRect.getY());
                });
            }

            if (o instanceof IContainer oc && (deep >= 1 || deep == -1)) {
                show(oc, deep - 1);
            }

            counter++;
            if (counter >= 100) return;
        }
    }

    public void clear() {
        while (!displayObjects.isEmpty()) {
            displayObjects.remove(0).removeFromParent();
        }
    }

    public DebugDisplayObjectViewer setKeyListenerEnabled(boolean b) {
        if (keyListenerEnabled == b) return this;
        keyListenerEnabled = b;

        Stage s = D2D2.stage();

        s.removeEventListener(this, InputEvent.KEY_DOWN);
        s.removeEventListener(this, InputEvent.KEY_UP);

        if (b) {
            s.addEventListener(this, InputEvent.KEY_DOWN, event -> {
                InputEvent e = event.casted();
                if (e.isShift() && e.isControl()) {
                    switch (e.getKeyCode()) {
                        case KeyCode.F1 -> {
                            show();
                        }
                    }
                }
            });

            s.addEventListener(this, InputEvent.KEY_UP, event -> {
                InputEvent e = event.casted();
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
