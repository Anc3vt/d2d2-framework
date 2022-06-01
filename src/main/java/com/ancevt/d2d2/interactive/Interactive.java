package com.ancevt.d2d2.interactive;

public interface Interactive {

    void setTabbingEnabled(boolean tabbingEnabled);

    boolean isTabbingEnabled();

    InteractiveArea getInteractiveArea();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    boolean isDragging();

    boolean isHovering();

    void focus();

    boolean isFocused();
}
