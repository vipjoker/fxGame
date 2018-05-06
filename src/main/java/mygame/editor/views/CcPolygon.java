package mygame.editor.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import mygame.editor.model.Point;

public class CcPolygon extends CcNode{
    private final PolygonShape mShape;

    public CcPolygon(PolygonShape shape){
        this.mShape = shape;
    }

    @Override
    public void rasterize(GraphicsContext context) {
        int size = mShape.getVertexCount();
        double[] xPoints = new double[size];
        double[] yPoints = new double[size];
        for (int index = 0; index < size; index++) {
            Vector2 point = new Vector2();
            mShape.getVertex(index,point);
            xPoints[index] = point.x * 32;
            yPoints[index] =- point.y * 32;
        }

        context.setFill(Color.RED.deriveColor(1,1,1,0.3));
        context.fillPolygon(xPoints,yPoints,size);
        context.fill();

    }
}
