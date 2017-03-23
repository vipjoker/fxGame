package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.LineModel;
import mygame.editor.views.CustomRegion;

import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class LineDrawer extends Action {

    private LineModel model;

    public LineDrawer(CustomRegion parent, List<AbstractModel> list) {
        super(parent, list);
    }


    @Override
    public void mouseMoved(Point2D pos) {
        if (model != null) model.updateLine(pos);
    }

    @Override
    public void mousePressed(Point2D pos) {

        if (model == null) {
            model = new LineModel(pos);
            parent.addChild(model);
        } else {
            model.addPoint(pos);
        }

    }

    @Override
    public void mouseReleased(Point2D pos) {

    }

    @Override
    public void finishDrawing() {
        models.add(model);
        model = null;
    }
}
