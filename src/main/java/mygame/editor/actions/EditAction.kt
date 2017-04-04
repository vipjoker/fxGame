package mygame.editor.actions

import javafx.scene.Node
import javafx.scene.effect.*
import javafx.scene.paint.Color

import mygame.editor.kotlin.CustonPane
import mygame.editor.model.*
import mygame.editor.model.AbstractModel.Type.*
import java.util.function.BiConsumer


class EditAction(parent: CustonPane, models: List<AbstractModel>) : Action(parent, models) {
    var activeHandlerPoint:HandlerPoint? = null

    val handlers: MutableList<HandlerPoint> = mutableListOf()
    override fun init() {

        parent.parent.setOnMousePressed {
            removeEffects()
            it.consume()

        }

        for (model in models) {
            model.setOnMouseClicked {

                if (!it.isControlDown) removeEffects()

                val mod: AbstractModel = it.source as AbstractModel


                mod.points.forEach {
                    var handlerPoint: HandlerPoint = HandlerPoint(it, mod,parent.transform)
                    parent.addItem(handlerPoint)
                    handlers.add(handlerPoint)
                    handlerPoint.setOnMousePressed {
                        print("CLICKED")
                        activeHandlerPoint = it.source as HandlerPoint
                        it.consume()
                    }
                }
                var shadow: Shadow = Shadow(0.0, Color.GOLD)
                mod.setEffect(shadow)
                it.consume()
            }
        }
    }


    fun removeEffects() {
        for (model in models) model.setEffect(null)
        for (handler in handlers) parent.removeItem(handler)
        handlers.clear()


    }

    override fun mouseMoved(point: Point) {
        if(activeHandlerPoint!= null){
            println("Update to ${point.x} ${point.y}")
            (activeHandlerPoint as HandlerPoint).updatePoint(point)
            parent.updateAll()
        }
    }

    override fun mousePressed(point: Point) {

    }

    override fun mouseReleased(point: Point) {
        activeHandlerPoint = null
    }

    override fun finishDrawing() {
        for (model in models) {
            model.setOnMouseClicked(null);
        }
    }
}
