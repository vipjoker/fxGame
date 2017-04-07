package mygame.editor.actions;

import mygame.editor.ui.CustonPane;
import mygame.editor.view.AbstractView;
import mygame.editor.view.LineModel;
import mygame.editor.model.Point;

import java.util.List;


public class LineDrawer extends Action {

    private LineModel model;

    public LineDrawer(CustonPane parent, List<AbstractView> list) {
        super(parent, list);
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
    public void mouseMoved(Point pos) {
        if (model != null) model.updateLine(pos);
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
