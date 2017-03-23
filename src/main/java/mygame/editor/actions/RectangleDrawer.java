package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mygame.Constants;
import mygame.editor.model.AbstractModel;
import mygame.editor.views.CustomRegion;

import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class RectangleDrawer extends Action {

    public RectangleDrawer(CustomRegion parent, List<AbstractModel> models) {
        super(parent,models);
    }


    Point2D startPoint;
    Rectangle rectangle;


    @Override
    public void mouseMoved(Point2D pos) {

        if (startPoint != null) {
            if (rectangle == null) {


                rectangle = new Rectangle(startPoint.getX() , startPoint.getY() , pos.getX() - startPoint.getX() , pos.getY() - startPoint.getY() );
                rectangle.setStroke(Constants.RED);
                rectangle.setStrokeWidth(2);
                rectangle.setFill(Color.RED.deriveColor(1,1,1,.25));




                parent.addChild(rectangle);
            } else {
                if (pos.getX() > startPoint.getX()) rectangle.setWidth(pos.getX() -startPoint.getX());
                else {
                    rectangle.setWidth(startPoint.getX() - pos.getX());
                    rectangle.setX(pos.getX() );
                }

                if (pos.getY() > startPoint.getY()) rectangle.setHeight(pos.getY() - startPoint.getY());
                else {
                    rectangle.setHeight(startPoint.getY() - pos.getY());
                    rectangle.setY(pos.getY() );
                }

            }
        }


    }



    @Override
    public void mousePressed(Point2D position) {

        if (startPoint == null) startPoint = position;
    }

    @Override
    public void mouseReleased(Point2D position) {

        setColorListeners(rectangle);

        startPoint = null;
        rectangle = null;


    }

    @Override
    public void finishDrawing() {

    }
}
