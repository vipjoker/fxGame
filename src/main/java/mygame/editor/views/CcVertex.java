package mygame.editor.views;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CcVertex extends CcNode {
    private final Vector2 point;

    public CcVertex(Vector2 point) {
        this.point = point;
        this.x = point.x;
        this.y = -point.y;
    }




    @Override
    public void rasterize(GraphicsContext context) {
        if(active) {
            context.setFill(Color.RED);
        }else{
            context.setFill(Color.CYAN);
        }
        context.beginPath();

        context.fillRect( - 5,   -5, 10, 10);

        context.beginPath();



        context.stroke();
    }

    @Override
    public void move(Point2D point2D) {
        super.move(point2D);
        point.set((float) x,(float)-y);
    }

    @Override
    public boolean contains(Point2D point2D) {

        final Rectangle rectangle = new Rectangle((float)  - 5, (float) -5, 10, 10);
        //System.out.println("point: " + point2D + " rect: " + rectangle);
        return rectangle.contains((float) point2D.getX(),(float) point2D.getY());
    }
}
