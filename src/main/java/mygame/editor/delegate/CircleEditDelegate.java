package mygame.editor.delegate;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import mygame.editor.model.CircleModel;
import mygame.editor.model.Point;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by oleh on 3/26/17.
 */
public class CircleEditDelegate implements BiConsumer<Node,Point> {

    private final CircleModel model;

    public CircleEditDelegate(CircleModel model) {
        this.model = model;
    }

    @Override
    public void accept(Node node,Point point) {

    }
}
