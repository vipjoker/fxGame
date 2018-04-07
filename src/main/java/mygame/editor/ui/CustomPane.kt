package mygame.editor.ui

import javafx.application.Platform
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.input.MouseButton.*
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
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


class CustomPane(width: Double, height: Double) : Pane() {

    private var delegate: ActionListenerDelegate by Delegates.notNull<ActionListenerDelegate>()

    var root: Pane = Pane()
//    var transform: CustonAffine = CustonAffine()

    var infoText: Text


    val items: MutableList<Transformable> = mutableListOf()
    val horLine: Line
    val verLine: Line

    init {
        prefWidth = width
        prefHeight = height
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



        addChildren(root, horLine, verLine, infoText)


        Platform.runLater {
            //            clip = Rectangle(width, height)

//            transform.setToIdentity()
//            transform.appendTranslation(width / 2, height / 2)
//            transform.appendScale(1.0, -1.0)
            updateAll()
        }

        setListeners()

    }


    fun setListenerDelegate(delegate: ActionListenerDelegate) {
        this.delegate = delegate
    }

    fun updateAll() {


//        val point = transform.transformPoint(Point(0.0, 0.0))
//        horLine.startX = 0.0
//        horLine.startY = point.y
//        horLine.endX = width
//        horLine.endY = point.y
//
//        verLine.startX = point.x
//        verLine.endX = point.x
//        verLine.startY = 0.0
//        verLine.endY = height


    }

    fun addItem(item: Transformable) {
        items.add(item)
//        item.transform(transform)
        children.add(item as Node)
    }

    fun removeItem(item: Transformable) {
        children.remove(item as Node)
    }

    private fun addChildren(vararg nodes: Node) {
        children.addAll(nodes)
    }

    private var lastPoint: Point? = null
    private fun setListeners() {

        setOnScroll { onScroll(it) }
        setOnMousePressed { onMousePressed(it) }
        setOnMouseDragged { onMouseDragged(it) }
        setOnMouseReleased { onMouseReleased(it) }
    }

    private fun onScroll(event: ScrollEvent) {
//        val point = transform.inverseTransform(event.x, event.y)


        if (event.deltaY > 0) {

//            transform.appendScale(1.05, 1.05, point)
            root.scaleX *= 1.05
            root.scaleY *= 1.05
            updateAll()
            event.consume()
        } else {
            root.scaleX *= 0.95
            root.scaleY *= 0.95
            updateAll()
            event.consume()


        }
    }

    private fun onMousePressed(event: MouseEvent) {
        if (event.button == SECONDARY) {

            lastPoint = Point(event.x, event.y)
        }
        updateAll()
    }

    private fun onMouseDragged(event: MouseEvent) {
        if (event.button == SECONDARY && lastPoint != null) {


            val deltaX = event.x - (lastPoint as Point).x
            val deltaY = event.y - (lastPoint as Point).y
            lastPoint?.x = event.x
            lastPoint?.y = event.y
            root.translateX += deltaX
            root.translateY += deltaY

            event.consume()

        } else {
            delegate.onMouseDragged(Point(event.x, event.y))

        }
        updateAll()

    }

    private fun onMouseReleased(event: MouseEvent) {
        if (event.button == SECONDARY) lastPoint = null
        else delegate.onMouseReleased(Point(event.x, event.y))

        updateAll()
    }


}


