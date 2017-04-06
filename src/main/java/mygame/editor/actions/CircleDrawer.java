package mygame.editor.actions;

import mygame.editor.ui.CustonPane;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.CircleModel;
import mygame.editor.model.Point;

import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class CircleDrawer extends Action {


   CircleModel model ;
    public CircleDrawer(CustonPane parent, List<AbstractModel> model) {
        super(parent,model);
    }

    @Override
    public void mouseMoved(Point pos) {
        if(model != null) model.updateRadius(pos);

    }

    @Override
    public void mousePressed(Point pos) {
        if(model == null) {
            model = new CircleModel(pos);

            parent.addItem(model);
        }

    }

    @Override
    public void mouseReleased(Point pos) {
        if (model != null)models.add(model);
        model = null;
    }

    @Override
    public void finishDrawing() {

    }
}
