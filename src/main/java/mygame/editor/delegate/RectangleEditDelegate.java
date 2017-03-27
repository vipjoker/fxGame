package mygame.editor.delegate;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import mygame.editor.model.Point;
import mygame.editor.model.RectangleModel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by oleh on 3/26/17.
 */
public class RectangleEditDelegate implements BiConsumer<Node,Point> {

    private final RectangleModel model;

    public RectangleEditDelegate(RectangleModel model) {
        this.model = model;
    }

    @Override
    public void accept(Node circle,Point point) {

    }
}
