package com.ancevt.d2d2.display.shape;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Vertex {
    public float x;
    public float y;

    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
