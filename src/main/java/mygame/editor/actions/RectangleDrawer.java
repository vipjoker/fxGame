package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import mygame.Constants;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.RectangleModel;
import mygame.editor.views.CustomRegion;

import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class RectangleDrawer extends Action {

    public RectangleDrawer(CustomRegion parent, List<AbstractModel> models) {
        super(parent,models);
    }


    RectangleModel model;

    @Override
    public void mouseMoved(Point2D pos) {

        if(model != null)model.updatePoint(pos);

    }



    @Override
    public void mousePressed(Point2D position) {

        if(model == null){
            model = new RectangleModel(position);
            parent.addChild(model);
        }
    }

    @Override
    public void mouseReleased(Point2D position) {
        if(model!= null) models.add(model);
        model = null;


    }

    @Override
    public void finishDrawing() {

    }
}
