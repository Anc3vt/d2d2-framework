package com.ancevt.d2d2.event.dispatch;

import com.ancevt.d2d2.event.Event;

public interface EventDispatcher<S> {

    <T extends Event<S>> void addEventListener(Class<T> eventType, EventListener<T> listener);

    <T extends Event<S>> void addEventListener(Object key, Class<T> eventType, EventListener<T> listener);

    <T extends Event<S>> void removeEventListener(Class<T> eventType, EventListener<T> listener);

    void removeAllEventListeners();

    void removeAllEventListenersByKey(Object key);

    <T extends Event<S>> void dispatchEvent(T event);
}
