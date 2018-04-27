package mygame.editor.actions;

import javafx.scene.layout.VBox
import mygame.editor.ui.PhysicsNode
import mygame.editor.model.Point
import mygame.editor.ui.CustomPane
import mygame.editor.view.AbstractView

/**
 * Created by oleh on 3/27/17.
 */
class SelectAction(val pane: VBox, transGroup: CustomPane, models: List<AbstractView>) : Action(transGroup, models) {

    init {


    }

    override fun init(){
        parent.parent.setOnMouseClicked { clearEffects() }
        for(child in parent.root.children){
            child.setOnMouseClicked {
                var n = it.source as PhysicsNode
                n.toggleActive()
            }

        }
//        for (model in models) {
//            model.setOnMouseClicked({
//                clearEffects()
//                pane.children.clear()
//                try {
//                    val loader = FXMLLoader(this.javaClass.getResource("/info.fxml"));
//                    val load: Parent = loader.load()
//                    val controller: InfoController = loader.getController();
//                    controller.setNameInfo(model.toString())
//
////            controller.setPositionInfo(Point (model.getModel().getPosition().getX(),
////                    model.getModel().getPosition().getY()));
//
//                    pane.children.add(load)
//                } catch (e: IOException) {
//                    e.printStackTrace();
//                }
//                pane.children.add(Text(it.source.toString()))
//
//                model.effect = Grid(0.0, Color.SALMON)
//                it.consume()
//            })
//        }
    }


    private fun clearEffects() {
        for (model in models) {
            model.effect = null
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
