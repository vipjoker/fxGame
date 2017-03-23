package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import mygame.editor.DragHelper;
import mygame.editor.model.AbstractModel;
import mygame.editor.views.CustomRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/21/17.
 */
public class MoverAction extends Action {
    List<DragHelper> helpers;
    public MoverAction(CustomRegion parent, List<AbstractModel> models) {
        super(parent, models);

    }

    @Override
    public void mouseMoved(Point2D position) {

    }

    @Override
    public void mousePressed(Point2D position) {
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
    public void mouseReleased(Point2D position) {

    }

    @Override
    public void finishDrawing() {
        for(DragHelper helper :helpers)helper.removeDrag();
        helpers.clear();
    }
}
