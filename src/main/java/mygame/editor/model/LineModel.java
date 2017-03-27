package mygame.editor.model;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import mygame.Constants;

import java.util.ArrayList;

/**
 * Created by oleh on 3/21/17.
 */
public class LineModel extends AbstractModel {
    Path path;
    LineTo lineTo;
    public LineModel(Point2D startPosition){
        super(Type.LINE);

        MoveTo moveTo = new MoveTo(startPosition.getX(), startPosition.getY());
        path = new Path(moveTo);
        path.toBack();
        path.setStroke(Constants.RED);
        path.setStrokeWidth(2);
        Circle handler = getHandler(startPosition);
        handler.setUserData(moveTo);

        handler.centerXProperty().bind(moveTo.xProperty());
        handler.centerYProperty().bind(moveTo.yProperty());
        controlls.add(handler);
        getChildren().addAll(path,handler);

    }

    public void addPoint(Point2D pos){

        lineTo = new LineTo(pos.getX(),pos.getY());
        Circle handler = getHandler(pos);
        handler.centerYProperty().bind(lineTo.yProperty());
        handler.centerXProperty().bind(lineTo.xProperty());
        handler.setUserData(lineTo);
        path.getElements().add(lineTo);
        getChildren().add(handler);
    }

    public void updateLine(Point2D pos){
        if(lineTo !=null){
            lineTo.setX(pos.getX());
            lineTo.setY(pos.getY());
        }
    }

}
