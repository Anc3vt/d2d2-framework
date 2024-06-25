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
import com.ancevt.d2d2.display.shape.BorderedRectangle;
import com.ancevt.d2d2.common.Disposable;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.SimpleContainer;
import com.ancevt.d2d2.display.Colored;
import com.ancevt.d2d2.display.DisplayObject;
import com.ancevt.d2d2.display.Resizable;
import com.ancevt.d2d2.display.text.Text;
import com.ancevt.d2d2.event.Event;

public class DebugBorder extends SimpleContainer implements Resizable, Colored, Disposable {

    private final BorderedRectangle borderedRectangle;
    private final DisplayObject displayObject;
    private final Text label;

    private boolean disposed;

    private DebugBorder(DisplayObject displayObject) {
        Color color = Color.createVisibleRandomColor();

        this.displayObject = displayObject;
        borderedRectangle = new BorderedRectangle(displayObject.getWidth(),
            displayObject.getHeight(),
            Color.NO_COLOR,
            color
        );

        addChild(borderedRectangle);

        label = new Text();
        label.setText(displayObject.getDisplayObjectId() + " " + displayObject.getName());
        label.setAutosize(true);
        addChild(label, 2, -label.getHeight());

        displayObject.addEventListener(this, Event.ADD_TO_STAGE, this::displayObject_addToStage);
        displayObject.addEventListener(this, Event.REMOVE_FROM_STAGE, this::displayObject_removeFromStage);
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

    public static DebugBorder create(DisplayObject displayObject) {
        DebugBorder debugBorder = new DebugBorder(displayObject);
        if (displayObject.isOnScreen()) {
            D2D2.stage().addChild(debugBorder);
        }
        return debugBorder;
    }
}
