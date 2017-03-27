package mygame.editor.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import mygame.Constants;
import mygame.editor.Converter;

import static mygame.editor.Converter.countXdistance;
import static mygame.editor.Converter.countYDistance;

/**
 * Created by oleh on 3/21/17.
 */
public class RectangleModel extends AbstractModel {

    final Rectangle rectangle;
    Circle startHandler,endHandler;

    public RectangleModel(Point2D startPoint) {
        super(Type.RECTANGLE);
        rectangle = new Rectangle(startPoint.getX(), startPoint.getY(), 5,5);
        rectangle.setStroke(Constants.RED);
        rectangle.setFill(Constants.RED.deriveColor(1, 1, 1, .25));
        getChildren().add(rectangle);

        startHandler = getHandler(startPoint);
        addControl(startHandler);
        startHandler.setUserData(startPoint);

    }

    public void updatePoint(Point2D newPoint) {

        if(endHandler == null){
            endHandler  = getHandler(newPoint);
            endHandler.setUserData(newPoint);
            addControl(endHandler);
        }else{
            endHandler.setCenterX(newPoint.getX());
            endHandler.setCenterY(newPoint.getY());
        }

        Point2D startPoint = (Point2D) startHandler.getUserData();

        if (newPoint.getX() < startPoint.getX()) rectangle.setX(newPoint.getX());

        rectangle.setWidth(countXdistance(startPoint,newPoint));



        if (newPoint.getY() < startPoint.getY())rectangle.setY(newPoint.getY());

        rectangle.setHeight(countYDistance(startPoint,newPoint));



    }


}

