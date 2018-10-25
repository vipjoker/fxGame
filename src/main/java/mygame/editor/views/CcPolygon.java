package mygame.editor.views;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.model.Point;
import mygame.editor.model.box2d.B2Fixture;
import physicsPort.triangulation.Vec2;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class CcPolygon extends CcNode {

    private List<Vector2> points = new ArrayList<>();


    public CcPolygon(PolygonShape shape) {

        Vector2 buffer = new Vector2();
        for (int i = 0; i < shape.getVertexCount(); i++) {
            shape.getVertex(i, buffer);
            points.add(new Vector2(buffer.x * 32, -buffer.y * 32));
        }
    }

    public CcPolygon(B2Fixture fixture) {
        final List<Vector2> points = fixture.getPoints();
        for (Vector2 point : points) {
            final Vector2 cpy = point.cpy();
            cpy.y *= -1;
            cpy.scl(32);
            this.points.add(cpy);
        }

    }


    @Override
    public void rasterize(GraphicsContext context) {
        int size = points.size();
        double[] xPoints = new double[size];
        double[] yPoints = new double[size];
        int index = 0;
        for (Vector2 v : points) {

            xPoints[index] = v.x;
            yPoints[index] = v.y;
            index++;
        }

        context.beginPath();
        if(active){
            context.setFill(Color.GREEN.deriveColor(1, 1, 1, 0.3));
        }else {
            context.setFill(Color.RED.deriveColor(1, 1, 1, 0.3));
            context.setStroke(Color.RED);
        }
        context.fillPolygon(xPoints, yPoints, size);
        context.fill();
        context.stroke();

    }

    @Override
    public boolean contains(javafx.geometry.Point2D point2D) {
        return true;
    }

    float sign(Vector2 p1, Vector2 p2, Vector2 p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    boolean triangleContains(Vector2 pt, Vector2 v1, Vector2 v2, Vector2 v3) {
        float d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(pt, v1, v2);
        d2 = sign(pt, v2, v3);
        d3 = sign(pt, v3, v1);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }


    private List<Polygon> triangulatePolygon(List<Vector2> points) {
        List<Polygon> triangles = new ArrayList<>();

        boolean clockwise = isClockwise(points);
        int index = 0;

        while (points.size() > 2) {

            Vector2 p1 = points.get((index + 0) % points.size());
            Vector2 p2 = points.get((index + 1) % points.size());
            Vector2 p3 = points.get((index + 2) % points.size());

            Vector2 v1 = new Vector2(p2.x - p1.x, p2.y - p1.y);
            Vector2 v2 = new Vector2(p3.x - p1.x, p3.y - p1.y);

            double cross = v1.crs(v2);

            Polygon triangle = new Polygon(
                    new float[]
                            {p1.x, p1.y,
                                    p2.x, p2.y,
                                    p3.x, p3.y});



            //System.out.println("cross = " + cross);
            if (!clockwise && cross >= 0 && validTriangle(triangle, p1, p2, p3, points)) {
                points.remove(p2);
                triangles.add(triangle);
            } else if (clockwise && cross <= 0 && validTriangle(triangle, p1, p2, p3, points)) {
                points.remove(p2);
                triangles.add(triangle);
            } else {
                index++;
            }

        }

        if (points.size() < 3) {
            points.clear();
        }
        return triangles;
    }

    public boolean validTriangle(Polygon triangle, Vector2 p1, Vector2 p2, Vector2 p3, List<Vector2> points) {
        for (Vector2 p : points) {
            if (p != p1 && p != p2 && p != p3 && triangle.contains(p)) {
                return false;
            }
        }
        return true;
    }

    public boolean isClockwise(List<Vector2> points) {
        int sum = 0;
        for (int i = 0; i < points.size(); i++) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get((i + 1) % points.size());
            sum += (p2.x - p1.x) * (p2.y + p1.y);
        }
        return sum >= 0;
    }
}
