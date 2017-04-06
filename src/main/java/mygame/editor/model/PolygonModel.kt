package mygame.editor.model

import javafx.scene.shape.Polygon
import mygame.Constants
import mygame.editor.model.CustonAffine

class PolygonModel constructor(startPoint: Point) : AbstractModel(Type.POLYGON) {
    var polygon: Polygon

    init {

        appendPoint(startPoint)
        polygon = Polygon()
        polygon.fill = Constants.ORANGE.deriveColor(1.0, 1.0, 1.0, .25)
        polygon.stroke = Constants.ORANGE
        children.add(polygon)



    }

    fun addPoint(newPoint:Point) {
        points.add(newPoint)
        update()
    }




    fun updateLastPoint(newLocation: Point) {

    }

    override fun transform(trans: CustonAffine) {
        polygon.points.clear()
        if (points.size > 2)
            for (point in points) {
                val p  = trans.transformPoint(point)
                polygon.points.add(p.x)
                polygon.points.add(p.y)
            }
    }

    override fun update(vararg points: Point) {

    }
}
