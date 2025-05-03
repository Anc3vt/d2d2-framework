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
import com.ancevt.d2d2.event.dispatch.Event;
import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.scene.*;
import com.ancevt.d2d2.scene.shape.BorderedRectangle;
import com.ancevt.d2d2.scene.text.Text;

public class DebugBorder extends ContainerImpl implements Resizable, Colored, Disposable {

    private final BorderedRectangle borderedRectangle;
    private final SceneEntity sceneEntity;
    private final Text label;

    private boolean disposed;

    private DebugBorder(SceneEntity sceneEntity) {
        Color color = Color.createVisibleRandomColor();

        this.sceneEntity = sceneEntity;
        borderedRectangle = new BorderedRectangle(sceneEntity.getWidth(),
                sceneEntity.getHeight(),
                Color.NO_COLOR,
                color
        );

        addChild(borderedRectangle);

        label = new Text();
        label.setText(sceneEntity.getDisplayObjectId() + " " + sceneEntity.getName());
        label.setAutosize(true);
        addChild(label, 2, -label.getHeight());

        sceneEntity.addEventListener(this, SceneEvent.AddToScene.class, this::displayObject_addToStage);
        sceneEntity.addEventListener(this, SceneEvent.RemoveFromScene.class, this::displayObject_removeFromStage);
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
    public void onEnterFrame() {
        setXY(sceneEntity.getAbsoluteX(), sceneEntity.getAbsoluteY());
        setSize(sceneEntity.getWidth(), sceneEntity.getHeight());
        setScale(sceneEntity.getAbsoluteScaleX(), sceneEntity.getAbsoluteScaleY());
        setRotation(sceneEntity.getAbsoluteRotation());
    }

    @Override
    public void dispose() {
        sceneEntity.removeEventListener(this, SceneEvent.AddToScene.class);
        sceneEntity.removeEventListener(this, SceneEvent.RemoveFromScene.class);
        removeFromParent();
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    public static DebugBorder create(SceneEntity sceneEntity) {
        DebugBorder debugBorder = new DebugBorder(sceneEntity);
        if (sceneEntity.isOnScreen()) {
            D2D2.stage().addChild(debugBorder);
        }
        return debugBorder;
    }
}
