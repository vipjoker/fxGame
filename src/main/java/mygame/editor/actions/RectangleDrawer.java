package mygame.editor.actions;

import mygame.editor.ui.CustonPane;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.Point;
import mygame.editor.model.RectangleModel;

import java.util.List;

public class RectangleDrawer extends Action {

    public RectangleDrawer(CustonPane parent, List<AbstractModel> models) {
        super(parent,models);
    }


    RectangleModel model;

    @Override
    public void mouseMoved(Point pos) {

        if(model != null)model.updatePoint(pos);

    }



    @Override
    public void mousePressed(Point position) {

        if(model == null){
            model = new RectangleModel(position);
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
