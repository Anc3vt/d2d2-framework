package com.ancevt.d2d2.input;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.EventDispatcher;
import com.ancevt.d2d2.event.InteractiveEvent;
import lombok.Getter;

import java.util.function.Consumer;

public class KeyHandler {

    private final int keyCode;
    private final EventDispatcher eventDispatcher;
    private final int keyAlias;
    public Consumer<Options> onKeyDown;
    public Consumer<Options> onKeyUp;
    public Consumer<Options> onKeyRepeat;
    public Consumer<Options> onKeyType;

    @Getter
    private boolean enabled;

    public KeyHandler(int keyCode, EventDispatcher eventDispatcher) {
        this(keyCode, eventDispatcher, 0);
    }

    public KeyHandler(int keyCode, EventDispatcher eventDispatcher, int keyAlias) {
        this.keyCode = keyCode;
        this.eventDispatcher = eventDispatcher;
        this.keyAlias = keyAlias;
    }

    public void setEnabled(boolean enabled) {
        if (enabled == this.enabled) return;
        this.enabled = enabled;

        if (enabled) {
            eventDispatcher.addEventListener(this, InteractiveEvent.KEY_DOWN, event -> {
                InteractiveEvent e = event.casted();
                if ((e.getKeyCode() == keyCode || e.getKeyCode() == keyAlias) && onKeyDown != null) {
                    onKeyDown.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent.KEY_UP, event -> {
                InteractiveEvent e = event.casted();
                if ((e.getKeyCode() == keyCode || e.getKeyCode() == keyAlias) && onKeyUp != null) {
                    onKeyUp.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent.KEY_REPEAT, event -> {
                InteractiveEvent e = event.casted();
                if ((e.getKeyCode() == keyCode || e.getKeyCode() == keyAlias) && onKeyRepeat != null) {
                    onKeyRepeat.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent.KEY_TYPE, event -> {
                InteractiveEvent e = event.casted();
                if ((e.getKeyCode() == keyCode || e.getKeyCode() == keyAlias) && onKeyType != null) {
                    onKeyType.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
        } else {
            eventDispatcher.removeEventListener(this, InteractiveEvent.KEY_DOWN);
            eventDispatcher.removeEventListener(this, InteractiveEvent.KEY_UP);
            eventDispatcher.removeEventListener(this, InteractiveEvent.KEY_REPEAT);
            eventDispatcher.removeEventListener(this, InteractiveEvent.KEY_TYPE);
        }
    }

    public KeyHandler registerOnKeyDown(Consumer<Options> fn) {
        onKeyDown = fn;
        return this;
    }
    public KeyHandler registerOnKeyUp(Consumer<Options> fn) {
        onKeyUp = fn;
        return this;
    }
    public KeyHandler registerOnKeyRepeat(Consumer<Options> fn) {
        onKeyRepeat = fn;
        return this;
    }
    public KeyHandler registerOnKeyType(Consumer<Options> fn) {
        onKeyType = fn;
        return this;
    }

    public KeyHandler registerOkKeyRepeatAsOnKeyDown() {
        onKeyRepeat = onKeyDown;
        return this;
    }

    public record Options(
        boolean shift,
        boolean control,
        boolean alt,
        int keyCode,
        char character,
        String keyType
    ) {
    }

}
