package com.ancevt.d2d2.event.core;

import com.ancevt.d2d2.time.Timer;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class EventLink<T extends Event> {

    private static final Map<String, ArrayList<EventLink<?>>> TAG_REGISTRY =
            new HashMap<>();

    private final EventDispatcher dispatcher;
    private final Class<T> eventType;
    private final EventListener<T> listener;

    private boolean paused = false;
    private int remaining = -1; // -1 означает бесконечную подписку
    private Consumer<Throwable> errorHandler = Throwable::printStackTrace;
    private String tag;

    public EventLink(EventDispatcher dispatcher,
                     Class<T> eventType,
                     EventListener<T> listener) {
        this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher");
        this.eventType = Objects.requireNonNull(eventType, "eventType");
        this.listener = Objects.requireNonNull(listener);
        dispatcher.addEventListener(this, eventType, this::handle);
    }

    private void handle(T event) {
        if (paused || (remaining == 0)) return;

        if (remaining > 0) remaining--;

        try {
            listener.onEvent(event);
        } catch (Throwable ex) {
            errorHandler.accept(ex);
        }

        if (remaining == 0) unregister();
    }

    public EventLink<T> setPaused(boolean paused) {
        this.paused = paused;
        return this;
    }

    public boolean isPaused() {
        return paused;
    }

    public EventLink<T> once() {
        return autoUnregisterAfter(1);
    }

    public EventLink<T> onError(Consumer<Throwable> handler) {
        this.errorHandler = Objects.requireNonNull(handler, "handler");
        return this;
    }

    public EventLink<T> autoUnregisterAfter(int count) {
        if (count < 1) throw new IllegalArgumentException("count >= 1");
        this.remaining = count;
        return this;
    }

    public EventLink<T> tag(String tag) {
        this.tag = Objects.requireNonNull(tag, "tag");
        TAG_REGISTRY
                .computeIfAbsent(tag, t -> new ArrayList<>())
                .add(this);
        return this;
    }

    public static void unregisterByTag(String tag) {
        List<EventLink<?>> regs = TAG_REGISTRY.remove(tag);
        if (regs != null) {
            regs.forEach(EventLink::unregister);
        }
    }

    public <R> EventLink<T> map(Function<? super T, ? extends R> mapper,
                                EventListener<? super R> newListener) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(newListener);

        EventListener<T> wrappedListener = event -> {
            try {
                R mappedEvent = mapper.apply(event);
                newListener.onEvent(mappedEvent);
            } catch (Throwable ex) {
                errorHandler.accept(ex);
            }
        };

        dispatcher.removeEventListener(this, eventType);
        dispatcher.addEventListener(this, eventType, wrappedListener);
        return this;
    }

    public EventLink<T> withTimeout(long timeoutMillis, Runnable onTimeout) {
        Objects.requireNonNull(onTimeout, "onTimeout");

        // Используем твой таймер вместо потоков
        Timer.setTimeout(timeoutMillis, timer -> {
            unregister();
            try {
                onTimeout.run();
            } catch (Throwable ex) {
                errorHandler.accept(ex);
            }
        });

        return this;
    }

    public void unregister() {
        dispatcher.removeEventListener(this, eventType);
        if (tag != null) {
            TAG_REGISTRY.computeIfPresent(tag, (t, list) -> {
                list.remove(this);
                return list.isEmpty() ? null : list;
            });
        }
    }

    public Class<T> getEventType() {
        return eventType;
    }

    public EventListener<T> getListener() {
        return listener;
    }

    public boolean isRegistered() {
        return !paused && remaining != 0;
    }
}
