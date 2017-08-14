package mygame.editor.view

import javafx.scene.shape.Circle
import mygame.Constants
import mygame.editor.model.CircleFixtutureModel
import mygame.editor.model.CustonAffine
import mygame.editor.model.Point
import mygame.editor.view.AbstractView


class CircleModel(pos: Point) : AbstractView(CircleFixtutureModel(pos)) {

    var circle: Circle
    val center: Point = pos
    val radiusPoint: Point = pos.clone().add(10.0,0.0)

    init{
        circle =  Circle(pos.x,pos.y,center.distance(radiusPoint), Constants.GREEN.deriveColor(1.0,1.0,1.0,.25))
        circle.stroke = Constants.GREEN
        appendPoint(pos)
        appendPoint(radiusPoint)
        children.add(circle)
    }

    fun updateRadius(radius: Point){
        radiusPoint.set(radius)

        circle.radius = center.distance(radiusPoint)
    }

    override fun transform(trans: CustonAffine) {
        val newCenter = trans.transformPoint(center)
        val newRadius = trans.transformPoint(radiusPoint)
        update(newCenter,newRadius)

    }

    override fun update(vararg points: Point) {

        var center: Point = points[0]
        var radius: Point =  points[1]

        circle.radius = center.distance(radius)
        circle.centerX = center.x
        circle.centerY = center.y
    }
}

