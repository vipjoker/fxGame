package mygame.editor.view

import javafx.scene.shape.Polygon
import mygame.Constants
import mygame.editor.model.CustonAffine
import mygame.editor.model.Point
import mygame.editor.model.PolygonFixtureModel
import mygame.editor.view.AbstractView

class PolygonModel constructor(startPoint: Point) : AbstractView(PolygonFixtureModel(startPoint)) {
    var polygon: Polygon

    init {

        appendPoint(startPoint)
        polygon = Polygon()
        polygon.fill = Constants.ORANGE.deriveColor(1.0, 1.0, 1.0, .25)
        polygon.stroke = Constants.ORANGE
        children.add(polygon)




    }

    fun addPoint(newPoint: Point) {
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
