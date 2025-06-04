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

package com.ancevt.d2d2.scene;

import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.NodeEvent;
import com.ancevt.d2d2.event.StageEvent;
import com.ancevt.d2d2.event.core.EventLink;
import com.ancevt.d2d2.event.core.EventListener;
import lombok.Getter;

public class Stage extends BasicGroup implements Resizable {
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;

    @Getter
    private float width;

    @Getter
    private float height;

    @Getter
    private Color backgroundColor;

    public Stage() {
        setName("_" + getClass().getSimpleName() + getNodeId());
        setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundColor(int rgb) {
        setBackgroundColor(Color.of(rgb));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name=" + getName() +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        dispatchEvent(CommonEvent.Resize.create(width, height));
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        dispatchEvent(CommonEvent.Resize.create(width, height));
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        dispatchEvent(CommonEvent.Resize.create(width, height));
    }

    static void dispatchAddToStage(Node node) {
        if (node.isOnScreen()) {
            node.dispatchEvent(NodeEvent.AddToScene.create());
            if (node instanceof Group group) {
                for (int i = 0; i < group.getNumChildren(); i++) {
                    dispatchAddToStage(group.getChild(i));
                }
            }
        }
    }

    static void dispatchRemoveFromStage(Node node) {
        if (node.isOnScreen()) {
            node.dispatchEvent(NodeEvent.RemoveFromScene.create());

            if (node instanceof Group group) {
                for (int i = 0; i < group.getNumChildren(); i++) {
                    dispatchRemoveFromStage(group.getChild(i));
                }
            }
        }
    }

    public EventLink<CommonEvent.Start> onStart(EventListener<CommonEvent.Start> listener) {
        return on(CommonEvent.Start.class, listener);
    }

    public EventLink<CommonEvent.Stop> onStop(EventListener<CommonEvent.Stop> listener) {
        return on(CommonEvent.Stop.class, listener);
    }

    public EventLink<StageEvent.Tick> onTick(EventListener<StageEvent.Tick> listener) {
        return on(StageEvent.Tick.class, listener);
    }


    public EventLink<StageEvent.PreFrame> onPreFrame(EventListener<StageEvent.PreFrame> listener) {
        return on(StageEvent.PreFrame.class, listener);
    }


    public EventLink<StageEvent.PostFrame> onPostFrame(EventListener<StageEvent.PostFrame> listener) {
        return on(StageEvent.PostFrame.class, listener);
    }


    public EventLink<InputEvent.MouseDown> onMouseDown(EventListener<InputEvent.MouseDown> listener) {
        return on(InputEvent.MouseDown.class, listener);
    }


    public EventLink<InputEvent.MouseUp> onMouseUp(EventListener<InputEvent.MouseUp> listener) {
        return on(InputEvent.MouseUp.class, listener);
    }


    public EventLink<InputEvent.MouseMove> onMouseMove(EventListener<InputEvent.MouseMove> listener) {
        return on(InputEvent.MouseMove.class, listener);
    }


    public EventLink<InputEvent.MouseWheel> onMouseWheel(EventListener<InputEvent.MouseWheel> listener) {
        return on(InputEvent.MouseWheel.class, listener);
    }

    public EventLink<InputEvent.MouseHover> onMouseHover(EventListener<InputEvent.MouseHover> listener) {
        return on(InputEvent.MouseHover.class, listener);
    }


    public EventLink<InputEvent.MouseOut> onMouseOut(EventListener<InputEvent.MouseOut> listener) {
        return on(InputEvent.MouseOut.class, listener);
    }


    public EventLink<InputEvent.MouseDrag> onMouseDrag(EventListener<InputEvent.MouseDrag> listener) {
        return on(InputEvent.MouseDrag.class, listener);
    }


    public EventLink<InputEvent.KeyDown> onKeyDown(EventListener<InputEvent.KeyDown> listener) {
        return on(InputEvent.KeyDown.class, listener);
    }


    public EventLink<InputEvent.KeyRepeat> onKeyRepeat(EventListener<InputEvent.KeyRepeat> listener) {
        return (EventLink<InputEvent.KeyRepeat>) on(InputEvent.KeyRepeat.class, listener);
    }


    public EventLink<InputEvent.KeyUp> onKeyUp(EventListener<InputEvent.KeyUp> listener) {
        return (EventLink<InputEvent.KeyUp>) on(InputEvent.KeyUp.class, listener);
    }


    public EventLink<InputEvent.KeyType> onKeyType(EventListener<InputEvent.KeyType> listener) {
        return (EventLink<InputEvent.KeyType>) on(InputEvent.KeyType.class, listener);
    }


    public EventLink<InputEvent.FocusIn> onFocusIn(EventListener<InputEvent.FocusIn> listener) {
        return (EventLink<InputEvent.FocusIn>) on(InputEvent.FocusIn.class, listener);
    }


    public EventLink<InputEvent.FocusOut> onFocusOut(EventListener<InputEvent.FocusOut> listener) {
        return (EventLink<InputEvent.FocusOut>) on(InputEvent.FocusOut.class, listener);
    }

    public EventLink<InputEvent.MouseUp> onClick(EventListener<InputEvent.MouseUp> listener) {
        return onMouseUp(listener);
    }
}
