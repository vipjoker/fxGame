package mygame.editor.actions;

import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import mygame.Constants;

/**
 * Created by oleh on 3/18/17.
 */
public class CircleDrawer extends Drawer {


    Point2D center;
    Circle circle;
    public CircleDrawer(Group parent) {
        super(parent);
    }

    @Override
    public void mouseMoved(Point2D pos) {
        if(circle == null){
            circle = new Circle(pos.getX(),pos.getY(),center.distance(pos), Constants.GREEN.deriveColor(1,1,1,.25));
            circle.setStroke(Constants.GREEN);
            parent.getChildren().add(circle);

        }else{
            circle.setRadius(center.distance(pos));
        }
    }

    @Override
    public void mousePressed(Point2D pos) {
        if(center == null){
            center = pos;
        }

    }

    @Override
    public void mouseReleased(Point2D pos) {
        circle = null;
        center = null;
    }

    @Override
    public void finishDrawing() {

    }
}
