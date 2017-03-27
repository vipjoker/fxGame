package mygame.editor.delegate;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.Point;
import mygame.editor.model.PolygonModel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by oleh on 3/26/17.
 */
public class PolygonEditDelegate implements BiConsumer<Node,Point> {
    private final PolygonModel model;

    public PolygonEditDelegate(PolygonModel model) {
        this.model = model;
    }

    @Override
    public void accept(Node node,Point point) {

        Circle circle = (Circle)node;
        Point oldPoint = (Point) circle.getUserData();

        Transform localToParentTransform = circle.getLocalToParentTransform();
        Point2D transform = localToParentTransform.transform(new Point2D(point.getX(), point.getY()));
        oldPoint.set(transform.getX(),transform.getY());
        model.updatePolygon();


    }
}
