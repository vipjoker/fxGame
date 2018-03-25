package mygame.editor.actions.shapes;

import mygame.editor.actions.Action;
import mygame.editor.ui.CustonPane;
import mygame.editor.view.AbstractView;
import mygame.editor.model.Point;
import mygame.editor.view.RectangleView


class RectangleDrawer(pane:CustonPane,models:List<AbstractView>) :Action(pane,models) {

    var model: RectangleView? = null

    override fun mouseMoved(pos:Point) {
        model?.updatePoint(pos);
    }

    override fun mousePressed(position:Point ) {
        if(model == null){
            model = RectangleView(position)
            parent.addItem(model!!)
        }
    }

    override fun mouseReleased(position:Point) {
        if(model!= null) models.add(model)
        model = null
    }

    override fun finishDrawing() {

    }
}
