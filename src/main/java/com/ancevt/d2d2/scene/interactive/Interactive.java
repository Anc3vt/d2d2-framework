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

package com.ancevt.d2d2.scene.interactive;

import com.ancevt.d2d2.common.Disposable;
import com.ancevt.d2d2.event.InputEvent;
import com.ancevt.d2d2.event.core.EventLink;
import com.ancevt.d2d2.event.core.EventListener;
import com.ancevt.d2d2.scene.Node;
import com.ancevt.d2d2.scene.shape.FreeShape;

public interface Interactive extends Node, Disposable {

    void setPushEventsUp(boolean pushEventUp);

    boolean isPushEventsUp();

    void setTabbingEnabled(boolean tabbingEnabled);

    boolean isTabbingEnabled();

    InteractiveArea getInteractiveArea();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    void setDragging(boolean dragging);

    boolean isDragging();

    void setHovering(boolean hovering);

    boolean isHovering();

    void focus();

    boolean isFocused();

    default void unfocus() {
        if (isFocused()) {
            InteractiveManager.getInstance().resetFocus();
        }
    }

    void setInteractiveFreeShape(FreeShape freeShape);

    FreeShape getInteractiveFreeShape();

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.MouseDown> onMouseDown(EventListener<InputEvent.MouseDown> listener) {
        return (EventLink<InputEvent.MouseDown>) on(InputEvent.MouseDown.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.MouseUp> onMouseUp(EventListener<InputEvent.MouseUp> listener) {
        return (EventLink<InputEvent.MouseUp>) on(InputEvent.MouseUp.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.MouseMove> onMouseMove(EventListener<InputEvent.MouseMove> listener) {
        return (EventLink<InputEvent.MouseMove>) on(InputEvent.MouseMove.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.MouseWheel> onMouseWheel(EventListener<InputEvent.MouseWheel> listener) {
        return (EventLink<InputEvent.MouseWheel>) on(InputEvent.MouseWheel.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.MouseHover> onMouseHover(EventListener<InputEvent.MouseHover> listener) {
        return (EventLink<InputEvent.MouseHover>) on(InputEvent.MouseHover.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.MouseOut> onMouseOut(EventListener<InputEvent.MouseOut> listener) {
        return (EventLink<InputEvent.MouseOut>) on(InputEvent.MouseOut.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.MouseDrag> onMouseDrag(EventListener<InputEvent.MouseDrag> listener) {
        return (EventLink<InputEvent.MouseDrag>) on(InputEvent.MouseDrag.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.KeyDown> onKeyDown(EventListener<InputEvent.KeyDown> listener) {
        return (EventLink<InputEvent.KeyDown>) on(InputEvent.KeyDown.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.KeyRepeat> onKeyRepeat(EventListener<InputEvent.KeyRepeat> listener) {
        return (EventLink<InputEvent.KeyRepeat>) on(InputEvent.KeyRepeat.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.KeyUp> onKeyUp(EventListener<InputEvent.KeyUp> listener) {
        return (EventLink<InputEvent.KeyUp>) on(InputEvent.KeyUp.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.KeyType> onKeyType(EventListener<InputEvent.KeyType> listener) {
        return (EventLink<InputEvent.KeyType>) on(InputEvent.KeyType.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.FocusIn> onFocusIn(EventListener<InputEvent.FocusIn> listener) {
        return (EventLink<InputEvent.FocusIn>) on(InputEvent.FocusIn.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventLink<InputEvent.FocusOut> onFocusOut(EventListener<InputEvent.FocusOut> listener) {
        return (EventLink<InputEvent.FocusOut>) on(InputEvent.FocusOut.class, listener);
    }

    default EventLink<InputEvent.MouseUp> onClick(EventListener<InputEvent.MouseUp> listener) {
        return onMouseUp(e -> {

            System.out.println("left: " + e.isLeft());

            if (e.isOnArea()) {
                listener.onEvent(e);
            }
        });
    }
}
