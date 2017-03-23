package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;
import mygame.Constants;
import mygame.editor.model.AbstractModel;
import mygame.editor.views.CustomRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class PolygonDrawer extends Action {
    Polygon polygon;
    List<Point2D> points;

    public PolygonDrawer(CustomRegion parent, List<AbstractModel> models) {
        super(parent,models);
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
                parent.addChild(polygon);
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
