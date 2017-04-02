package mygame.editor.actions;

import javafx.geometry.Point2D;
import mygame.editor.kotlin.CustonPane;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.LineModel;
import mygame.editor.model.Point;

import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class LineDrawer extends Action {

    private LineModel model;

    public LineDrawer(CustonPane parent, List<AbstractModel> list) {
        super(parent, list);
    }


    @Override
    public void mouseMoved(Point pos) {
        if (model != null) model.updateLine(pos);
    }

    @Override
    public void mousePressed(Point pos) {

        if (model == null) {
            model = new LineModel(pos);
            parent.addItem(model);
        } else {
            model.addPoint(pos);
        }

    }

    @Override
    public void mouseReleased(Point pos) {

    }

    @Override
    public void finishDrawing() {
        models.add(model);
        model = null;
    }
}
