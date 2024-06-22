package com.ancevt.d2d2.display.shape;

import com.ancevt.d2d2.display.BaseDisplayObject;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Colored;
import com.ancevt.d2d2.display.texture.TextureAtlas;
import com.ancevt.d2d2.exception.NotImplementedException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FreeShape extends BaseDisplayObject implements Shape, Colored {

    private final List<Vertex> vertexList = new ArrayList<>();

    @Setter
    private Color color = Color.WHITE;

    @Setter
    private TextureAtlas textureAtlas;
    @Getter
    private float currentX;
    @Getter
    private float currentY;

    @Getter
    private final List<Triangle> triangles = new ArrayList<>();


    public void vertex(float x, float y) {
        this.currentX = x;
        this.currentY = y;
        vertexList.add(new Vertex(x, y));
    }

    public void curveTo(float controlX1, float controlY1, float controlX2, float controlY2, float endX, float endY, float step) {
        float t = 0.0f;
        while (t <= 1.0f) {
            float oneMinusT = 1.0f - t;
            float x = oneMinusT * oneMinusT * oneMinusT * currentX +
                3 * oneMinusT * oneMinusT * t * controlX1 +
                3 * oneMinusT * t * t * controlX2 +
                t * t * t * endX;
            float y = oneMinusT * oneMinusT * oneMinusT * currentY +
                3 * oneMinusT * oneMinusT * t * controlY1 +
                3 * oneMinusT * t * t * controlY2 +
                t * t * t * endY;
            vertex(x, y);
            this.currentX = x;
            this.currentY = y;
            t += step;
        }
        // Обновляем currentX и currentY

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + vertexList.size() + "}";
    }

    public void closePath() {
        var begin = vertexList.get(0);
        vertex(begin.getX(), begin.getY());
    }

    public void compile() {
        triangles.clear();


        //TODO:implement
        throw new NotImplementedException();
    }

}
