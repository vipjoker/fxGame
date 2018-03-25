package mygame.editor.view

import javafx.scene.Group
import mygame.editor.kotlin.Transformable
import mygame.editor.model.AbstractModel
import mygame.editor.model.Point

abstract class AbstractView(var model:AbstractModel): Group(), Transformable {


    var points:MutableList<Point>  = mutableListOf()

    fun  appendPoint(point: Point){
       points.add(point)
    }

    abstract fun update(vararg points: Point)

    enum class Type{
        POLYGON,LINE,CIRCLE,RECTANGLE,BODY,JOINT
    }
}
