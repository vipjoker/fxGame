package mygame.editor;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Camera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Created by Admin on 11.06.2016.
 */
public class App extends Application{

    private static App INSTANCE;
    private Window window;
    private Scene scene;
    private Stage stage;
    public static App getInstance(){
        return INSTANCE;
    }

    public Window getWindow(){
        return window;
    }


    public Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        INSTANCE = this;
        Parent load = FXMLLoader.load(this.getClass().getResource("/editor.fxml"));
        primaryStage.setTitle("Editor");
        scene  = new Scene(load,800,600);
        window  = scene.getWindow();
        stage = primaryStage;

        primaryStage.setScene(scene);
        primaryStage.show();

//
//        scene.widthProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println("Width: " + newSceneWidth);
//            }
//        });
//        scene.heightProperty().addListener(new ChangeListener<Number>() {
//            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                System.out.println("Height: " + newSceneHeight);
//            }
//        });
    }
}
