package mygame.editor.model;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import mygame.Constants;

/**
 * Created by oleh on 3/21/17.
 */
public class CircleModel extends AbstractModel {

    Circle circle;
    Circle handler;

    public CircleModel(Point2D pos){
        super(Type.CIRCLE);
        circle = new Circle(pos.getX(),pos.getY(),5, Constants.GREEN.deriveColor(1,1,1,.25));
            circle.setStroke(Constants.GREEN);
            getChildren().add(circle);



    }

    public void updateRadius(Point2D radius){
        if(handler == null) {
            handler = getHandler(radius);
            handler.setUserData(circle);
            addControl(handler);
        }else{
            handler.setCenterY(radius.getY());
            handler.setCenterX(radius.getX());
            circle.setRadius(radius.distance(circle.getCenterX(),circle.getCenterY()));
        }


    }

}
