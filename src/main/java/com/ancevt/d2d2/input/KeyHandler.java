package com.ancevt.d2d2.input;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.EventDispatcher;
import com.ancevt.d2d2.event.InteractiveEvent;
import lombok.Getter;

import java.util.function.Consumer;

public class KeyHandler {

    private final int keyCode;
    private final EventDispatcher eventDispatcher;
    public Consumer<Options> onKeyDown;
    public Consumer<Options> onKeyUp;
    public Consumer<Options> onKeyRepeat;
    public Consumer<Options> onKeyType;

    @Getter
    private boolean enabled;

    public KeyHandler(int keyCode) {
        this(keyCode, D2D2.stage());
    }

    public KeyHandler(int keyCode, EventDispatcher eventDispatcher) {
        this.keyCode = keyCode;
        this.eventDispatcher = eventDispatcher;
        setEnabled(true);
    }

    public void setEnabled(boolean enabled) {
        if (enabled == this.enabled) return;
        this.enabled = enabled;

        if (enabled) {
            eventDispatcher.addEventListener(this, InteractiveEvent.KEY_DOWN, event -> {
                InteractiveEvent e = event.casted();
                if (e.getKeyCode() == keyCode && onKeyDown != null) {
                    onKeyDown.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent.KEY_UP, event -> {
                InteractiveEvent e = event.casted();
                if (e.getKeyCode() == keyCode && onKeyUp != null) {
                    onKeyUp.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent.KEY_REPEAT, event -> {
                InteractiveEvent e = event.casted();
                if (e.getKeyCode() == keyCode && onKeyRepeat != null) {
                    onKeyRepeat.accept(new Options(e.isShift(), e.isControl(), e.isAlt(), e.getKeyCode(), e.getCharacter(), e.getKeyType()));
                }
            });
            eventDispatcher.addEventListener(this, InteractiveEvent.KEY_TYPE, event -> {
                InteractiveEvent e = event.casted();
                if (e.getKeyCode() == keyCode && onKeyType != null) {
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

    public record Options(
        boolean shift,
        boolean control,
        boolean alt,
        int keyCode,
        char character,
        String keyType
    ) {
    }

    public static void main(String[] args) {

        KeyHandler kh = new KeyHandler(KeyCode.D);
        kh.onKeyDown = options -> {
            System.out.println("hk down " + options);
        };
        kh.onKeyUp = options -> {
            System.out.println("hk up " + options);
        };
        kh.onKeyRepeat = options -> {
            System.out.println("hk repeat " + options);
        };
        kh.onKeyType = options -> {
            System.out.println("hk type " + options);
        };

    }
}
