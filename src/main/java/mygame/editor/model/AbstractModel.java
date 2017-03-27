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
     private final Type type;
    protected Circle getHandler(Point2D point) {
        Circle circle = new Circle(point.getX(), point.getY(), 5);
        circle.setStroke(Constants.WHITE);
        circle.setFill(Constants.LIGHT_GREY);
        return circle;
    }
    protected AbstractModel(Type type){
        this.type = type;
        controlls = new ArrayList<>();
    }

    public Type getType() {
        return type;
    }

    public List<Circle> getControlls() {
        return controlls;
    }

    public void addControl(Circle circle){
        controlls.add(circle);
        getChildren().add(circle);
    }


    public enum Type{
        POLYGON,LINE,CIRCLE,RECTANGLE
    }
}
