package mygame.editor.actions;

import mygame.editor.ui.CustonPane;
import mygame.editor.model.AbstractModel;
import mygame.editor.model.Point;
import mygame.editor.model.PolygonModel;

import java.util.List;

/**
 * Created by oleh on 3/18/17.
 */
public class PolygonDrawer extends Action {

    PolygonModel polygonModel;

    public PolygonDrawer(CustonPane parent, List<AbstractModel> models) {
        super(parent,models);
    }

    @Override
    public void mouseMoved(Point pos) {
        if(polygonModel != null)polygonModel.updateLastPoint(pos);
    }

    @Override
    public void mousePressed(Point pos) {
        if(polygonModel == null){
            polygonModel = new PolygonModel(pos);
            parent.addItem(polygonModel);
        }else{
            polygonModel.addPoint(pos);
        }
    }

    @Override
    public void mouseReleased(Point pos) {

    }

    @Override
    public void finishDrawing() {
        if(polygonModel != null)models.add(polygonModel);
        polygonModel=null;
    }


}
