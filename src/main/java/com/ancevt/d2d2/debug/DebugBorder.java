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
import com.ancevt.d2d2.common.Disposable;
import com.ancevt.d2d2.event.core.Event;
import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.scene.*;
import com.ancevt.d2d2.scene.shape.BorderedRectangle;
import com.ancevt.d2d2.scene.text.BitmapText;

public class DebugBorder extends BasicGroup implements Resizable, Colored, Disposable {

    private final BorderedRectangle borderedRectangle;
    private final Node node;
    private final BitmapText label;

    private boolean disposed;

    private DebugBorder(Node node) {
        Color color = Color.createVisibleRandomColor();

        this.node = node;
        borderedRectangle = new BorderedRectangle(node.getWidth(),
                node.getHeight(),
                Color.NO_COLOR,
                color
        );

        addChild(borderedRectangle);

        label = new BitmapText();
        label.setText(node.getNodeId() + " " + node.getName());
        label.setAutosize(true);
        addChild(label, 2, -label.getHeight());

        node.addEventListener(this, SceneEvent.AddToScene.class, this::displayObject_addToStage);
        node.addEventListener(this, SceneEvent.RemoveFromScene.class, this::displayObject_removeFromStage);
    }

    private void displayObject_removeFromStage(Event event) {
        removeFromParent();
    }

    private void displayObject_addToStage(Event event) {
        D2D2.stage().addChild(this);
    }

    @Override
    public void setSize(float width, float height) {
        borderedRectangle.setSize(width, height);
    }

    @Override
    public void setWidth(float value) {
        borderedRectangle.setWidth(value);
    }

    @Override
    public void setHeight(float value) {
        borderedRectangle.setHeight(value);
    }

    @Override
    public void setColor(Color color) {
        borderedRectangle.setBorderColor(color);
        label.setColor(color);
    }

    @Override
    public Color getColor() {
        return borderedRectangle.getBorderColor();
    }

    @Override
    public void preFrame() {
        setPosition(node.getGlobalX(), node.getGlobalY());
        setSize(node.getWidth(), node.getHeight());
        setScale(node.getGlobalScaleX(), node.getGlobalScaleY());
        setRotation(node.getGlobalRotation());
    }

    @Override
    public void dispose() {
        node.removeEventListener(this, SceneEvent.AddToScene.class);
        node.removeEventListener(this, SceneEvent.RemoveFromScene.class);
        removeFromParent();
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    public static DebugBorder create(Node node) {
        DebugBorder debugBorder = new DebugBorder(node);
        if (node.isOnScreen()) {
            D2D2.stage().addChild(debugBorder);
        }
        return debugBorder;
    }
}
