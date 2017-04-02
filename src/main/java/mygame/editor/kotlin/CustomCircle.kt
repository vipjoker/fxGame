package mygame.editor.kotlin;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.transform.Affine
import mygame.editor.model.CustonAffine


class CustomCircle(x:Double ,y:Double, rad:Double) : Circle(x,y,rad),Transformable {

    val center:Point2D
    val radiusPoint:Point2D

     init{

        fill = Color.WHEAT
        center =  Point2D(x,y)
        radiusPoint =  Point2D(x + rad,y)
    }

    fun updatePosition(x:Double,y:Double){

    }

   override fun transform(trans:CustonAffine){
        val newCenter = trans.transform(center)
        val newRadius = trans.transform(radiusPoint)
        radius = newCenter.distance(newRadius)
        setCenter(newCenter)
    }



    fun setCenter(point:Point2D){
        centerX = point.x
        centerY = point.y
    }


}
