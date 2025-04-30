package com.ancevt.d2d2.event;

import com.ancevt.d2d2.scene.Container;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

public abstract class SceneEvent<S> extends Event<S> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class LoopUpdate<S> extends SceneEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class ExitFrame<S> extends SceneEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class EnterFrame<S> extends SceneEvent<S> {
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class Add<S> extends SceneEvent<S> {
        private Container parent;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    @Getter
    @Accessors(chain = true, fluent = true)
    static final class Remove<S> extends SceneEvent<S> {
        private Container parent;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class AddToScene<S> extends SceneEvent<S> {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE, staticName = "create")
    static final class RemoveFromScene<S> extends SceneEvent<S> {
    }
}




