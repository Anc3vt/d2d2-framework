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

package com.ancevt.d2d2.time;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Timer {

    private static final List<Timer> timers = new ArrayList<>();
    private static final List<Timer> timersToRemove = new ArrayList<>();

    @Getter
    private final Consumer<Timer> func;

    @Getter
    private final long delay;

    @Setter
    @Getter
    private boolean loop;

    private long startTime = System.currentTimeMillis();

    private boolean alive;

    public Timer(long delay, Consumer<Timer> func) {
        this.func = func;
        this.delay = delay;
    }

    public void start() {
        alive = true;
        startTime = System.currentTimeMillis();
        synchronized (timers) {
            if (!timers.contains(this)) {
                timers.add(this);
            }
        }
    }

    public void stop() {
        alive = false;
        startTime = 0;
        synchronized (timersToRemove) {
            if (!timersToRemove.contains(this)) {
                timersToRemove.add(this);
            }
        }
    }

    public static void cancelIf(Predicate<Timer> condition) {
        timers.stream()
                .filter(condition)
                .forEach(Timer::stop);
    }

    public boolean isStarted() {
        return timers.contains(this);
    }

    public static Timer setInterval(long delay, Consumer<Timer> func) {
        Timer timer = new Timer(delay, func);
        timer.setLoop(true);
        timers.add(timer);
        timer.start();
        return timer;
    }

    public static Timer setTimeout(long delay, Consumer<Timer> func) {
        Timer timer = new Timer(delay, func);
        timer.setLoop(false);
        timers.add(timer);
        timer.start();
        return timer;
    }

    public static void clearAllTimers() {
        timers.clear();
    }

    public static void processTimers() {
        synchronized (timers) {
            long currentTime = System.currentTimeMillis();
            for (Timer timer : new ArrayList<>(timers)) { // копия на случай stop() внутри func
                if (currentTime - timer.startTime >= timer.delay) {
                    if (timer.alive) {
                        timer.func.accept(timer);
                    }

                    if (timer.loop) {
                        timer.startTime = currentTime;
                    } else {
                        timer.stop();
                    }
                }
            }
        }

        synchronized (timersToRemove) {
            for (Timer timer : timersToRemove) {
                synchronized (timers) {
                    timers.remove(timer);
                }
            }
            timersToRemove.clear();
        }
    }

}
