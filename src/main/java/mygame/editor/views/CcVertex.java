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
    }


    @Override
    public void rasterize(GraphicsContext context) {
        if(active) {
            context.setFill(Color.PINK);
        }else{
            context.setFill(Color.CYAN);
        }

        context.fillRect(point.x - 3, point.y - 3, 6, 6);
    }

    @Override
    public boolean contains(Point2D point2D) {

        final Rectangle rectangle = new Rectangle(point.y - 3, point.y - 3, 6, 6);

        return rectangle.contains((float) point2D.getX(),(float) point2D.getY());
    }
}
