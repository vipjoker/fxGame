package mygame.editor.actions.shapes;

import mygame.editor.actions.Action;
import mygame.editor.ui.CustonPane;
import mygame.editor.view.AbstractView;
import mygame.editor.model.Point;
import mygame.editor.view.PolygonModel;


/**
 * Created by oleh on 3/18/17.
 */
class PolygonDrawer (pane:CustonPane,models:List<AbstractView>): Action(pane,models) {

    var polygonModel:PolygonModel? = null


    override fun mouseMoved(pos:Point) {
        polygonModel?.updateLastPoint(pos);
    }

    override fun mousePressed(pos:Point) {
        if(polygonModel == null){
            polygonModel = PolygonModel(pos)
            parent.addItem(polygonModel!!)
        }else{
            polygonModel?.addPoint(pos)
        }
    }

    override fun mouseReleased(pos:Point) {

    }

    override fun finishDrawing() {
        if(polygonModel != null)models.add(polygonModel)
        polygonModel=null
    }


}
