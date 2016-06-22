package mygame.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Admin on 11.06.2016.
 */
public class App extends Application{

    private static App INSTANCE;
    private Stage window;

    public static App getInstance(){
        return INSTANCE;
    }

    public Stage getWindow(){
        return window;
    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        INSTANCE = this;
        Parent load = FXMLLoader.load(this.getClass().getResource("/editor.fxml"));
        primaryStage.setTitle("Editor");
        primaryStage.setScene(new Scene(load,800,600));
        primaryStage.show();
    }
}
