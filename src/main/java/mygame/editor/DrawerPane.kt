package mygame.editor;

import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.Duration

class DrawerPane : Pane() {

    var rightContent = VBox()
    var leftContent = VBox()
    val center:StackPane = StackPane()
    internal var isOpen = true
    internal var rightIsOpen = true

    init {

        val pane = Pane()
        pane.prefWidth(100.0)
        pane.prefHeight(50.0)
        pane.background = Background(BackgroundFill(Color.GREEN, null, null))


//        background = Background(BackgroundFill(Color.RED, null, null))



        val left = ScrollPane()
        left.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        left.content = leftContent
        left.background = Background(BackgroundFill(Color.BLACK.deriveColor(1.0, 1.0, 1.0, .5), null, null))

        leftContent.alignmentProperty().value = Pos.CENTER
        leftContent.spacingProperty().setValue(10)



        val right = ScrollPane()
        right.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
        right.padding = Insets(0.0, 0.0, 0.0, 20.0)
        right.background = Background(BackgroundFill(Color.BLACK.deriveColor(1.0, 1.0, 1.0, .5), null, null))
        right.prefWidth = 200.0
        right.content = rightContent
        rightContent.alignmentProperty().value = Pos.CENTER
        rightContent.spacingProperty().setValue(10)


        center.background = Background(BackgroundFill(Color.WHEAT.deriveColor(1.0, 1.0, 1.0, 1.0), null, null))
        center.prefHeightProperty().bind(heightProperty())

        widthProperty().addListener { observable, oldValue, newValue ->
            var l = left.width
            var r = right.width
            if (!isOpen) l -= 180.0
            if (!rightIsOpen) r -= 180.0
            center.prefWidth = newValue.toDouble() - l - r



            right.layoutX = this.width - right.width
        }


        left.translateXProperty().addListener { observable, oldValue, newValue ->
            val width = newValue.toDouble() - oldValue.toDouble()
            center.layoutX = center.layoutX + width
            center.prefWidth = center.width - width
        }

        right.translateXProperty().addListener { observable, oldValue, newValue ->
            val width = newValue.toDouble() - oldValue.toDouble()
            center.prefWidth = center.width + width

        }


        left.prefWidth = 200.0
        left.prefHeightProperty().bind(heightProperty())
        right.prefHeightProperty().bind(heightProperty())
        children.addAll(left, center, right)


        left.setOnMouseClicked { event ->
            val translateTransition = TranslateTransition(Duration.millis(500.0), left)
            translateTransition.toX = (if (isOpen) -180 else 0).toDouble()
            translateTransition.play()
            isOpen = !isOpen
        }

        right.setOnMouseClicked { event ->
            val translateTransition = TranslateTransition(Duration.millis(500.0), right)
            translateTransition.toX = (if (rightIsOpen) 180 else 0).toDouble()
            translateTransition.play()
            rightIsOpen = !rightIsOpen
        }



        Platform.runLater {
            center.layoutX = left.width
            center.prefWidth = width - left.width - right.width
            right.layoutX = width - right.width

        }


    }


    fun getLeft():VBox{
        return leftContent
    }

    fun getRight():VBox{
        return rightContent
    }

    fun getCenterPane():StackPane{
        return center
    }

}

