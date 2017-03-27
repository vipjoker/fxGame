package mygame.editor.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import mygame.Constants;

import java.util.*;

/**
 * Created by oleh on 3/21/17.
 */
public class PolygonModel extends AbstractModel {
    Polygon polygon;
    List<Point> points;


    public PolygonModel(Point2D startPoint){
        super(Type.POLYGON);
        points = new ArrayList<>();
        Point point = new Point(startPoint);
        points.add(point);
        polygon = new Polygon();
        polygon.setFill(Constants.ORANGE.deriveColor(1, 1, 1, .25));
        polygon.setStroke(Constants.ORANGE);
        getChildren().add(polygon);
        Circle handler = getHandler(startPoint);
        handler.setUserData(point);
        addControl(handler);
        polygon.getPoints().addAll(startPoint.getX(), startPoint.getY());


    }

    public void addPoint(Point2D newPoint){
        Point point = new Point(newPoint);
        Circle handler = getHandler(newPoint);
        handler.setUserData(point);
        addControl(handler);

        points.add(point);

       updatePolygon();
    }




    public void updatePolygon(){
        polygon.getPoints().clear();
        if(points.size() >2)
        for (Point point : points) {
            polygon.getPoints().add(point.getX());
            polygon.getPoints().add(point.getY());
        }
    }

    public void updateLastPoint(Point2D newLocation){

    }
}
