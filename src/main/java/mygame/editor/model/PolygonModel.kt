package mygame.editor.model

import javafx.scene.shape.Polygon
import mygame.Constants

class PolygonModel constructor(startPoint: Point) : AbstractModel(Type.POLYGON) {
    var polygon: Polygon

    init {

        appendPoint(startPoint)
        polygon = Polygon()
        polygon.fill = Constants.ORANGE.deriveColor(1.0, 1.0, 1.0, .25)
        polygon.stroke = Constants.ORANGE
        children.add(polygon)
        polygon.points.addAll(startPoint.x, startPoint.y)


    }

    fun addPoint(newPoint:Point) {
        points.add(newPoint)
        update()
    }




    fun updateLastPoint(newLocation: Point) {

    }

    override fun transform(trans: CustonAffine) {

    }

    override fun update(vararg points: Point) {
        polygon.points.clear()
        if (points.size > 2)
            for (point in points) {
                polygon.points.add(point.x)
                polygon.points.add(point.y)
            }
    }
}
