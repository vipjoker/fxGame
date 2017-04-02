package mygame.editor.model;

import javafx.geometry.Point2D
import javafx.scene.transform.Affine


class CustonAffine: Affine() {

     fun transformPoint(x: Double, y: Double): Point {
        return Point(super.transform(x, y))
    }

    fun transformPoint (point:Point):Point{
        val transformed = transform(point.toPoint2D())
        return Point(transformed)
    }

    fun inverseTransformPoint(x:Double,y:Double):Point{
        return Point(inverseTransform(x,y))
    }

    fun inverseDeltaTransformPoint(x:Double,y:Double):Point{
        return Point(inverseDeltaTransform(x,y))
    }

}
