package mygame.editor

import javafx.application.Application
import javafx.application.Application.launch
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.Window


fun main(args: Array<String>) {
   launch(App::class.java)

}

object AppHolder{
    var instance:App?=null
}

class App : Application() {

    var window: Window? = null; get
    var scene: Scene? = null; get
    var stage: Stage? = null; get

    override fun start(primaryStage: Stage) {

        AppHolder.instance = this
        val load: Parent = FXMLLoader.load(this.javaClass.getResource("/editor.fxml"))

        scene = Scene(load, 1200.0, 800.0)

        primaryStage.title = "Editor"
        window = scene?.window
        stage = primaryStage

        primaryStage.scene = scene
        primaryStage.show()
    }



}

