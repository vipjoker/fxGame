package mygame.editor.actions;

import javafx.scene.Node;
import mygame.editor.ui.CustomPane;
import mygame.editor.view.AbstractView;
import mygame.editor.model.Point;
import mygame.editor.util.DragNDrop;

import java.util.List;


/**
 * Created by oleh on 3/21/17.
 */
public class MoverAction extends Action {


    public MoverAction(CustomPane parent, List<AbstractView> models) {
        super(parent, models);
    }

    @Override
    public void init() {

        for (Node child : parent.getRoot().getChildren()) {
            DragNDrop listener = new DragNDrop();
            child.setOnMouseDragged(listener.getOnMoveListener());
            child.setOnMousePressed(listener.getOnStartListener());

        }
    }

    @Override
    public void mouseMoved(Point position) {

    }

    @Override
    public void mousePressed(Point position) {

    }


    @Override
    public void mouseReleased(Point position) {

    }

    @Override
    public void finishDrawing() {
        for (Node child : parent.getRoot().getChildren()) {
            child.setOnMouseDragged(null);
            child.setOnMousePressed(null);

        }
    }
}
