package mygame.editor.actions.shapes;

import mygame.editor.actions.Action;
import mygame.editor.ui.CustomPane;
import mygame.editor.view.AbstractView;
import mygame.editor.view.LineModel;
import mygame.editor.model.Point;


class LineDrawer(pane: CustomPane, list: List<AbstractView>) : Action(pane, list) {

    var model: LineModel? = null

    override fun mousePressed(pos: Point) {
        if (model == null) {
            model = LineModel(pos)
            parent.addItem(model!!)
        } else {
            model?.addPoint(pos);
        }
    }

    override fun mouseMoved(pos: Point) {
        model?.updateLine(pos)
    }

    override fun mouseReleased(pos: Point) {

    }

    override fun finishDrawing() {
        models.add(model)
        model = null
    }
}
