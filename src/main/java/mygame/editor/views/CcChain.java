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
import mygame.editor.util.Box2dUtil;

import java.util.ArrayList;
import java.util.List;

public class CcChain extends CcFixtureNode {

    private  B2Fixture fixture;
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
        this.fixture = fixture;
        for (Vector2 vector2 : fixture.getPoints()) {
            final Vector2 v = vector2.cpy().scl(32);
            v.y *= -1;
            points.add(v);
        }

        path = initPath();

    }

    @Override
    public B2Fixture getFixture() {
        return fixture;
    }

    private Path initPath(){
       Path path = new Path();
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

        return path;
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

    @Override
    public void update() {

        fixture.getPoints().clear();
        for (Vector2 vector2 : getPoints()) {
            final Vector2 box2dVec = Box2dUtil.toBox2d(vector2);
            fixture.getPoints().add(box2dVec);
        }

        path = initPath();
    }

    @Override
    public void addPoint(Point2D point) {
        final javafx.geometry.Point2D p = convertToLocalSpace(point);
        points.add(new Vector2((float) p.getX(),(float) -p.getY()));

    }
}
