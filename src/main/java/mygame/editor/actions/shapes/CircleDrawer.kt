package mygame.editor.actions.shapes;

import mygame.editor.actions.Action
import mygame.editor.ui.CustomPane;
import mygame.editor.view.AbstractView;
import mygame.editor.view.CircleModel;
import mygame.editor.model.Point;


/**
 * Created by oleh on 3/18/17.
 */
class CircleDrawer (val pane:CustomPane, model:List<AbstractView>) : Action(pane,model){


   var model:CircleModel? = null


    override fun mouseMoved(pos:Point) {
        model?.updateRadius(pos)
    }


    override fun mousePressed(pos:Point ) {
        if(model == null) {
            model = CircleModel(pos);

            pane.addItem(model!!)
        }

    }

    override fun mouseReleased(pos: Point) {
        if (model != null)models.add(model);
        model = null
    }

    override fun finishDrawing() {

    }
}
