package mygame.editor.model

import javafx.scene.shape.Circle
import mygame.Constants
import mygame.editor.kotlin.Transformable

class HandlerPoint(point:Point,model:AbstractModel):Circle(point.x,point.y,5.0),Transformable{
    val model:AbstractModel
    val point:Point
    init {
        this.model = model
        this.point = point

        stroke = Constants.WHITE
        fill = Constants.LIGHT_GREY
        update(point)
    }

    fun update(point:Point){
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