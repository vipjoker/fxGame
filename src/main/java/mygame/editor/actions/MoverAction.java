package mygame.editor.actions;

import javafx.geometry.Point2D;
import mygame.editor.DragHelper;
import mygame.editor.kotlin.CustonPane;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/21/17.
 */
public class MoverAction extends Action {
    List<DragHelper> helpers;
    public MoverAction(CustonPane parent, List<AbstractModel> models) {
        super(parent, models);

    }

    @Override
    public void init() {
        if(helpers == null || helpers.isEmpty()){
            helpers = new ArrayList<>();

            for(AbstractModel model : models){

                DragHelper helper = new DragHelper();

                helper.setDrag(model);
                helpers.add(helper);
            }
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
        for(DragHelper helper :helpers)helper.removeDrag();
        helpers.clear();
    }
}
