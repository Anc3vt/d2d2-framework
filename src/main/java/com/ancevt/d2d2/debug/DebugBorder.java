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
import com.ancevt.d2d2.common.IDisposable;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Container;
import com.ancevt.d2d2.display.IColored;
import com.ancevt.d2d2.display.IDisplayObject;
import com.ancevt.d2d2.display.IResizable;
import com.ancevt.d2d2.display.text.BitmapText;
import com.ancevt.d2d2.event.Event;

public class DebugBorder extends Container implements IResizable, IColored, IDisposable {

    private final BorderedRect borderedRect;
    private final IDisplayObject displayObject;
    private final BitmapText label;

    private boolean disposed;

    private DebugBorder(IDisplayObject displayObject) {
        Color color = Color.createVisibleRandomColor();

        this.displayObject = displayObject;
        borderedRect = new BorderedRect(displayObject.getWidth(),
            displayObject.getHeight(),
            Color.NO_COLOR,
            color
        );

        add(borderedRect);

        label = new BitmapText();
        label.setText(displayObject.getDisplayObjectId() + " " + displayObject.getName());
        label.setAutosize(true);
        add(label, 2, -label.getHeight());

        displayObject.addEventListener(this, Event.ADD_TO_STAGE, this::displayObject_addToStage);
        displayObject.addEventListener(this, Event.REMOVE_FROM_STAGE, this::displayObject_removeFromStage);
    }

    private void displayObject_removeFromStage(Event event) {
        removeFromParent();
    }

    private void displayObject_addToStage(Event event) {
        D2D2.stage().add(this);
    }

    @Override
    public void setSize(float width, float height) {
        borderedRect.setSize(width, height);
    }

    @Override
    public void setWidth(float value) {
        borderedRect.setWidth(value);
    }

    @Override
    public void setHeight(float value) {
        borderedRect.setHeight(value);
    }

    @Override
    public void setColor(Color color) {
        borderedRect.setBorderColor(color);
        label.setColor(color);
    }

    @Override
    public Color getColor() {
        return borderedRect.getBorderColor();
    }

    @Override
    public void onEnterFrame() {
        setXY(displayObject.getAbsoluteX(), displayObject.getAbsoluteY());
        setSize(displayObject.getWidth(), displayObject.getHeight());
        setScale(displayObject.getAbsoluteScaleX(), displayObject.getAbsoluteScaleY());
        setRotation(displayObject.getAbsoluteRotation());
    }

    @Override
    public void dispose() {
        displayObject.removeEventListener(this, Event.ADD_TO_STAGE);
        displayObject.removeEventListener(this, Event.REMOVE_FROM_STAGE);
        removeFromParent();
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    public static DebugBorder create(IDisplayObject displayObject) {
        DebugBorder debugBorder = new DebugBorder(displayObject);
        if (displayObject.isOnScreen()) {
            D2D2.stage().add(debugBorder);
        }
        return debugBorder;
    }
}
