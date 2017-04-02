package mygame.editor.model;

import javafx.geometry.Point2D


class Point {


    var x:Double get set
    var y:Double get set
    constructor(x:Double,y:Double){
        this.x = x
        this.y = y
    }

    constructor(point2D: Point2D){
        x = point2D.x
        y = point2D.y
    }

    fun add(x:Double,y:Double):Point{
        this.x+=x
        this.y+=y
        return this
    }

    fun distance(point:Point):Double{
        val deltaX  = point.x - x
        val deltaY = point.y - y
        return Math.sqrt(deltaX  * deltaX + deltaY * deltaY)
    }

    fun clone():Point{
        return Point(x,y)
    }

    fun toPoint2D():Point2D{
        return Point2D(x,y);
    }
}
