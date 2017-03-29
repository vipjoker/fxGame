package mygame.demo.kotlin;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.transform.Affine


class CustomCircle(x:Double ,y:Double, rad:Double) : Circle(x,y,rad) {

    val center:Point2D
    val radiusPoint:Point2D

     init{

        fill = Color.WHEAT
        center =  Point2D(x,y)


        radiusPoint =  Point2D(x + rad,y)
    }

    fun updatePosition(x:Double,y:Double){

    }

    fun transform(transform:Affine){
        val newCenter = transform.transform(center)
        val newRadius = transform.transform(radiusPoint)
        radius = newCenter.distance(newRadius)
        setCenter(newCenter)



    }



    fun setCenter(point:Point2D){
        centerX = point.x
        centerY = point.y
    }


}
