package mygame.editor.actions;

import mygame.editor.ui.CustonPane;
import mygame.editor.view.AbstractView;
import mygame.editor.model.Point;
import mygame.editor.view.RectangleView;

import java.util.List;

public class RectangleDrawer extends Action {

    public RectangleDrawer(CustonPane parent, List<AbstractView> models) {
        super(parent,models);
    }


    RectangleView model;

    @Override
    public void mouseMoved(Point pos) {

        if(model != null)model.updatePoint(pos);

    }



    @Override
    public void mousePressed(Point position) {

        if(model == null){
            model = new RectangleView(position);
            parent.addItem(model);
        }
    }

    @Override
    public void mouseReleased(Point position) {
        if(model!= null) models.add(model);
        model = null;


    }

    @Override
    public void finishDrawing() {

    }
}
