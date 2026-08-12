package newStuff.box2dEditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World!");
        URL resource = getClass().getResource("/main.fxml");

        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();

        MainController controller = loader.getController();

        Scene scene = new Scene(root,1280,720);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}