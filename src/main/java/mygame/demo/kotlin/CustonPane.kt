package mygame.demo.kotlin

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.text.Text
import javafx.scene.transform.Affine
import javafx.scene.transform.Transform


class CustonPane : Pane(){

    var screen:Point2D? = null

    var transform:Affine
    var horLine:Line
    var verLine:Line
    var infoText: Text
    var circle :CustomCircle
    var polygon:CustomPolygon
    init {
        background = Background(BackgroundFill(Color.BLACK,null,null))
        horLine = Line()
        verLine = Line()
        circle = CustomCircle(0.0,0.0,10.0)
        polygon = CustomPolygon(40.0,40.0)
        horLine.stroke = Color.WHEAT
        verLine.stroke = Color.WHEAT
        infoText = Text("Info")
        infoText.fill = Color.AQUA
        infoText.x = 50.0
        infoText.y = 50.0
        addChildren(circle,polygon,verLine,horLine,infoText)

        Platform.runLater {
            screen = Point2D(width/2,height/2)
            updateAxis(screen as Point2D )



        }

        transform = Affine()
        transform.setToIdentity()
        setListeners()

    }

    fun updateAxis(point:Point2D){


        circle.transform(transform)
        polygon.transform(transform)


        horLine.startX = 0.0
        horLine.startY = point.y
        horLine.endX = width
        horLine.endY = point.y

        verLine.startX = point.x
        verLine.endX = point.x
        verLine.startY = 0.0
        verLine.endY = height
    }

    fun addChild (node:Node) {
        children.add(node)
    }

    fun addChildren(vararg nodes:Node){
        children.addAll(nodes)
    }



    var  lastPoint:Point2D? = null
    fun setListeners(){
        setOnScroll {
            val delta  = if(it.deltaY > 0)  1.05 else 0.95

            val point = transform.inverseTransform(it.x,it.y)
            transform.appendScale(delta,delta,point)
            val newPoint = transform.transform(screen)

            updateAxis(newPoint)
            it.consume()
        }
        setOnMousePressed {
            lastPoint = Point2D(it.x,it.y)
        }

        setOnMouseDragged {
            if(lastPoint != null){


                val trans = transform.inverseDeltaTransform(it.x,it.y)
                val startPoint =transform.inverseDeltaTransform(lastPoint)

                val deltaX = trans.x - (startPoint as Point2D).x
                val deltaY = trans.y - (startPoint as Point2D).y

               infoText.text = "transX ${trans.x} transY ${trans.y}  \ndelta x $deltaX delta y $deltaY"
               transform.appendTranslation(deltaX,deltaY)

                val newPoint =transform.transform(screen)

                updateAxis(newPoint)
                lastPoint = Point2D(it.x,it.y)
                it.consume()

            }
        }

        setOnMouseReleased { lastPoint = null }
    }



}


