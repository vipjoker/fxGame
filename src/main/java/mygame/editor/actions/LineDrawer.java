package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import mygame.Constants;

/**
 * Created by oleh on 3/18/17.
 */
public class LineDrawer extends Drawer {

    Path path;
    LineTo lineTo;
    Circle last ;
    public LineDrawer(Group parent) {
        super(parent);
    }



    @Override
    public void mouseMoved(Point2D pos) {
        if(lineTo == null){
            lineTo = new LineTo(pos.getX(),pos.getY());

            path.getElements().add(lineTo);
        }else{
             if(last != null) {
                 last.setCenterX(pos.getX());
                 last.setCenterY(pos.getY());
             }
            lineTo.setX(pos.getX());
            lineTo.setY(pos.getY());
        }
    }

    @Override
    public void mousePressed(Point2D pos) {

        if (path == null) {
            path = new Path(new MoveTo(pos.getX(), pos.getY()));
            getHandler(pos);
            path.setStroke(Constants.RED);
            path.setStrokeWidth(2);
            ((Group) parent).getChildren().add(path);
        }else {
           last = getHandler(pos);
            lineTo = new LineTo(pos.getX(),pos.getY());
            path.getElements().add(lineTo);

        }

    }

    @Override
    public void mouseReleased(Point2D pos) {

    }

    @Override
    public void finishDrawing() {

        path = null;
        lineTo = null;
    }
}
