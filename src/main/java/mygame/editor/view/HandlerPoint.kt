package mygame.editor.view

import javafx.scene.control.Tooltip
import javafx.scene.shape.Circle
import mygame.Constants
import mygame.editor.kotlin.Transformable
import mygame.editor.view.AbstractView.Type.*
import mygame.editor.model.CustonAffine
import mygame.editor.model.Point
import mygame.editor.model.Type
import mygame.editor.view.AbstractView

class HandlerPoint(centerPoint: Point, view: AbstractView, affine: CustonAffine): Circle( centerPoint.x,centerPoint.y,5.0), Transformable {
    val view: AbstractView
    val point: Point

    val affine: CustonAffine
    init {
        this.affine = affine
        this.view = view
        this.point = centerPoint

        stroke = Constants.WHITE
        fill = Constants.LIGHT_GREY
        update(point)


        setListeners()
    }


    fun setListeners(){

         val t = Tooltip("${getModelName()}\nx = ${point.x}\ny = ${point.y}")
         Tooltip.install(this, t)
    }

    fun updatePoint(point: Point){
        this.point.set(point)
    }

    fun getModelName():String{
        return when(view.model.type){
            Type.BODY->"Body"
            Type.FIXTURE->"Fixture"
            Type.JOINT->"Joint"
            else ->"Unknown"
        }
    }

    private fun update(point: Point){
        centerX = point.x
        centerY = point.y
    }

    enum class  Constraint{
        HORIZONTAL,
        VERTICAL,
        GRID
    }

    override fun transform(trans: CustonAffine) {
        var newPoint =  trans.transformPoint(point)
        update(newPoint)
    }
}