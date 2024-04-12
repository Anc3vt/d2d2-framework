package com.ancevt.d2d2.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LifecycleEvent extends Event {

    public static final String START = "lifecycleStart";
    public static final String EXIT = "lifecycleExit";
}
