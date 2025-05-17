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

package com.ancevt.d2d2.motion;

import com.ancevt.d2d2.D2D2;
import com.ancevt.d2d2.event.CommonEvent;
import com.ancevt.d2d2.event.core.Event;
import com.ancevt.d2d2.event.SceneEvent;
import com.ancevt.d2d2.event.core.EventDispatcherImpl;
import lombok.Getter;

import java.util.function.Consumer;

import static com.ancevt.d2d2.motion.Motion.State.*;

public class Motion<N extends Number> extends EventDispatcherImpl {

    @Getter
    private final N beginValue;
    @Getter
    private final N endValue;
    @Getter
    private final N step;
    @Getter
    private State state;
    private final Consumer<N> applyFunction;
    private N currentValue;
    private final NumberOperations<N> numberOperations;

    public Motion(N beginValue, N endValue, N step, Consumer<N> applyFunction) {
        this.beginValue = beginValue;
        this.endValue = endValue;
        this.step = step;
        this.applyFunction = applyFunction;
        state = NEW;

        if (beginValue instanceof Integer) {
            numberOperations = (NumberOperations<N>) new IntegerOperations();
        } else if (beginValue instanceof Float) {
            numberOperations = (NumberOperations<N>) new FloatOperations();
        } else if (beginValue instanceof Double) {
            numberOperations = (NumberOperations<N>) new DoubleOperations();
        } else {
            throw new IllegalArgumentException("type not supported");
        }
    }

    public void start() {
        D2D2.root().removeEventListener(this, SceneEvent.Tick.class);
        D2D2.root().addEventListener(this, SceneEvent.Tick.class, this::stage_loopUpdate);
        dispatchEvent(CommonEvent.Start.create());
        state = IN_PROCESS;
    }

    public void stop() {
        D2D2.root().removeEventListener(this, SceneEvent.Tick.class);
    }

    private void stage_loopUpdate(Event event) {
        process();
    }

    private void process() {
        if (currentValue == null) {
            currentValue = beginValue;
        }

        if (numberOperations.lessThan(currentValue, endValue)) {
            currentValue = numberOperations.add(currentValue, step);

            if (numberOperations.greaterThanOrEqual(currentValue, endValue)) {
                currentValue = endValue;
                complete();
            }

        } else if (numberOperations.greaterThan(currentValue, endValue)) {
            currentValue = numberOperations.subtract(currentValue, step);

            if (numberOperations.lessThanOrEqual(currentValue, endValue)) {
                currentValue = endValue;
                complete();
            }
        }

        applyFunction.accept(currentValue);
    }

    private void complete() {
        stop();
        state = COMPLETE;
        dispatchEvent(CommonEvent.Complete.create());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "beginValue=" + beginValue +
                ", endValue=" + endValue +
                ", step=" + step +
                ", currentValue=" + currentValue +
                ", state=" + state +
                '}';
    }

    enum State {
        NEW,
        IN_PROCESS,
        COMPLETE
    }
}
