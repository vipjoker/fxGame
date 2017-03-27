package mygame.editor.actions;

import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;
import mygame.Constants;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.PolygonModel;
import mygame.editor.views.CustomRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class PolygonDrawer extends Action {

    PolygonModel polygonModel;

    public PolygonDrawer(CustomRegion parent, List<AbstractModel> models) {
        super(parent,models);
    }

    @Override
    public void mouseMoved(Point2D pos) {
        if(polygonModel != null)polygonModel.updateLastPoint(pos);
    }

    @Override
    public void mousePressed(Point2D pos) {
        if(polygonModel == null){
            polygonModel = new PolygonModel(pos);
            parent.addChild(polygonModel);
        }else{
            polygonModel.addPoint(pos);
        }
    }

    @Override
    public void mouseReleased(Point2D pos) {

    }

    @Override
    public void finishDrawing() {
        if(polygonModel != null)models.add(polygonModel);
        polygonModel=null;
    }


}
