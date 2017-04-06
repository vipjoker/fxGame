package mygame.editor.ui;


import javafx.scene.Group
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import mygame.Constants
import mygame.editor.kotlin.Transformable
import mygame.editor.model.CustonAffine
import mygame.editor.model.Point

class BodyView(center: Point) : Group(), Transformable {
    var hor: Line = Line()
    var ver: Line = Line()
    var rect: Rectangle = Rectangle()

    var center: Point
    var left: Point
    var up: Point
    var HALF_LENGTH: Double = 10.0
    var STROKE_WIDTH: Double = 1.0

    init {
        this.center = center
        this.left = center.clone().add(HALF_LENGTH, 0.0)
        this.up = center.clone().add(0.0, HALF_LENGTH)
        hor.stroke = Constants.GREEN
        hor.strokeWidth = STROKE_WIDTH
        ver.stroke = Constants.RED
        rect.fill = Constants.WHITE.deriveColor(1.0,1.0,1.0,.25)
        ver.strokeWidth = STROKE_WIDTH
        children.addAll(rect,hor, ver)
    }


    override fun transform(trans: CustonAffine) {

        val center = trans.transformPoint(center)
        val left = trans.transformPoint(left)
        val up = trans.transformPoint(up)

        hor.startX = center.x
        hor.endX = left.x

        hor.startY = center.y
        hor.endY = left.y

        ver.startX = center.x
        ver.endX = up.x
        ver.startY = center.y
        ver.endY = up.y
        rect.x =center.x - center.distance(left)
        rect.y = center.y - center.distance(up)
        rect.width = center.distance(left)*2
        rect.height = center.distance(up)*2


    }
}
