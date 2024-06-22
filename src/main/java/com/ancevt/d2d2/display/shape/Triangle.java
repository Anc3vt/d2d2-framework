package com.ancevt.d2d2.display.shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public class Triangle {

    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;
    private final float x3;
    private final float y3;

    public boolean contains(Vertex p) {
        return
            (p.x == x1 && p.y == y1) &&
                (p.x == x2 && p.y == y2) &&
                (p.x == x3 && p.y == y3);
    }
}
