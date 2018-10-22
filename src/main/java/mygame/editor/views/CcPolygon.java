package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.model.Point;
import mygame.editor.model.box2d.B2Fixture;

import java.util.ArrayList;
import java.util.List;

public class CcPolygon extends CcNode{

    private List<Vector2> points = new ArrayList<>();


    public CcPolygon(PolygonShape shape){

        Vector2 buffer = new Vector2();
        for (int i = 0; i < shape.getVertexCount(); i++) {
            shape.getVertex(i,buffer);
            points.add(new Vector2(buffer.x * 32,-buffer.y * 32));
        }
    }

    public CcPolygon(B2Fixture fixture){
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
        for (Vector2 v:points) {

            xPoints[index] =v.x;
            yPoints[index] =v.y;
            index++;
        }

        context.setFill(Color.RED.deriveColor(1,1,1,0.3));
        context.fillPolygon(xPoints,yPoints,size);
        context.fill();

    }
}
