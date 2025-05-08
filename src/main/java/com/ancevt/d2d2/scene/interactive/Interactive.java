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
import com.ancevt.d2d2.event.core.EventHandleRegistration;
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
    default EventHandleRegistration<InputEvent.MouseDown> onMouseDown(EventListener<InputEvent.MouseDown> listener) {
        return (EventHandleRegistration<InputEvent.MouseDown>) on(InputEvent.MouseDown.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.MouseUp> onMouseUp(EventListener<InputEvent.MouseUp> listener) {
        return (EventHandleRegistration<InputEvent.MouseUp>) on(InputEvent.MouseUp.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.MouseMove> onMouseMove(EventListener<InputEvent.MouseMove> listener) {
        return (EventHandleRegistration<InputEvent.MouseMove>) on(InputEvent.MouseMove.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.MouseWheel> onMouseWheel(EventListener<InputEvent.MouseWheel> listener) {
        return (EventHandleRegistration<InputEvent.MouseWheel>) on(InputEvent.MouseWheel.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.MouseHover> onMouseHover(EventListener<InputEvent.MouseHover> listener) {
        return (EventHandleRegistration<InputEvent.MouseHover>) on(InputEvent.MouseHover.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.MouseOut> onMouseOut(EventListener<InputEvent.MouseOut> listener) {
        return (EventHandleRegistration<InputEvent.MouseOut>) on(InputEvent.MouseOut.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.MouseDrag> onMouseDrag(EventListener<InputEvent.MouseDrag> listener) {
        return (EventHandleRegistration<InputEvent.MouseDrag>) on(InputEvent.MouseDrag.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.KeyDown> onKeyDown(EventListener<InputEvent.KeyDown> listener) {
        return (EventHandleRegistration<InputEvent.KeyDown>) on(InputEvent.KeyDown.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.KeyRepeat> onKeyRepeat(EventListener<InputEvent.KeyRepeat> listener) {
        return (EventHandleRegistration<InputEvent.KeyRepeat>) on(InputEvent.KeyRepeat.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.KeyUp> onKeyUp(EventListener<InputEvent.KeyUp> listener) {
        return (EventHandleRegistration<InputEvent.KeyUp>) on(InputEvent.KeyUp.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.KeyType> onKeyType(EventListener<InputEvent.KeyType> listener) {
        return (EventHandleRegistration<InputEvent.KeyType>) on(InputEvent.KeyType.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.FocusIn> onFocusIn(EventListener<InputEvent.FocusIn> listener) {
        return (EventHandleRegistration<InputEvent.FocusIn>) on(InputEvent.FocusIn.class, listener);
    }

    @SuppressWarnings("unchecked")
    default EventHandleRegistration<InputEvent.FocusOut> onFocusOut(EventListener<InputEvent.FocusOut> listener) {
        return (EventHandleRegistration<InputEvent.FocusOut>) on(InputEvent.FocusOut.class, listener);
    }

    default EventHandleRegistration<InputEvent.MouseUp> onClick(EventListener<InputEvent.MouseUp> listener) {
        return onMouseUp(e -> {

            System.out.println("left: " + e.left());

            if (e.onArea()) {
                listener.onEvent(e);
            }
        });
    }
}
