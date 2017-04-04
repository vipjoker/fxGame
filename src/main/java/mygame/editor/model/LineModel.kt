package mygame.editor.model

import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import mygame.Constants

class LineModel(startPoint: Point) : AbstractModel(Type.LINE) {
    val path:Path = Path()
    init{

        path.toBack()
        path.stroke = Constants.RED
        path.strokeWidth = 2.0
        appendPoint(startPoint)
        children.add(path)
    }

    fun addPoint(pos:Point){
        appendPoint(pos)
    }

    fun updateLine(pos:Point){

        if(!points.isEmpty()){
            points.last().set(pos)
        }
    }


    override fun transform(trans:CustonAffine ) {
        path.elements.clear()
        for(index in 0..points.lastIndex){
            val p:Point = trans.transformPoint(points[index])
            if(index == 0 ){
                path.elements.add(MoveTo(p.x,p.y))
            }else{
                path.elements.add(LineTo(p.x,p.y))
            }
        }
    }

    override fun update(vararg points: Point) {

    }
}

