package mygame.editor.model

import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import mygame.Constants

class LineModel(startPoint: Point) : AbstractModel(Type.LINE) {
    val path:Path
    var lineTo:LineTo? = null
    init{


        val moveTo =  MoveTo(startPoint.x, startPoint.y)
        path =  Path(moveTo)
        path.toBack()
        path.stroke = Constants.RED
        path.strokeWidth = 2.0

        appendPoint(startPoint)
        children.add(path)
    }

    fun addPoint(pos:Point){
        lineTo =  LineTo(pos.x,pos.y)

        path.elements.add(lineTo)
    }

    fun updateLine(pos:Point){
        if(lineTo !=null){
            (lineTo as LineTo).x = pos.x
            (lineTo as LineTo).y = pos.y
        }
    }


    override fun transform(trans:CustonAffine ) {

    }

    override fun update(vararg points: Point) {

    }
}

