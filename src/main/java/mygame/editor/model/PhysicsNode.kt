package mygame.editor.model

import javafx.scene.layout.Pane
import mygame.editor.Controller

/**
 * Created by oleh on 17.04.18.
 */
class PhysicsNode(val controller:Controller) : Pane() {

    var active:Boolean = false

    fun toggleActive(){
        active = !active
        if(active)setActive()
        else setNonActive()
    }

    fun setActive(){
        layoutX
        style = "-fx-border-color: #f0f; -fx-border-width: 5px;-fx-border-radius: 5px;"
    }

    fun setNonActive(){
        style = "-fx-border-color: none"
    }

}