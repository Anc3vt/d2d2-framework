/**
 * Copyright (C) 2024 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.d2d2.time;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class Timer {

    private static final List<Timer> timers = new CopyOnWriteArrayList<>();
    private static final List<Timer> timersToRemove = new ArrayList<>();

    @Getter
    private final Consumer<Timer> func;

    @Getter
    private final long delay;

    @Setter
    @Getter
    private boolean loop;

    private long startTime = System.currentTimeMillis();

    public Timer(Consumer<Timer> func, long delay) {
        this.func = func;
        this.delay = delay;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        timers.add(this);
    }

    public void stop() {
        startTime = 0;
        timersToRemove.add(this);
    }

    public boolean isStarted() {
        return timers.contains(this);
    }

    public static Timer setInterval(Consumer<Timer> func, long delay) {
        Timer timer = new Timer(func, delay);
        timer.setLoop(true);
        timers.add(timer);
        return timer;
    }

    public static Timer setTimeout(Consumer<Timer> func, long delay) {
        Timer timer = new Timer(func, delay);
        timer.setLoop(false);
        timers.add(timer);
        return timer;
    }

    public static void clearAllTimers() {
        timers.clear();
    }

    public static void processTimers() {
        timers.forEach(timer -> {
            long delay = timer.delay;
            long currentTime = System.currentTimeMillis();
            if (currentTime - timer.startTime >= delay) {
                timer.func.accept(timer);

                if (timer.isLoop()) {
                    timer.startTime = currentTime;
                } else {
                    timer.stop();
                }
            }
        });

        while (!timersToRemove.isEmpty()) {
            timers.remove(timersToRemove.remove(0));
        }
    }
}
