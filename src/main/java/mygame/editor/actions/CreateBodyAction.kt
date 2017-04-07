package mygame.editor.actions;

import mygame.editor.model.BodyModel
import mygame.editor.ui.CustonPane
import mygame.editor.view.AbstractView
import mygame.editor.model.Point
import mygame.editor.ui.BodyView

class CreateBodyAction(pane: CustonPane,val views:MutableList<AbstractView>):Action(pane, views){


    override fun mouseMoved(position: Point) {

    }

    override fun mousePressed(position: Point) {


        var model :BodyModel = BodyModel(position)
        var bodyView : BodyView = BodyView(model)

        parent.addItem(bodyView)

views.add(bodyView)

    }

    override fun mouseReleased(position: Point) {

    }

    override fun finishDrawing() {

    }
}
