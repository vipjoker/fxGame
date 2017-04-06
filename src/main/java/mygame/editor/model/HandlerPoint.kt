package mygame.editor.model

import javafx.scene.control.Tooltip
import javafx.scene.shape.Circle
import mygame.Constants
import mygame.editor.kotlin.Transformable
import mygame.editor.model.AbstractModel.Type.*
import mygame.editor.model.CustonAffine

class HandlerPoint(centerPoint:Point,model:AbstractModel,affine: CustonAffine):Circle( centerPoint.x,centerPoint.y,5.0),Transformable{
    val model:AbstractModel
    val point:Point

    val affine: CustonAffine
    init {
        this.affine = affine
        this.model = model
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
        return when(model.type){
            CIRCLE ->"Circle "
            RECTANGLE ->"Rectangle "
            POLYGON ->"Polygon "
            LINE ->"Line "
            else ->"Unknown"
        }
    }

    private fun update(point:Point){
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