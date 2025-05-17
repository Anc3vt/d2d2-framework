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

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.exception.InteractiveException;
import com.ancevt.d2d2.scene.BasicSprite;
import com.ancevt.d2d2.scene.shape.FreeShape;
import com.ancevt.d2d2.scene.texture.TextureRegion;

public class InteractiveSprite extends BasicSprite implements Interactive {

    private final InteractiveArea interactiveArea;

    private boolean tabbingEnabled;
    private boolean enabled;
    private boolean dragging;
    private boolean hovering;
    private boolean disposed;
    private boolean pushEventUp;

    private FreeShape freeShape;

    public InteractiveSprite() {
        interactiveArea = new InteractiveArea(0, 0, 0, 0);
        enabled = true;
        pushEventUp = true;
        InteractiveManager.getInstance().registerInteractive(this);
        setDefaultName();
    }

    private InteractiveSprite(TextureRegion textureRegion) {
        setTextureRegion(textureRegion);
        interactiveArea = new InteractiveArea(0, 0, textureRegion.getWidth(), textureRegion.getHeight());
        enabled = true;
        pushEventUp = true;
        InteractiveManager.getInstance().registerInteractive(this);
        setDefaultName();
    }

    private InteractiveSprite(String assetPath) {
        this(D2D2.textureManager().loadTexture(assetPath).createTextureRegion());
    }

    public static InteractiveSprite create() {
        return new InteractiveSprite();
    }

    public static InteractiveSprite create(TextureRegion textureRegion) {
        return new InteractiveSprite(textureRegion);
    }

    public static InteractiveSprite load(String assetPathToImage) {
        return new InteractiveSprite(assetPathToImage);
    }

    @Override
    public void setInteractiveFreeShape(FreeShape freeShape) {
        this.freeShape = freeShape;
    }

    public FreeShape getInteractiveFreeShape() {
        return freeShape;
    }

    @Override
    public void setPushEventsUp(boolean pushEventUp) {
        this.pushEventUp = pushEventUp;
    }

    @Override
    public boolean isPushEventsUp() {
        return pushEventUp;
    }

    private void setDefaultName() {
        setName("_" + getClass().getSimpleName() + getNodeId());
    }

    @Override
    public void setX(float value) {
        interactiveArea.setUp(
                value,
                interactiveArea.getY(),
                interactiveArea.getWidth(),
                interactiveArea.getHeight()
        );
        super.setX(value);
    }

    @Override
    public void setY(float value) {
        interactiveArea.setUp(
                interactiveArea.getX(),
                value,
                interactiveArea.getWidth(),
                interactiveArea.getHeight()
        );
        super.setY(value);
    }

    @Override
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
        super.setPosition(x, y);
    }

    @Override
    public void setTextureRegion(TextureRegion value) {
        super.setTextureRegion(value);
        if (interactiveArea != null) {
            interactiveArea.setWidth(getTextureRegion().getWidth());
            interactiveArea.setHeight(getTextureRegion().getHeight());
        }
    }

    @Override
    public void setTabbingEnabled(boolean tabbingEnabled) {
        this.tabbingEnabled = tabbingEnabled;
    }

    @Override
    public boolean isTabbingEnabled() {
        return tabbingEnabled;
    }

    @Override
    public InteractiveArea getInteractiveArea() {
        return interactiveArea;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;

        this.enabled = enabled;

        if (enabled)
            InteractiveManager.getInstance().registerInteractive(this);
        else
            InteractiveManager.getInstance().unregisterInteractive(this);
    }

    @Override
    public void setScaleX(float value) {
        super.setScaleX(value);
        interactiveArea.setWidth(value / getTextureRegion().getWidth());
    }

    @Override
    public void setScaleY(float value) {
        super.setScaleY(value);
        interactiveArea.setHeight(value / getTextureRegion().getHeight());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    @Override
    public boolean isHovering() {
        return hovering;
    }

    @Override
    public void focus() {
        InteractiveManager.getInstance().setFocused(this, false);
    }

    @Override
    public boolean isFocused() {
        return InteractiveManager.getInstance().getFocused() == this;
    }

    @Override
    public void dispose() {
        this.disposed = true;
        InteractiveManager.getInstance().unregisterInteractive(this);
        removeFromParent();
        addEventListener(SceneEvent.Add.class, e -> {
            throw new InteractiveException("Unable to add disposed interactive display object %s".formatted(this.toString()));
        });
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

}
