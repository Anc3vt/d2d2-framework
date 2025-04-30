package com.ancevt.d2d2.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public abstract class CommonEvent<S> extends Event<S> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class Start<S> extends CommonEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class Change<S> extends CommonEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class Complete<S> extends CommonEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class Action<S> extends CommonEvent<S> {
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class Resize<S> extends CommonEvent<S> {
        private int width;
        private int height;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class Error<S> extends CommonEvent<S> {
        private Throwable throwable;
    }
}
