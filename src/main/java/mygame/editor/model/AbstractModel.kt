package mygame.editor.model

import javafx.scene.Group
import mygame.editor.kotlin.Transformable

abstract class AbstractModel(val type:Type):Group(),Transformable{

    var points:MutableList<Point>  = mutableListOf()
    get
    fun  appendPoint(point:Point){
       points.add(point)
    }

    abstract fun update(vararg points:Point)

    enum class Type{
        POLYGON,LINE,CIRCLE,RECTANGLE
    }
}
