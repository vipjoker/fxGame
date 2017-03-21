package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import mygame.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class PolygonDrawer extends Drawer {
    Polygon polygon;
    List<Point2D> points;

    public PolygonDrawer(Group parent) {
        super(parent);
    }

    @Override
    public void mouseMoved(Point2D pos) {

    }

    @Override
    public void mousePressed(Point2D pos) {
        if (points == null) {
            points = new ArrayList<>();
        }
        getHandler(pos);
        points.add(pos);
        if (points.size() > 2) {
            if (polygon == null) {
                polygon = new Polygon();
                polygon.setFill(Constants.ORANGE.deriveColor(1, 1, 1, .25));
                polygon.setStroke(Constants.ORANGE);
                parent.getChildren().add(polygon);
                for (Point2D point : points) {
                    polygon.getPoints().add(point.getX());
                    polygon.getPoints().add(point.getY());
                }
            }

            polygon.getPoints().addAll(pos.getX(), pos.getY());


        }
    }

    @Override
    public void mouseReleased(Point2D pos) {

    }

    @Override
    public void finishDrawing() {
        polygon = null;
        points = null;
    }


}
