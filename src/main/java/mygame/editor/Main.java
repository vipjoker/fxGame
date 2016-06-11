package mygame.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Admin on 11.06.2016.
 */
public class Main  extends Application{

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent load = FXMLLoader.load(this.getClass().getResource("/editor.fxml"));
        primaryStage.setTitle("Editor");
        primaryStage.setScene(new Scene(load,1000,1000));
        primaryStage.show();
    }
}
