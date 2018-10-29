package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import mygame.editor.model.Point;
import mygame.editor.model.box2d.B2Fixture;

import java.util.ArrayList;
import java.util.List;

public class CcChain extends CcFixtureNode {

    List<Vector2> points = new ArrayList<>();
    private Path path;

    public CcChain(ChainShape chainShape) {

        Vector2 buffer = new Vector2();
        for (int i = 0; i < chainShape.getVertexCount(); i++) {
            chainShape.getVertex(i, buffer);
            points.add(new Vector2(buffer.x * 32, -buffer.y * 32));
        }


    }

    public CcChain(B2Fixture fixture) {
        for (Vector2 vector2 : fixture.getPoints()) {
            final Vector2 v = vector2.cpy().scl(32);
            v.y *= -1;
            points.add(v);
        }

        path = new Path();
        path.setStrokeWidth(5);
        boolean isFirstTime = true;
        for (Vector2 point : points) {

            if (isFirstTime) {
                isFirstTime = false;
                path.getElements().add(new MoveTo(point.x, -point.y));
            } else {
                path.getElements().add(new LineTo(point.x, -point.y));
            }
        }



    }

    @Override
    public void rasterize(GraphicsContext context) {

        if (active) {
            context.setStroke(Color.RED);
        }else{
            context.setStroke(Color.GREEN);
        }
        context.setLineWidth(3);
        context.beginPath();
        boolean isFirstTime = true;
        for (Vector2 point : points) {

            if (isFirstTime) {
                isFirstTime = false;
                context.moveTo(point.x, point.y);
            } else {
                context.lineTo(point.x, point.y);
            }
        }
        context.stroke();

    }

    @Override
    public boolean contains(Point2D point2D) {
        System.out.println("Contains " + point2D);
        return path.contains(point2D);
    }

    @Override
    public List<Vector2> getPoints() {
        return points;
    }
}
