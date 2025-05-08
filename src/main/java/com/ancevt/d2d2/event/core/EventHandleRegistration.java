package com.ancevt.d2d2.event.core;

import lombok.Getter;

@Getter
public class EventHandleRegistration<T extends Event> {

    private final EventDispatcher dispatcher;

    private final Class<T> eventType;

    private final EventListener<T> listener;

    EventHandleRegistration(EventDispatcher dispatcher, Class<T> eventType, EventListener<T> listener) {
        this.dispatcher = dispatcher;
        this.eventType = eventType;
        this.listener = listener;

        dispatcher.addEventListener(this, eventType, listener);
    }

    public void once() {
        unregister();

        dispatcher.addEventListener(this, eventType, event -> {
            listener.onEvent(event);
            unregister();
        });
    }


    public void unregister() {
        dispatcher.removeEventListener(this, eventType);
    }
}
