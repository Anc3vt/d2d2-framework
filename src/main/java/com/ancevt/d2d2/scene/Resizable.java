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
import com.ancevt.d2d2.event.core.EventLink;
import com.ancevt.d2d2.event.core.EventListener;

public interface Resizable extends Node {
    void setWidth(float value);

    void setHeight(float value);

    default void setSize(float width, float height) {
        setWidth(width);
        setHeight(height);
    }

    default void setSizeAs(Node node) {
        setSize(node.getWidth(), node.getHeight());
    }

    @SuppressWarnings("unchecked")
    default EventLink<CommonEvent.Resize> onResize(EventListener<CommonEvent.Resize> listener) {
        return on(CommonEvent.Resize.class, listener);
    }
}
