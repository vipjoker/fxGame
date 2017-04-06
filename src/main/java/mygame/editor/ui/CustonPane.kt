package mygame.editor.ui

import javafx.application.Platform
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.input.MouseButton.*
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import mygame.Constants.*
import mygame.editor.kotlin.ActionListenerDelegate
import mygame.editor.kotlin.Transformable
import mygame.editor.model.CustonAffine
import mygame.editor.model.Point
import kotlin.properties.Delegates


class CustonPane(width:Double,height:Double) : Pane() {

    private var delegate: ActionListenerDelegate by Delegates.notNull<ActionListenerDelegate>()


    var transform : CustonAffine = CustonAffine()
    get

    var infoText: Text


    val items: MutableList<Transformable> = mutableListOf()
    val horLine: Line
    val verLine: Line

    init {
        prefWidth = width
        prefHeight =height
        background = Background(BackgroundFill(BACKGROUND, null, null))
        cursor = Cursor.CROSSHAIR


        infoText = Text("Info")
        with(infoText) {
            fill = Color.AQUA
            x = 50.0
            y = 50.0
        }


        horLine = Line()
        verLine = Line()

        horLine.stroke = LIGHT_GREY
        verLine.stroke = LIGHT_GREY



        addChildren(horLine, verLine, infoText)


        Platform.runLater {
//            clip = Rectangle(width, height)

            transform.setToIdentity()
            transform.appendTranslation(width / 2, height / 2)
            transform.appendScale(1.0, -1.0)
            updateAll()
        }

        setListeners()

    }


    fun setListenerDelegate(delegate: ActionListenerDelegate) {
        this.delegate = delegate
    }

    fun updateAll() {


        val point = transform.transformPoint(Point(0.0, 0.0))
        horLine.startX = 0.0
        horLine.startY = point.y
        horLine.endX = width
        horLine.endY = point.y

        verLine.startX = point.x
        verLine.endX = point.x
        verLine.startY = 0.0
        verLine.endY = height

        items.forEach { it.transform(transform) }


    }

    fun addItem(item: Transformable) {
        items.add(item)
        item.transform(transform)
        children.add(item as Node)
    }

    fun removeItem(item: Transformable){
        children.remove(item as Node)
    }

    fun addChildren(vararg nodes: Node) {
        children.addAll(nodes)
    }

    var lastPoint: Point? = null
    fun setListeners() {

        setOnScroll {

            val point = transform.inverseTransform(it.x, it.y)


            if (it.deltaY > 0 && transform.mxx < 10) {

                transform.appendScale(1.05, 1.05, point)

                updateAll()
                it.consume()
            } else {
                if (transform.mxx > 0.1) {
                    transform.appendScale(.95, .95, point)
                    updateAll()
                    it.consume()
                }


            }
        }

        setOnMousePressed {
            if (it.button == SECONDARY) {
                lastPoint = transform.inverseDeltaTransformPoint(it.x, it.y)
            } else {
                val point = transform.inverseTransformPoint(it.x, it.y)
                delegate.onMousePressed(point)
                infoText.text = "lastX  ${point.x} transY ${point.y}  +  \n $transform"
            }
            updateAll()
        }

        setOnMouseDragged {

            if (it.button == SECONDARY && lastPoint != null) {
                val trans = transform.inverseDeltaTransformPoint(it.x, it.y)
                    val deltaX = trans.x - (lastPoint as Point).x
                    val deltaY = trans.y - (lastPoint as Point).y
                    transform.appendTranslation(deltaX, deltaY)

                lastPoint = trans
                it.consume()

            } else {
                delegate.onMouseDragged(transform.inverseTransformPoint(it.x, it.y))

            }
            updateAll()
        }

        setOnMouseReleased {
            if (it.button == SECONDARY) lastPoint = null
            else delegate.onMouseReleased(transform.inverseTransformPoint(it.x, it.y))

            updateAll()
        }
    }


}


