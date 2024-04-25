package com.ancevt.d2d2.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class WindowState {
    private final long id;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
}
