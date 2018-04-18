package mygame.editor.ui

import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.input.MouseButton.*
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.text.Text
import mygame.Constants.*
import mygame.editor.kotlin.ActionListenerDelegate
import mygame.editor.kotlin.Transformable
import mygame.editor.model.Point
import kotlin.properties.Delegates


class CustomPane(width: Double, height: Double) : Pane() {

    private var delegate: ActionListenerDelegate by Delegates.notNull()

    var root: Pane = Pane()

    private var infoText: Text

    private val items: MutableList<Transformable> = mutableListOf()

    private var labels: MutableList<Text> = mutableListOf()

    private var nodes: MutableList<Node> = mutableListOf();

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



        addChildren(root, infoText)

        val cellSize = 50.0
        val lineCount = 30
        val gridSize = cellSize * lineCount
        for (n in -lineCount..lineCount) {
            val coordinate = n * cellSize
            val verLine = Line(-gridSize, coordinate, gridSize, coordinate)
            verLine.stroke = if (n == 0) WHITE else LIGHT_GREY

            val horLine = Line(coordinate, -gridSize, coordinate, gridSize)
            horLine.stroke = if (n == 0) WHITE else LIGHT_GREY

            val labelX = Text(String.format("%.0f", coordinate))
            labelX.y = 0.0

            labelX.fill = Color.AQUA
            labelX.x = coordinate
            val labelY = Text(String.format("%.0f", coordinate))
            labelY.x = 0.0
            labelY.fill = Color.AQUA
            labelY.y = coordinate
            root.children.addAll(verLine, horLine, labelX, labelY)


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

    fun addItem(item: Node) {
//        items.add(item)
//        item.transform(transform)
        nodes.add(item)
        root.children.add(item)
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
        setOnMouseMoved { onMouseMoved(it) }
        setOnMousePressed { onMousePressed(it) }
        setOnMouseDragged { onMouseDragged(it) }
        setOnMouseReleased { onMouseReleased(it) }
    }

    private fun onScroll(event: ScrollEvent) {
        if (event.deltaY > 0) {
            root.scaleX *= 1.05
            root.scaleY *= 1.05
        } else {
            root.scaleX *= 0.95
            root.scaleY *= 0.95
        }
        updateAll()
        event.consume()


    }

    private fun onMouseMoved(event: MouseEvent) {

        val p = root.parentToLocal(event.x, event.y)

        infoText.text = String.format("%f %f", p.x, p.y)
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


