package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import mygame.Constants;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.CircleModel;
import mygame.editor.views.CustomRegion;

import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class CircleDrawer extends Action {


   CircleModel model ;
    public CircleDrawer(CustomRegion parent, List<AbstractModel> model) {
        super(parent,model);
    }

    @Override
    public void mouseMoved(Point2D pos) {
        if(model != null) model.updateRadius(pos);

    }

    @Override
    public void mousePressed(Point2D pos) {
        if(model == null) {
            model = new CircleModel(pos);
            parent.addChild(model);
        }

    }

    @Override
    public void mouseReleased(Point2D pos) {
        if (model != null)models.add(model);
    }

    @Override
    public void finishDrawing() {

    }
}
