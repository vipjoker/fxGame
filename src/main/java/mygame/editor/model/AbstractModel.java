package mygame.editor.model;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import mygame.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/21/17.
 */
public class AbstractModel extends Group {

    protected List<Circle> controlls;

    protected Circle getHandler(Point2D point) {
        Circle circle = new Circle(point.getX(), point.getY(), 5);
        circle.setStroke(Constants.WHITE);
        circle.setFill(Constants.LIGHT_GREY);
        return circle;
    }
    protected AbstractModel(){
        controlls = new ArrayList<>();
    }


    public List<Circle> getControlls() {
        return controlls;
    }

    public void addControl(Circle circle){
        controlls.add(circle);
        getChildren().add(circle);
    }
}
