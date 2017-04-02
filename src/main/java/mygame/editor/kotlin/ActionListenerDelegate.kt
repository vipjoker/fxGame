package mygame.editor.kotlin

import mygame.editor.model.Point


interface ActionListenerDelegate {
    fun onMousePressed(point :Point)
    fun onMouseReleased(point:Point)
    fun onMouseDragged(point: Point)
}
