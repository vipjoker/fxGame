package mygame.editor.actions;

import mygame.editor.model.PhysicsNode
import mygame.editor.ui.CustomPane;
import mygame.editor.view.AbstractView;
import mygame.editor.model.Point;
import mygame.editor.util.DragNDrop


/**
 * Created by oleh on 3/21/17.
 */
class MoverAction(parent: CustomPane, models: List<AbstractView>) : Action(parent, models) {

    override fun init() {

        for(child in parent.root.children){
            val listener = DragNDrop()
            child.onMouseDragged = listener.onMoveListener
            child.onMousePressed= listener.onStartListener

        }
    }

    override fun mouseMoved(position: Point) {

    }

    override fun mousePressed(position: Point) {

    }

    override fun mouseReleased(position: Point) {

    }

    override fun finishDrawing() {

    }
}
