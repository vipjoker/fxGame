package mygame.editor.actions;

import mygame.editor.ui.CustonPane
import mygame.editor.model.AbstractModel
import mygame.editor.model.Point
import mygame.editor.ui.BodyView

class CreateBodyAction(pane: CustonPane, models:List<AbstractModel>):Action(pane,models){


    override fun mouseMoved(position: Point) {

    }

    override fun mousePressed(position: Point) {
        var bodyView : BodyView = BodyView(position)
        parent.addItem(bodyView)

    }

    override fun mouseReleased(position: Point) {

    }

    override fun finishDrawing() {

    }
}
