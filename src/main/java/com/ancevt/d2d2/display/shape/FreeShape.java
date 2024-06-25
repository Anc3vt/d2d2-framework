package com.ancevt.d2d2.display.shape;

import com.ancevt.d2d2.display.BaseDisplayObject;
import com.ancevt.d2d2.display.Color;
import com.ancevt.d2d2.display.Colored;
import com.ancevt.d2d2.display.texture.Texture;
import lombok.Getter;
import lombok.Setter;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FreeShape extends BaseDisplayObject implements Shape, Colored {

    private List<Vertex> vertices = new ArrayList<>();

    @Setter
    private Color color = Color.WHITE;

    @Setter
    private Texture texture;
    @Getter
    private float currentX;
    @Getter
    private float currentY;

    @Getter
    private List<Triangle> triangles = new ArrayList<>();


    public void vertex(float x, float y) {
        this.currentX = x;
        this.currentY = y;
        vertices.add(new Vertex(x, y));
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
        return getClass().getSimpleName() + "{" + vertices.size() + "}";
    }

    public void closePath() {
        var begin = vertices.get(0);
        vertex(begin.getX(), begin.getY());
    }

    public FreeShape copy() {
        FreeShape copy = new FreeShape();

        List<Vertex> copyVertices = new ArrayList<>();
        for (Vertex v : vertices) {
            Vertex newVertex = new Vertex(v.getX(), v.getY());
            copyVertices.add(newVertex);
        }
        copy.vertices = copyVertices;

        List<Triangle> copyTriangles = new ArrayList<>();
        for (Triangle t : triangles) {
            Triangle newTriangle = new Triangle(t.getX1(), t.getY1(), t.getX2(), t.getY2(), t.getX3(), t.getY3());
        }
        copy.triangles = copyTriangles;
        return copy;
    }

    public void compile() {
        triangles.clear();

        List<PolygonPoint> polygonPoints = new ArrayList<>();
        for (Vertex vertex : vertices) {
            PolygonPoint polygonPoint = new PolygonPoint(vertex.x, vertex.y);
            polygonPoints.add(polygonPoint);
        }
        Polygon polygon = new Polygon(polygonPoints);

        Poly2Tri.triangulate(polygon);

        List<DelaunayTriangle> result = polygon.getTriangles();
        for (DelaunayTriangle delaunayTriangle : result) {
            triangles.add(
                new Triangle(
                    delaunayTriangle.points[0].getXf(),
                    delaunayTriangle.points[0].getYf(),
                    delaunayTriangle.points[1].getXf(),
                    delaunayTriangle.points[1].getYf(),
                    delaunayTriangle.points[2].getXf(),
                    delaunayTriangle.points[2].getYf()
                )
            );
        }
    }


    /*
        // Prepare input data
        Polygon polygon = new Polygon(Arrays.asList(new PolygonPoint(0, 0, 0),
          new PolygonPoint(10, 0, 1),new PolygonPoint(10, 10, 2),new PolygonPoint(0, 10, 3)));
        // Launch tessellation
        Poly2Tri.triangulate(polygon);
        // Gather triangles
        List<DelaunayTriangle> triangles = polygon.getTriangles();
      }
     */

}
