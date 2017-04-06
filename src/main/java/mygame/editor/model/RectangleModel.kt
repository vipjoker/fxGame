package mygame.editor.model;

import javafx.geometry.Point2D;

import javafx.scene.shape.Rectangle;

import mygame.Constants
import mygame.editor.model.CustonAffine

class RectangleModel(startPoint:Point) : AbstractModel(Type.RECTANGLE) {

    val rectangle:Rectangle


   init {

        rectangle =  Rectangle(startPoint.x, startPoint.y, 5.0,5.0);
        rectangle.stroke = Constants.RED
        rectangle.fill =Constants.RED.deriveColor(1.0, 1.0, 1.0, .25)
        children.add(rectangle)

        appendPoint(startPoint)

    }

    fun updatePoint(newPoint:Point ) {

//        if(endHandler == null){
//            endHandler  = getHandler(newPoint);
//            endHandler.setUserData(newPoint);
//            addControl(endHandler);
//        }else{
//            endHandler.setCenterX(newPoint.getX());
//            endHandler.setCenterY(newPoint.getY());
//        }
//
//        var startPoint = startHandler.getUserData() as Point
//
//        if (newPoint.getX() < startPoint.x) rectangle.setX(newPoint.getX());
//
//        rectangle.setWidth(countXdistance(startPoint,newPoint));
//
//
//
//        if (newPoint.getY() < startPoint.y)rectangle.setY(newPoint.getY());
//
//        rectangle.setHeight(countYDistance(startPoint,newPoint));



    }

    override fun update(vararg points: Point) {

    }

    override fun  transform(trans: CustonAffine) {

    }
}

