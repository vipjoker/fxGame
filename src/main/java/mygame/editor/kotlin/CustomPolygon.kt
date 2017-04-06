package mygame.editor.kotlin;

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import javafx.scene.transform.Affine
import mygame.editor.model.CustonAffine


class CustomPolygon(x:Double,y:Double) :Polygon(),Transformable{
    val DEFAULT_SIZE:Double = 10.0
    val mutablePoints:MutableList<Point2D>

    init{
        fill = Color.RED

        mutablePoints = mutableListOf(
            Point2D(x - DEFAULT_SIZE,y - DEFAULT_SIZE),
            Point2D(x + DEFAULT_SIZE,y - DEFAULT_SIZE),
            Point2D(x + DEFAULT_SIZE,y + DEFAULT_SIZE),
            Point2D(x - DEFAULT_SIZE,y + DEFAULT_SIZE)
        )
        updatePoints(mutablePoints)

    }

    fun updatePoints(list:MutableList<Point2D>){
        points.clear()
        list.forEach {
            points.add(it.x)
            points.add(it.y)
        }
    }


   override fun transform(trans: CustonAffine){
        updatePoints(
        mutablePoints.map {
           trans.transform(it)
        }.toMutableList()
        )
    }

}
