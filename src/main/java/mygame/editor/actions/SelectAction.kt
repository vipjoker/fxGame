package mygame.editor.actions;

import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.effect.Effect
import javafx.scene.effect.Glow
import javafx.scene.effect.Shadow
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import mygame.editor.InfoController
import mygame.editor.model.Point
import mygame.editor.ui.CustomPane
import mygame.editor.view.AbstractView
import java.io.IOException

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
                var n = it.target as Node
                n.effect = Glow(.4)
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
//                model.effect = Shadow(0.0, Color.SALMON)
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
