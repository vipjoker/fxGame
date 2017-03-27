package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import mygame.editor.DragHelper;
import mygame.editor.delegate.CircleEditDelegate;
import mygame.editor.delegate.LineEditDelegate;
import mygame.editor.delegate.PolygonEditDelegate;
import mygame.editor.delegate.RectangleEditDelegate;
import mygame.editor.model.*;
import mygame.editor.views.CustomRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by oleh on 3/21/17.
 */
public class EditAction extends Action {
    List<DragHelper> helpers;

    public EditAction(CustomRegion parent, List<AbstractModel> models) {
        super(parent, models);
        helpers = new ArrayList<>();
    }

    @Override
    public void init() {

        parent.getParent().setOnMouseClicked(event -> {
            removeEffects();
            event.consume();

        });

        for (AbstractModel model : models) {
            model.setOnMouseClicked(event -> {

                if (!event.isControlDown()) removeEffects();

                AbstractModel mod = ((AbstractModel) event.getSource());


                for (Circle circle : mod.getControlls()) {
                    DragHelper helper = new DragHelper(getDelegateHandler(model));
                    helpers.add(helper);
                    helper.setDrag(circle);

                }
                Shadow shadow = new Shadow(0, Color.GOLD);
                mod.setEffect(shadow);
                event.consume();
            });
        }
    }

    public void removeEffects() {
        for (AbstractModel model : models) {
            model.setEffect(null);
        }
        for (DragHelper helper : helpers) helper.removeDrag();
        helpers.clear();
    }

    private BiConsumer<Node,Point> getDelegateHandler(AbstractModel model) {
        switch (model.getType()) {
            case RECTANGLE:
                return new RectangleEditDelegate((RectangleModel) model);
            case CIRCLE:
                return new CircleEditDelegate((CircleModel) model);
            case LINE:
                return new LineEditDelegate((LineModel) model);
            case POLYGON:
                return new PolygonEditDelegate((PolygonModel) model);
            default:
                throw new RuntimeException("Wrong model type");
        }


    }

    @Override
    public void mouseMoved(Point2D position) {

    }

    @Override
    public void mousePressed(Point2D position) {

    }

    @Override
    public void mouseReleased(Point2D position) {

    }

    @Override
    public void finishDrawing() {
        for (AbstractModel model : models) {
            model.setOnMouseClicked(null);
        }
    }
}
